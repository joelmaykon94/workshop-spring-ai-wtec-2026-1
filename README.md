# Workshop Spring AI - WTEC 2026

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/joelmaykon94/workshop-spring-ai-wtec-2026-1)

Repositório oficial do Workshop sobre Agentes de Inteligência Artificial para Detecção de Fraudes usando Spring AI.

## 🚀 Como Iniciar a Prática (Passo a Passo)

### Passo 1: Fazer Fork do Repositório (Obrigatório)
1. Estando nesta página, clique no botão **Fork** no canto superior direito do GitHub.
2. Isso criará uma cópia idêntica deste projeto em sua conta pessoal do GitHub.

### Passo 2: Inicializar o Ambiente em Nuvem
1. No seu repositório bifurcado, clique no botão verde **<> Code**.
2. Selecione a aba **Codespaces** e clique em **Create codespace on main**.

---

## 🛠️ Orquestração Automática e Infraestrutura

Assim que o Codespace abrir, o Docker Compose subirá em segundo plano a infraestrutura base:
- **PostgreSQL** com a extensão `pgvector` (Para RAG).
- **MinIO** (Armazenamento S3 para recibos).
- **Kafka** (Mensageria assíncrona para orquestração da Saga).
- **Langfuse** (Painel web para observabilidade da IA).

1. **Como verificar se os containers subiram:**
   Abra o terminal do Codespaces e execute:
   ```bash
   docker ps
   ```

2. **Dashboards Web (Aba Ports):**
   No VS Code, clique na aba **Ports** na parte inferior e altere a visibilidade para **Public**:
   *   **Langfuse (Porta 3000):** Painel de Observabilidade. Crie uma conta e gere suas chaves em Settings > API Keys.
   *   **MinIO (Porta 9001):** Nosso S3 local. Login: `minioadmin / minioadmin`.

---

## ⚙️ Configurando o Projeto

1. **Variáveis de Ambiente:**
   Renomeie o arquivo `.env.example` para `.env` na raiz do projeto:
   ```bash
   cp .env.example .env
   ```

2. **Chaves de API:**
   Abra o arquivo `.env` e preencha as chaves:
   - `OPENAI_API_KEY`: Crie uma chave grátis no [Google AI Studio](https://aistudio.google.com/app/apikey).
   - `LANGFUSE_PUBLIC_KEY` e `LANGFUSE_SECRET_KEY`: Pegue no painel do Langfuse criado no passo anterior.

3. **Iniciando a Aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 📝 O Desafio Hands-on

A branch `main` contém a estrutura do projeto (`Controllers`, `Services`, `Models`) pronta, mas o Cérebro do Agente está vazio!
Seu objetivo durante o minicurso é abrir a classe **`FraudDetectionAgent.java`** e implementar os `TODO`s:

1. **RAG:** Implementar a busca semântica em VectorSearchService.
2. **Observabilidade:** Iniciar o trace da requisição no Langfuse.
3. **Visão Computacional:** Preparar a imagem recebida do bucket S3.
4. **Engenharia de Prompt:** Montar as intruções para o Agente.
5. **Integração LLM:** Chamar a API do Gemini via Spring AI `ChatClient`.

Se ficar travado, consulte o código completo na branch **`solucao`**.

---

## 🧪 Simulando o Tráfego em Tempo Real

Após implementar seu Agente de IA, você pode testá-lo enviando rajadas de transações! Preparamos um Endpoint que cria 10 transações falsas, gera e faz upload automático de imagens de recibos, e envia para a IA julgar uma a uma.

Com a aplicação rodando, abra outro terminal e execute:
```bash
curl -X POST http://localhost:8080/api/fraud/simulate
```
*Acompanhe no console do Spring Boot e no dashboard do Langfuse as análises sendo realizadas!*

---

## 📚 Referências e Documentações

*   **Spring AI:** [Visão Geral](https://spring.io/projects/spring-ai#overview) | [API do ChatClient](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
*   **Google AI Studio:** [Gemini API](https://aistudio.google.com/)
*   **Observabilidade:** [Langfuse Docs](https://langfuse.com/docs)