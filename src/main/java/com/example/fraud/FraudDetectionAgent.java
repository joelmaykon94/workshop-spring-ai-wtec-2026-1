package com.example.fraud;

import org.springframework.stereotype.Component;

@Component
public class FraudDetectionAgent {

    private final VectorSearchService vectorSearchService;
    // TODO: Injetar ChatClient ou outros componentes necessários

    public FraudDetectionAgent(VectorSearchService vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }

    /**
     * Analisa uma transação para determinar se é fraude, correlacionando 
     * com o histórico vetorial (RAG) e chamando o modelo LLM.
     * 
     * @param transaction A transação a ser analisada
     * @return Objeto FraudAnalysis contendo o veredito e justificativa.
     */
    public FraudAnalysis analyze(Transaction transaction) {
        // TODO: Implementar a lógica do agente cognitivo
        // 1. Chamar vectorSearchService.findSimilarSuspiciousTransactions(transaction)
        // 2. Construir o Prompt com a transação atual e o histórico retornado pelo RAG
        // 3. Chamar o ChatClient (LLM) para analisar e decidir
        // 4. Mapear a resposta do LLM para o record FraudAnalysis
        return new FraudAnalysis(false, "Not implemented yet", 0.0);
    }
}
