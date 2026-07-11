package com.example.fraud.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionSagaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionSagaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Passo 1 da Saga: Inicia o processo emitindo evento de fraude detectada
     * para que o serviço de Ledger (Conta) desfaça a transação.
     */
    public void initiateCompensation(String transactionId, String reason) {
        System.out.println("[SAGA] Iniciando compensação (estorno) para a transação " + transactionId);
        String payload = String.format("{\"transactionId\": \"%s\", \"action\": \"ROLLBACK\", \"reason\": \"%s\"}", transactionId, reason);
        
        kafkaTemplate.send("fraud-compensations", transactionId, payload);
    }

    /**
     * Passo 2 da Saga (Simulação): Ouve o próprio tópico para simular o 
     * Ledger-Service recebendo o comando e estornando o dinheiro.
     */
    @KafkaListener(topics = "fraud-compensations", groupId = "fraud-group")
    public void consumeCompensationEvent(String message) {
        System.out.println("==================================================");
        System.out.println("[LEDGER SERVICE] (Saga Consumer) RECEBEU O EVENTO:");
        System.out.println("Payload: " + message);
        System.out.println("Ação: Saldo estornado e transação cancelada com sucesso!");
        System.out.println("==================================================");
    }
}
