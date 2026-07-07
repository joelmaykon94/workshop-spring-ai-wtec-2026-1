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
Para não ficarmos "no escuro" sobre o que o LLM está fazendo, vamos iniciar o rastreamento (Trace) no Langfuse.

**Substitua o TODO da Etapa 2 por:**
```java
String traceId = UUID.randomUUID().toString();
startLangfuseTrace(traceId, transaction);
```

---

### ETAPA 3: MULTIMODALIDADE (Visão e Áudio Computacional)
A transação possui um cupom fiscal/recibo ou áudio de autorização de voz? Se sim, vamos baixá-lo do nosso S3 (MinIO) para enviar aos "sentidos" da IA.

**Substitua o TODO da Etapa 3 por:**
```java
byte[] imageBytes = extractReceiptImage(transaction);
byte[] audioBytes = extractVoiceAuth(transaction);
```

---

### ETAPA 4: ENGENHARIA DE PROMPT
Aqui nós programamos a IA usando a linguagem humana. Vamos definir as regras de negócio (`systemPrompt`) e injetar os dados da transação + o contexto RAG (`userPrompt`).

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

### ETAPA 5: CHAMADA AO LLM (Gemini 1.5 Flash)
Agora é a hora do show! Usando o `ChatClient` do Spring AI, vamos enviar tudo isso para o modelo do Google Gemini.
*A mágica acontece no `.entity(FraudAnalysis.class)`, onde o Spring AI força o LLM a devolver um JSON perfeito e já o converte para nossa classe Java!*

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
A IA respondeu. Vamos enviar a latência, o custo de tokens e a decisão tomada de volta para o Dashboard do Langfuse.

**Substitua o TODO da Etapa 6 por:**
```java
finishLangfuseTrace(traceId, systemPrompt, userPrompt, analysis, startTime, endTime);
```

---

### FINALIZANDO:
Por fim, no final do método, apague o "mock" que estava lá e retorne a análise real gerada pelo Gemini:

**Apague:**
```java
// TODO: Retornar a análise real feita pela IA.
return new FraudAnalysis(false, "Agente de IA em construção! Implemente os TODOs durante o minicurso.", 0.0);
```

**E substitua por:**
```java
return analysis;
```

---

### 🎉 Parabéns!
Seu Agente de Inteligência Artificial Multimodal está pronto! Volte ao `README.md` e veja a seção **"Simulando o Tráfego em Tempo Real"** para colocar seu agente à prova sob estresse!
