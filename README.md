# Workshop Spring AI - WTEC 2026 (Gabarito Oficial 🟢)

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/joelmaykon94/workshop-spring-ai-wtec-2026-1)

> **⚠️ AVISO:** Você está na branch **`solucao`**. Esta branch contém a implementação completa, refatorada e arquiteturalmente finalizada do projeto. Para o template inicial do minicurso, mude para a branch `main`.

Repositório oficial do Workshop sobre Agentes de Inteligência Artificial para Detecção de Fraudes usando Spring AI.

## 🏛️ Arquitetura da Solução

Neste projeto nós construímos uma orquestração reativa onde um Agente de Inteligência Artificial atua como Auditor de Fraudes para transações financeiras.

*   **Padrão MVC + Services:** Controladores limpos e lógicas de orquestração encapsuladas na camada de `Service` (`FraudService.java`).
*   **Multimodalidade (Spring AI + Gemini 1.5 Flash):** Análise combinada de dados estruturados em JSON e fotos de cupons fiscais baixadas dinamicamente via bucket S3.
*   **Vector Database (PGVector):** Utilização do padrão **RAG** (Retrieval-Augmented Generation) para buscar o histórico de fraudes anteriores do usuário e prover contexto extra ao LLM.
*   **SAGA Pattern (Kafka):** Quando o Agente de IA detecta que uma transação é fraude, o sistema dispara um evento em um tópico Kafka (`fraud-compensations`) instruindo serviços parceiros (ex: Ledger Bancário) a realizar o Rollback (Estorno) do valor imediatamente.
*   **Observabilidade (Langfuse):** Traçabilidade de todo o fluxo (Traces), latência por etapa (Spans) e log completo de tokens consumidos pelas gerações LLM (Generations) em tempo real.

---

## 🚀 Como Executar o Gabarito (Codespaces)

O ambiente no GitHub Codespaces subirá **automaticamente** os serviços base no Docker Compose (PostgreSQL, Kafka, MinIO e Langfuse).

1. **Dashboards Web (Aba Ports):**
   *   **Langfuse (Porta 3000):** Painel de Observabilidade. Crie uma conta e gere suas chaves em Settings > API Keys.
   *   **MinIO (Porta 9001):** Nosso S3 local. Login: `minioadmin / minioadmin`.

2. **Configurando as Chaves (.env):**
   Renomeie `.env.example` para `.env` e preencha:
   ```bash
   cp .env.example .env
   ```
   *As chaves necessárias são `OPENAI_API_KEY` (Gemini via Google AI Studio) e as chaves do Langfuse.*

3. **Iniciando a Aplicação Spring Boot:**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Testando a Orquestração Completa

Com o sistema rodando, você pode iniciar o nosso "Canhão de Transações". Preparamos um Endpoint que simula a chegada em tempo real de **10 transações bancárias variadas** (algumas legais, outras super fraudulentas).

Abra outro terminal e execute:
```bash
curl -X POST http://localhost:8080/api/fraud/simulate
```

**O que vai acontecer debaixo dos panos?**
1. O controlador de simulação gerará dados na mosca e fará o download de recibos customizados via API do Placehold, subindo eles no **MinIO**.
2. Ele vai fazer o **Seed (Alimentação do RAG)** com as transações 1 e 2 no PGVector.
3. Nas transações de 3 a 10, ele acionará o `FraudDetectionAgent`.
4. O Agente executará: RAG -> Recuperação Multimodal -> Engenharia de Prompt -> Chamada ao Gemini -> Registro no Langfuse.
5. Se for Fraude, ele aciona o Apache Kafka para emitir o estorno da SAGA.
6. Há um *Rate Limiting Control* de 15 segundos entre requisições implementado na simulação para respeitar com folga a cota do plano gratuito do Gemini (15 RPM).

*Volte ao painel do **Langfuse** para ver a mágica acontecendo em lindos gráficos!*