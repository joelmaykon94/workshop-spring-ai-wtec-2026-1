# 🛠️ Guia Prático: Implementando o Agente de IA (Passo a Passo)

Neste guia, você construirá o cérebro da nossa aplicação: o **`FraudDetectionAgent.java`**.
Abra o arquivo `src/main/java/com/example/fraud/service/FraudDetectionAgent.java` no seu editor e siga as etapas abaixo para preencher cada um dos `TODO`s.

---

### ETAPA 1: RAG (Retrieval-Augmented Generation)
Vamos buscar no nosso banco de dados vetorial (PGVector) se existe algum histórico de fraude semelhante a esta transação.

**Substitua o TODO da Etapa 1 por:**
```java
List<String> similarTransactions = vectorSearchService.findSimilarSuspiciousTransactions(transaction);
```

---

### ETAPA 2: OBSERVABILIDADE (Início)
Para habilitar o monitoramento e rastreamento da decisão do LLM, iniciamos a instrumentação via Langfuse.

**Substitua o TODO da Etapa 2 por:**
```java
String traceId = UUID.randomUUID().toString();
startLangfuseTrace(traceId, transaction);
```

---

### ETAPA 3: MULTIMODALIDADE (Visão e Áudio Computacional)
Caso a transação possua artefatos como um cupom fiscal/recibo ou autorização por voz, extraímos os dados do Storage S3 (MinIO) para processamento multimodal.

**Substitua o TODO da Etapa 3 por:**
```java
byte[] imageBytes = extractReceiptImage(transaction);
byte[] audioBytes = extractVoiceAuth(transaction);
```

---

### ETAPA 4: ENGENHARIA DE PROMPT
Nesta etapa, definimos o prompt do sistema (System Prompt) com as diretrizes de análise de risco e injetamos os dados da transação integrados ao contexto vetorial (User Prompt).

**Substitua o TODO da Etapa 4 por:**
```java
String systemPrompt = """
    Você é um agente sênior de detecção de fraudes financeiras.
    Analise a transação fornecida e decida se é fraudulenta.
    Leve em consideração o histórico de transações similares (RAG).
    Se houver imagem, avalie a veracidade do comprovante fiscal.
    Se houver áudio, ele contém a autorização por voz do cliente. Transcreva-o mentalmente e verifique se o tom e o que é falado condizem com a transação ou se parece um golpista com muita pressa.
    Sempre responda em português.
    """;
    
String userPrompt = String.format("""
    Transação Atual:
    %s
    
    Transações Históricas Similares (Contexto RAG):
    %s
    """, transaction.toString(), String.join("\\n", similarTransactions));
```

---

### ETAPA 5: INTEGRAÇÃO LLM (Gemini 1.5 Flash)
A comunicação com a API do Gemini é realizada através do `ChatClient` do ecossistema Spring AI. O processamento multimodal é injetado programaticamente.
*O método `.entity(FraudAnalysis.class)` orquestra automaticamente o parser do JSON retornado pelo modelo para o Record Java instanciado.*

**Substitua o TODO da Etapa 5 por:**
```java
OffsetDateTime startTime = OffsetDateTime.now();

FraudAnalysis analysis = chatClient.prompt()
    .system(systemPrompt)
    .user(u -> {
        u.text(userPrompt);
        if (imageBytes != null) {
            u.media(new Media(MimeTypeUtils.IMAGE_JPEG, new org.springframework.core.io.ByteArrayResource(imageBytes)));
        }
        if (audioBytes != null) {
            u.media(new Media(MimeTypeUtils.parseMimeType("audio/mpeg"), new org.springframework.core.io.ByteArrayResource(audioBytes)));
        }
    })
    .call()
    .entity(FraudAnalysis.class);
    
OffsetDateTime endTime = OffsetDateTime.now();
```

---

### ETAPA 6: OBSERVABILIDADE (Fim)
A etapa de rastreabilidade é finalizada consolidando os dados de latência, consumo de tokens e a deliberação analítica final no dashboard do Langfuse.

**Substitua o TODO da Etapa 6 por:**
```java
finishLangfuseTrace(traceId, systemPrompt, userPrompt, analysis, startTime, endTime);
```

---

### FINALIZANDO:
Conclua a implementação removendo a instrução estática de "mock" ao final do método e retornando a inferência gerada pelo Agente:

**Remova:**
```java
// TODO: Retornar a análise real feita pela IA.
return new FraudAnalysis(false, "Agente de IA em construção! Implemente os TODOs durante o minicurso.", 0.0);
```

**E adicione:**
```java
return analysis;
```

---

### Conclusão
O Agente de Inteligência Artificial Multimodal foi implementado com sucesso. Retorne ao arquivo `README.md` e execute as requisições detalhadas na seção **"Simulando o Tráfego em Tempo Real"** para validar o processamento em massa.
