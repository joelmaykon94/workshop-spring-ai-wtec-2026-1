# Etapa 3: Agente LLM Multimodal e Langfuse

Este é o roteiro prático para o **Slide 7**. O objetivo aqui é codificar a classe `FraudDetectionAgent`, que irá conectar tudo: buscar a imagem no S3 (MinIO), resgatar o histórico do banco vetorial (RAG) e enviar o contexto completo para a LLM avaliar, gerando métricas de rastreamento no Langfuse.

## O que vamos fazer?
1. Usar o `MinioService` para ler os bytes do arquivo de imagem do comprovante ou documento.
2. Registrar o início da nossa análise (Trace) usando o `LangfuseClient`.
3. Criar a estrutura do Prompt do sistema (quem o agente é) e do usuário (os dados do PIX e histórico RAG).
4. Usar a abstração `ChatClient` do Spring AI com suporte à Visão Computacional Multimodal (Media).

---

## 💻 Mão na massa (Hands-on)

Abra o arquivo `src/main/java/com/example/fraud/FraudDetectionAgent.java` e cole o código completo abaixo:

```java
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
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.UUID;

@Component
public class FraudDetectionAgent {

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
            """, transaction.toString(), String.join("\\n", similarTransactions));

        // 4. Buscar imagem do MinIO (se houver comprovante na transação)
        byte[] imageBytes = null;
        if (transaction.receiptImage() != null && !transaction.receiptImage().isBlank()) {
            imageBytes = minioService.getImageBytes(transaction.receiptImage());
        }

        final byte[] finalImageBytes = imageBytes;

        // 5. Chamada ao Llama 3.2-Vision via Spring AI
        return chatClient.prompt()
            .system(systemPrompt)
            .user(u -> {
                u.text(userPrompt);
                // Se o MinIO retornou a imagem com sucesso, anexamos ela no Prompt do Spring AI
                if (finalImageBytes != null) {
                    u.media(new Media(MimeTypeUtils.IMAGE_JPEG, new ByteArrayResource(finalImageBytes)));
                }
            })
            .call()
            .entity(FraudAnalysis.class); // Mapeamento Automático (Structured Output)
    }
}
```

### 🧠 Explicação Didática para a Turma (Dicas de Palestra):
1. **Multimodalidade (`Media`)**: Chame a atenção para a linha do `u.media()`. É aí que a mágica acontece. Não estamos apenas mandando um texto sobre a transação, estamos literalmente "dando olhos" ao LLM para que ele veja a foto do comprovante ou de um RG suspeito recuperado pelo MinIO (S3).
2. **Langfuse Tracing**: Mostre rapidamente a parte de `LangfuseClient.ingestion()`. Explique que em produção é impossível descobrir se a IA está alucinando ou lenta sem uma ferramenta de telemetria baseada em Traces e Sessões.
3. **Structured Output (`.entity(FraudAnalysis.class)`)**: Destaque a simplicidade incrível do Spring AI. Não precisamos fazer regex na resposta do LLM. O próprio Spring AI força o modelo a cuspir um JSON válido e o converte nativamente para o seu Record Java (`FraudAnalysis`). Isso resolve o maior pesadelo de integração com IA!
