package com.example.fraud;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FraudDetectionAgent {

    private final VectorSearchService vectorSearchService;
    private final ChatClient chatClient;

    public FraudDetectionAgent(VectorSearchService vectorSearchService, ChatClient.Builder chatClientBuilder) {
        this.vectorSearchService = vectorSearchService;
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Analisa uma transação para determinar se é fraude, correlacionando 
     * com o histórico vetorial (RAG) e chamando o modelo LLM.
     * 
     * @param transaction A transação a ser analisada
     * @return Objeto FraudAnalysis contendo o veredito e justificativa.
     */
    public FraudAnalysis analyze(Transaction transaction) {
        // 1. Chamar busca vetorial para encontrar contexto (RAG)
        List<String> similarTransactions = vectorSearchService.findSimilarSuspiciousTransactions(transaction);
        
        // 2. Construir o Prompt System
        String systemPrompt = """
            Você é um agente sênior de detecção de fraudes financeiras.
            Analise a transação fornecida e decida se é fraudulenta.
            Leve em consideração o histórico de transações similares suspeitas (RAG).
            Sempre responda em português.
            """;
            
        // 3. Construir o Prompt User
        String userPrompt = String.format("""
            Transação Atual:
            %s
            
            Transações Históricas Similares (Contexto RAG):
            %s
            """, transaction.toString(), String.join("\n", similarTransactions));

        // 4. Chamar o ChatClient (LLM) e mapear diretamente para o record FraudAnalysis
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .entity(FraudAnalysis.class);
    }
}
