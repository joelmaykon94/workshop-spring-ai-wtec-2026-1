# Etapa 2: Implementando RAG e Busca Vetorial no Antifraude

Este é o roteiro prático para o **Slide 6**. O objetivo aqui é codificar a classe `VectorSearchService`, que fará a busca de histórico suspeito no PostgreSQL (pgvector).

## O que vamos fazer?
Vamos transformar a transação atual do usuário em texto, converter em _embeddings_ (usando o `nomic-embed-text` do Ollama) e buscar transações parecidas no histórico do banco vetorial. Essa informação será nosso "Contexto" (Retrieval) para a Geração (Augmented Generation).

---

## 💻 Mão na massa (Hands-on)

Abra o arquivo `src/main/java/com/example/fraud/VectorSearchService.java` que está apenas com os `// TODO` e substitua pelo código abaixo:

```java
package com.example.fraud;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;

    // 1. Injeção de Dependência do VectorStore
    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Busca transações suspeitas similares no banco de dados vetorial (pgvector).
     * @param transaction A transação atual que está sendo analisada.
     * @return Lista de strings representando o histórico de transações similares.
     */
    public List<String> findSimilarSuspiciousTransactions(Transaction transaction) {
        
        // 2. Transformar a transação em um texto descritivo para gerar o Embedding
        String searchQuery = String.format(
            "merchant: %s, location: %s, amount: %.2f",
            transaction.merchant(), transaction.location(), transaction.amount()
        );

        // 3. Executar a busca de Similaridade Cosine (Top 3 resultados)
        List<Document> results = vectorStore.similaritySearch(
            SearchRequest.builder().query(searchQuery).topK(3).build()
        );

        // 4. Mapear os Documentos encontrados de volta para Texto Plano
        return results.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }
}
```

### 🧠 Explicação Didática para a Turma (Dicas de Palestra):
1. **O que é o `VectorStore`?** Destaque que o Spring AI abstrai a complexidade do banco. Esse objeto está se conectando no Postgres e rodando consultas matemáticas (`HNSW` e similaridade de cosseno) sem a gente escrever nenhum SQL!
2. **O que é a Busca Vetorial na prática?** Em vez de usar cláusulas `WHERE amount = X`, o banco de dados encontra transações "semanticamente parecidas" com a atual. Isso é perfeito para achar golpistas que operam contas com padrões sutis e variados.
3. **Top K (3):** Estamos limitando o contexto para as 3 transações mais relevantes, para economizar o limite de _tokens_ (janela de contexto) quando enviarmos tudo para o LLM na próxima etapa.
