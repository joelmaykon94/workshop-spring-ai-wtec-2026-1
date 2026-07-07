package com.example.fraud.service;

import com.example.fraud.model.*;
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
        // TODO: Busca o contexto do usuário e padrões de fraude no Banco Vetorial.
        // =======================================================


        // =======================================================
        // ETAPA 2: OBSERVABILIDADE (Início)
        // TODO: Inicia o rastreamento da transação no Langfuse.
        // =======================================================


        // =======================================================
        // ETAPA 3: MULTIMODALIDADE (Visão e Áudio Computacional)
        // TODO: Extrai a imagem do recibo e o áudio de autorização do MinIO (S3)
        // =======================================================


        // =======================================================
        // ETAPA 4: ENGENHARIA DE PROMPT
        // TODO: Prepara as instruções combinando RAG e Dados da Transação. Adicionar menção de áudio.
        // =======================================================


        // =======================================================
        // ETAPA 5: CHAMADA AO LLM (Gemini 1.5 Flash)
        // TODO: Envia o texto + imagem + áudio + regras de negócio (Spring AI).
        // =======================================================


        // =======================================================
        // ETAPA 6: OBSERVABILIDADE (Fim)
        // TODO: Atualiza o trace no Langfuse com a resposta, tokens e latência.
        // =======================================================


        // TODO: Retornar a análise real feita pela IA.
        return new FraudAnalysis(false, "Agente de IA em construção! Implemente os TODOs durante o minicurso.", 0.0);
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

    private byte[] extractVoiceAuth(Transaction transaction) {
        if (transaction.voiceAuth() == null || transaction.voiceAuth().isBlank()) {
            return null;
        }
        return minioService.getImageBytes(transaction.voiceAuth());
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
