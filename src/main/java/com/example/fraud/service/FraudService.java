package com.example.fraud.service;

import com.example.fraud.model.FraudAnalysis;
import com.example.fraud.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class FraudService {

    private final FraudAnalyzer fraudAnalyzer;
    private final VectorSearchService vectorSearchService;
    private final TransactionSagaService sagaService;

    public FraudService(FraudAnalyzer fraudAnalyzer, VectorSearchService vectorSearchService, TransactionSagaService sagaService) {
        this.fraudAnalyzer = fraudAnalyzer;
        this.vectorSearchService = vectorSearchService;
        this.sagaService = sagaService;
    }

    public FraudAnalysis processTransaction(Transaction transaction) {
        FraudAnalysis analysis = fraudAnalyzer.analyze(transaction);

        if (analysis.isFraud()) {
            sagaService.initiateCompensation(transaction.id(), analysis.reason());
        }

        return analysis;
    }

    public String seedTransaction(Transaction transaction) {
        vectorSearchService.addSuspiciousTransaction(transaction);
        return "Transação adicionada à base de conhecimento (RAG)!";
    }
}
