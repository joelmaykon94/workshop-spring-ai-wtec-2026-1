package com.example.fraud;

import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.controller.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudControllerTest {

    @Test
    void shouldAnalyzeTransactionAndNotTriggerSagaWhenNotFraud() {
        // Arrange
        Transaction tx = new Transaction("tx-1", "user-1", 100.0, "Supermercado", "SP", "dev-1", "127.0.0.1", "2026-07-07T10:00:00Z", null);
        
        StubFraudAnalyzer agent = new StubFraudAnalyzer(new FraudAnalysis(false, "Transação padrão.", 0.95));
        StubTransactionSagaService sagaService = new StubTransactionSagaService();
        StubVectorSearchService vectorService = new StubVectorSearchService();
        
        FraudService fraudService = new FraudService(agent, vectorService, sagaService);
        FraudController controller = new FraudController(fraudService);

        // Act
        FraudAnalysis result = controller.analyzeTransaction(tx);

        // Assert
        assertEquals(false, result.isFraud());
        assertEquals("Transação padrão.", result.reason());
        assertEquals(0.95, result.confidenceScore());
        assertEquals(0, sagaService.compensationCount);
    }

    @Test
    void shouldAnalyzeTransactionAndTriggerSagaWhenFraudDetected() {
        // Arrange
        Transaction tx = new Transaction("tx-2", "user-2", 15000.0, "Loja de Eletrônicos", "RJ", "dev-2", "192.168.1.1", "2026-07-07T02:00:00Z", null);
        
        StubFraudAnalyzer agent = new StubFraudAnalyzer(new FraudAnalysis(true, "Valor atípico.", 0.99));
        StubTransactionSagaService sagaService = new StubTransactionSagaService();
        StubVectorSearchService vectorService = new StubVectorSearchService();
        
        FraudService fraudService = new FraudService(agent, vectorService, sagaService);
        FraudController controller = new FraudController(fraudService);

        // Act
        FraudAnalysis result = controller.analyzeTransaction(tx);

        // Assert
        assertEquals(true, result.isFraud());
        assertEquals("Valor atípico.", result.reason());
        assertEquals(0.99, result.confidenceScore());
        assertEquals(1, sagaService.compensationCount);
        assertEquals("tx-2", sagaService.lastTransactionId);
    }

    @Test
    void shouldSeedSuspiciousTransactionSuccessfully() {
        // Arrange
        Transaction tx = new Transaction("tx-3", "user-3", 500.0, "Loja Falsa", "SP", "dev-3", "10.0.0.1", "2026-07-07T12:00:00Z", null);
        
        StubVectorSearchService vectorService = new StubVectorSearchService();
        FraudService fraudService = new FraudService(null, vectorService, null);
        FraudController controller = new FraudController(fraudService);

        // Act
        String result = controller.seedSuspiciousTransaction(tx);

        // Assert
        assertEquals("Transação adicionada à base de conhecimento (RAG)!", result);
        assertEquals(1, vectorService.addCount);
    }

    // --- STUBS ---

    static class StubFraudAnalyzer implements FraudAnalyzer {
        private final FraudAnalysis mockResult;

        public StubFraudAnalyzer(FraudAnalysis mockResult) {
            this.mockResult = mockResult;
        }

        @Override
        public FraudAnalysis analyze(Transaction transaction) {
            return mockResult;
        }
    }

    static class StubTransactionSagaService extends TransactionSagaService {
        int compensationCount = 0;
        String lastTransactionId = null;

        public StubTransactionSagaService() {
            super(null);
        }

        @Override
        public void initiateCompensation(String transactionId, String reason) {
            this.compensationCount++;
            this.lastTransactionId = transactionId;
        }
    }

    static class StubVectorSearchService extends VectorSearchService {
        int addCount = 0;

        public StubVectorSearchService() {
            super(null);
        }

        @Override
        public void addSuspiciousTransaction(Transaction transaction) {
            this.addCount++;
        }
    }
}
