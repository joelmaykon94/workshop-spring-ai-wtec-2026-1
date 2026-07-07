package com.example.fraud;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudDetectionAgent agent;
    private final VectorSearchService vectorSearchService;
    private final TransactionSagaService sagaService;

    public FraudController(FraudDetectionAgent agent, VectorSearchService vectorSearchService, TransactionSagaService sagaService) {
        this.agent = agent;
        this.vectorSearchService = vectorSearchService;
        this.sagaService = sagaService;
    }

    @GetMapping("/index")
    public String index() {
        return "Fraud Detection API is running!";
    }

    @PostMapping("/analyze")
    public FraudAnalysis analyzeTransaction(@RequestBody Transaction transaction) {
        FraudAnalysis analysis = agent.analyze(transaction);
        
        // Se for fraude, inicia a Saga de compensação (estorno)
        if (analysis.isFraud()) {
            sagaService.initiateCompensation(transaction.id(), analysis.reason());
        }
        
        return analysis;
    }

    @PostMapping("/seed")
    public String seedSuspiciousTransaction(@RequestBody Transaction transaction) {
        vectorSearchService.addSuspiciousTransaction(transaction);
        return "Transação adicionada à base de conhecimento (RAG)!";
    }
}
