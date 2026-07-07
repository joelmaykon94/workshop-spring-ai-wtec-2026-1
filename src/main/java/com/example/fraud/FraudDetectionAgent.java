package com.example.fraud;

import com.langfuse.client.LangfuseClient;
import com.langfuse.client.resources.ingestion.requests.IngestionRequest;
import com.langfuse.client.resources.ingestion.types.IngestionEvent;
import com.langfuse.client.resources.ingestion.types.TraceBody;
import com.langfuse.client.resources.ingestion.types.TraceEvent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

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

    public FraudAnalysis analyze(Transaction transaction) {
        // 1. Busca vetorial no PGVector
        List<String> similarTransactions = vectorSearchService.findSimilarSuspiciousTransactions(transaction);
        
        // 2. Instrumentação de Trace no Langfuse
        String traceId = UUID.randomUUID().toString();
        try {
            langfuseClient.ingestion().batch(IngestionRequest.builder()
                    .addBatch(IngestionEvent.traceCreate(TraceEvent.builder()
                            .id(traceId)
                            .timestamp(java.time.Instant.now().toString())
                            .body(TraceBody.builder()
                                    .name("FraudAnalysisFlow")
                                    .sessionId(transaction.userId())
                                    .build())
                            .build()))
                    .build());
        } catch (Exception e) {
            System.err.println("Erro ao registrar trace no Langfuse: " + e.getMessage());
        }

        // 3. Montagem dos Prompts
        String systemPrompt = """
            Você é um agente sênior de detecção de fraudes financeiras.
            Analise a transação fornecida e decida se é fraudulenta.
            Leve em consideração o histórico de transações similares (RAG).
            Sempre responda em português.
            """;
            
        String userPrompt = String.format("""
            Transação Atual:
            %s
            
            Transações Históricas Similares (Contexto RAG):
            %s
            """, transaction.toString(), String.join("\n", similarTransactions));

        // 4. Buscar imagem do MinIO ou decodificar Base64 (se houver comprovante na transação)
        byte[] imageBytes = null;
        if (transaction.receiptImage() != null && !transaction.receiptImage().isBlank()) {
            String imageStr = transaction.receiptImage();
            if (imageStr.startsWith("data:image") || imageStr.length() > 255) {
                // É uma string Base64
                String base64Data = imageStr.contains(",") ? imageStr.split(",")[1] : imageStr;
                imageBytes = java.util.Base64.getDecoder().decode(base64Data.trim());
            } else {
                // É um nome de arquivo no MinIO
                imageBytes = minioService.getImageBytes(imageStr);
            }
        }

        final byte[] finalImageBytes = imageBytes;

        // 5. Chamada ao Llama 3.2-Vision via Spring AI
        return chatClient.prompt()
            .system(systemPrompt)
            .user(u -> {
                u.text(userPrompt);
                if (finalImageBytes != null) {
                    u.media(new Media(MimeTypeUtils.IMAGE_JPEG, new org.springframework.core.io.ByteArrayResource(finalImageBytes)));
                }
            })
            .call()
            .entity(FraudAnalysis.class);
    }
}
