# Etapa 4: Resiliência e Compensação (Saga Coreográfica)

Este é o roteiro prático para a **Fase 4 (Resiliência)**.
O objetivo aqui é garantir que nossa aplicação não apenas alerte sobre fraudes, mas também reverta automaticamente o prejuízo (estorno) em um microsserviço hipotético de contas (Ledger), utilizando mensageria assíncrona (Apache Kafka).

## O que vamos fazer?
1. Configurar as propriedades do Kafka.
2. Criar a classe `TransactionSagaService` que atua tanto como Produtor (emitindo o alerta) quanto Consumidor (simulando a reversão no serviço de Ledger).
3. Conectar a Saga ao `FraudController`!

---

## 💻 Mão na massa (Hands-on)

### Passo 1: Dependência e Configuração
Certifique-se de que o **Spring Kafka** está no `pom.xml`. Se não estiver, abra o `pom.xml` e adicione junto às outras dependências:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

Em seguida, adicione no `application.properties`:
```properties
# Kafka Configuration
spring.kafka.bootstrap-servers=kafka:29092
spring.kafka.consumer.group-id=fraud-group
spring.kafka.consumer.auto-offset-reset=earliest
```

### Passo 2: O Serviço de Saga
Crie ou abra a classe `src/main/java/com/example/fraud/TransactionSagaService.java` e cole o código abaixo:

```java
package com.example.fraud;

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
        System.out.println("🚨 SAGA: Iniciando compensação (estorno) para a transação " + transactionId);
        String payload = String.format("{\\"transactionId\\": \\"%s\\", \\"action\\": \\"ROLLBACK\\", \\"reason\\": \\"%s\\"}", transactionId, reason);
        
        // Envia para o Tópico do Kafka
        kafkaTemplate.send("fraud-compensations", transactionId, payload);
    }

    /**
     * Passo 2 da Saga (Simulação): Ouve o próprio tópico para simular o 
     * Ledger-Service recebendo o comando e estornando o dinheiro.
     */
    @KafkaListener(topics = "fraud-compensations", groupId = "fraud-group")
    public void consumeCompensationEvent(String message) {
        System.out.println("==================================================");
        System.out.println("💰 LEDGER SERVICE (Saga Consumer) RECEBEU O EVENTO:");
        System.out.println("Payload: " + message);
        System.out.println("Ação: Saldo estornado e transação cancelada com sucesso!");
        System.out.println("==================================================");
    }
}
```

### Passo 3: Integre a Saga no Controller
Abra a classe `FraudController.java` e atualize-a para injetar o `TransactionSagaService` e engatilhar a compensação:

```java
package com.example.fraud;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudDetectionAgent agent;
    private final VectorSearchService vectorSearchService;
    private final TransactionSagaService sagaService; // <-- Novo serviço

    public FraudController(FraudDetectionAgent agent, VectorSearchService vectorSearchService, TransactionSagaService sagaService) {
        this.agent = agent;
        this.vectorSearchService = vectorSearchService;
        this.sagaService = sagaService; // <-- Inicialização
    }

    @PostMapping("/analyze")
    public FraudAnalysis analyzeTransaction(@RequestBody Transaction transaction) {
        FraudAnalysis analysis = agent.analyze(transaction);
        
        // Se for fraude, inicia a Saga de compensação (estorno via Kafka)
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
```

---

### 🧠 Explicação Didática para a Turma (Dicas de Palestra):
1. **Padrão Saga**: Pergunte aos alunos: *"O que acontece quando uma fraude é detectada e a compra já foi efetivada no cartão? Como reverter em um sistema distribuído?"* Isso puxa o assunto de transações distribuídas, explicando que não podemos usar um simples `BEGIN` e `COMMIT` de banco de dados SQL. Precisamos de Compensação.
2. **Desacoplamento com Kafka**: Explique que o microsserviço de Fraudes não precisa saber em qual banco o saldo fica. Ele apenas "grita no megafone" (Kafka) que a transação X foi fraudulenta. Quem souber lidar com isso (Ledger Service) que faça o rollback!
3. **Ponto de Reflexão Prático**: Esse é o "Cereja do Bolo" arquitetural. A aplicação passou de uma "API reativa de IA" para um sistema financeiro assíncrono real.
