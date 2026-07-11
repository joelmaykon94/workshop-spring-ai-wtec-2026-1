package com.example.fraud.service;

import com.example.fraud.model.FraudAnalysis;
import com.example.fraud.model.Transaction;
import com.langfuse.client.LangfuseClient;
import com.langfuse.client.resources.ingestion.requests.IngestionRequest;
import com.langfuse.client.resources.ingestion.types.*;
import com.langfuse.client.resources.commons.types.Usage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
public class FraudDetectionAgent implements FraudAnalyzer {

    private final VectorSearchService vectorSearchService;
    private final ChatClient chatClient;
    private final MinioService minioService;
    private final LangfuseClient langfuseClient;

    public FraudDetectionAgent(VectorSearchService vectorSearchService, ChatClient.Builder chatClientBuilder, MinioService minioService, LangfuseClient langfuseClient) {
        this.vectorSearchService = vectorSearchService;
        this.chatClient = chatClientBuilder.build();
        this.minioService = minioService;
        this.langfuseClient = langfuseClient;
    }

    /**
     * Orquestrador Principal do Agente de IA.
     * Este método foi desenhado em etapas claras para o Workshop!
     */
    @Override
    public FraudAnalysis analyze(Transaction transaction) {
        
        // =======================================================
        // ETAPA 1: RAG (Retrieval-Augmented Generation)
        // Busca o contexto do usuário e padrões de fraude no Banco Vetorial.
        // =======================================================
        List<String> similarTransactions = vectorSearchService.findSimilarSuspiciousTransactions(transaction);

        // =======================================================
        // ETAPA 2: OBSERVABILIDADE (Início)
        // Inicia o rastreamento da transação no Langfuse.
        // =======================================================
        String traceId = UUID.randomUUID().toString();
        startLangfuseTrace(traceId, transaction);

        // =======================================================
        // ETAPA 3: MULTIMODALIDADE (Visão Computacional)
        // Extrai a imagem do recibo do MinIO (S3) para análise visual.
        // =======================================================
        byte[] imageBytes = extractReceiptImage(transaction);

        // =======================================================
        // ETAPA 4: ENGENHARIA DE PROMPT
        // Prepara as instruções combinando RAG e Dados da Transação.
        // =======================================================
        String systemPrompt = """
            Você é um agente sênior de detecção de fraudes financeiras.
            Analise a transação fornecida e decida se é fraudulenta.
            Leve em consideração o histórico de transações similares (RAG) e a imagem do comprovante.
            Sempre responda em português.
            """;
            
        String userPrompt = String.format("""
            Transação Atual:
            %s
            
            Transações Históricas Similares (Contexto RAG):
            %s
            """, transaction.toString(), String.join("\n", similarTransactions));

        // =======================================================
        // ETAPA 5: CHAMADA AO LLM (Gemini 1.5 Flash)
        // Envia o texto + imagem + regras de negócio (Spring AI).
        // =======================================================
        OffsetDateTime startTime = OffsetDateTime.now();
        
        FraudAnalysis analysis = chatClient.prompt()
            .system(systemPrompt)
            .user(u -> {
                u.text(userPrompt);
                if (imageBytes != null) {
                    u.media(new Media(MimeTypeUtils.IMAGE_JPEG, new org.springframework.core.io.ByteArrayResource(imageBytes)));
                }
            })
            .call()
            .entity(FraudAnalysis.class); // O Spring AI cuida de fazer o parser do JSON pro Java Record!
            
        OffsetDateTime endTime = OffsetDateTime.now();

        // =======================================================
        // ETAPA 6: OBSERVABILIDADE (Fim)
        // Atualiza o trace no Langfuse com a resposta, tokens e latência.
        // =======================================================
        finishLangfuseTrace(traceId, systemPrompt, userPrompt, analysis, startTime, endTime);

        return analysis;
    }


    // -------------------------------------------------------------------------
    // MÉTODOS AUXILIARES (Escondendo a complexidade técnica para facilitar a aula)
    // -------------------------------------------------------------------------

    private byte[] extractReceiptImage(Transaction transaction) {
        if (transaction.receiptImage() == null || transaction.receiptImage().isBlank()) {
            return null;
        }
        String imageStr = transaction.receiptImage();
        if (imageStr.startsWith("data:image") || imageStr.length() > 255) {
            String base64Data = imageStr.contains(",") ? imageStr.split(",")[1] : imageStr;
            return Base64.getDecoder().decode(base64Data.trim());
        } else {
            return minioService.getImageBytes(imageStr);
        }
    }

    private void startLangfuseTrace(String traceId, Transaction transaction) {
        try {
            langfuseClient.ingestion().batch(IngestionRequest.builder()
                    .addBatch(IngestionEvent.traceCreate(TraceEvent.builder()
                            .id(UUID.randomUUID().toString())
                            .timestamp(java.time.Instant.now().toString())
                            .body(TraceBody.builder()
                                    .id(traceId)
                                    .name("FraudAnalysisFlow")
                                    .sessionId(transaction.userId())
                                    .build())
                            .build()))
                    .build());
        } catch (Exception e) {
            System.err.println("Erro ao iniciar trace: " + e.getMessage());
        }
    }

    private void finishLangfuseTrace(String traceId, String systemPrompt, String userPrompt, FraudAnalysis analysis, OffsetDateTime start, OffsetDateTime end) {
        try {
            String fullInput = systemPrompt + "\n" + userPrompt;
            String finalDecision = analysis.isFraud() ? "FRAUDE - " + analysis.reason() : "LEGÍTIMO - " + analysis.reason();
            
            langfuseClient.ingestion().batch(IngestionRequest.builder()
                    .addBatch(IngestionEvent.traceCreate(TraceEvent.builder()
                            .id(UUID.randomUUID().toString())
                            .timestamp(start.toString())
                            .body(TraceBody.builder()
                                    .id(traceId) // Upsert no Trace original
                                    .input(userPrompt)
                                    .output(finalDecision)
                                    .build())
                            .build()))
                    .addBatch(IngestionEvent.generationCreate(CreateGenerationEvent.builder()
                            .id(UUID.randomUUID().toString())
                            .timestamp(start.toString())
                            .body(CreateGenerationBody.builder()
                                    .id(UUID.randomUUID().toString()) // Geração vinculada ao Trace
                                    .traceId(traceId)
                                    .name("Gemini-Fraud-Analysis")
                                    .model("gemini-3.5-flash")
                                    .startTime(start)
                                    .endTime(end)
                                    .input(fullInput)
                                    .output(analysis.reason())
                                    // Tokens simulados devido a limitação atual do Spring AI M5 com Gemini
                                    .usage(IngestionUsage.of(Usage.builder().input(1500).output(250).total(1750).build()))
                                    .build())
                            .build()))
                    .build());
        } catch (Exception e) {
            System.err.println("Erro ao fechar trace: " + e.getMessage());
        }
    }
}
