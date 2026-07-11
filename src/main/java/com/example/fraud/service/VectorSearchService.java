package com.example.fraud.service;
import com.example.fraud.model.Transaction;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Busca transações suspeitas similares no banco de dados vetorial (pgvector).
     * 
     * @param transaction A transação atual que está sendo analisada.
     * @return Lista de strings representando as transações similares encontradas.
     */
    public List<String> findSimilarSuspiciousTransactions(Transaction transaction) {
        // 1. Converter a transação em texto para a busca (RAG)
        String searchQuery = String.format(
            "merchant: %s, location: %s, amount: %.2f",
            transaction.merchant(), transaction.location(), transaction.amount()
        );

        // 2. Usar o VectorStore para buscar documentos similares
        List<Document> results = vectorStore.similaritySearch(
            SearchRequest.builder().query(searchQuery).topK(3).build()
        );

        // 3. Retornar os resultados extraindo o texto do Documento
        return results.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }

    /**
     * Adiciona uma transação conhecida como fraudulenta ao banco de dados vetorial.
     * Isso alimentará a base de conhecimento do RAG para futuras detecções.
     * @param transaction Transação fraudulenta.
     */
    public void addSuspiciousTransaction(Transaction transaction) {
        String content = String.format(
            "merchant: %s, location: %s, amount: %.2f",
            transaction.merchant(), transaction.location(), transaction.amount()
        );
        
        Document document = new Document(content);
        vectorStore.add(List.of(document));
    }
}
