# Workshop Spring AI - WTEC 2026

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/joelmaykon94/workshop-spring-ai-wtec-2026-1)

Repositório oficial do Workshop sobre Agentes de Inteligência Artificial para Detecção de Fraudes usando Spring AI.

## 🚀 Como Iniciar a Prática (Passo a Passo)

### Passo 1: Fazer Fork do Repositório (Obrigatório)
1. Estando nesta página, clique no botão **Fork** no canto superior direito do GitHub.
2. Isso criará uma cópia idêntica deste projeto em sua conta pessoal do GitHub.

### Passo 2: Clonar na sua Máquina Local
1. No seu repositório bifurcado (Fork), clique no botão verde **<> Code** e copie a URL (HTTPS ou SSH).
2. Abra o terminal na sua máquina local e rode:
   ```bash
   git clone <SUA_URL_AQUI>
   cd workshop-spring-ai
   ```

### Passo 3: Inicializar o Ambiente via VS Code Dev Containers
A configuração do ambiente de desenvolvimento é automatizada via Container, isolando as dependências do sistema host:
1. Abra o **Visual Studio Code**.
2. Vá em `Extensions` e instale a extensão oficial da Microsoft chamada **"Dev Containers"**.
3. No VS Code, clique em `File > Open Folder...` e abra a pasta do projeto `workshop-spring-ai`.
4. Um aviso aparecerá no canto inferior direito: *"Folder contains a Dev Container configuration file"*. Clique no botão **Reopen in Container**.
   *(Caso a notificação não seja exibida, pressione `Ctrl+Shift+P` ou `Cmd+Shift+P`, pesquise e execute: **Dev Containers: Rebuild and Reopen in Container**).*

O VS Code provisionará o Java 21, as extensões necessárias para o ecossistema Spring e a infraestrutura de containers (banco de dados, filas e painéis) de forma automatizada e isolada.-

## 🛠️ Orquestração Automática e Infraestrutura

Assim que o container iniciar, o Docker Compose já terá subido em segundo plano a infraestrutura base:
- **PostgreSQL** com a extensão `pgvector` (Para RAG).
- **MinIO** (Armazenamento S3 para recibos).
- **Kafka** (Mensageria assíncrona para orquestração da Saga).
- **Langfuse** (Painel web para observabilidade da IA).

1. **Dashboards Web:**
   Como os containers estão rodando na sua máquina local, acesse direto pelo navegador:
   *   **Langfuse (http://localhost:3000):** Painel de Observabilidade. Crie uma conta e gere suas chaves em Settings > API Keys.
   *   **MinIO (http://localhost:9001):** Nosso S3 local. Login: `minioadmin / minioadmin`.

---

## ⚙️ Configurando o Projeto

1. **Variáveis de Ambiente:**
   Abra o terminal integrado do VS Code (que já estará rodando dentro do Linux do Dev Container) e renomeie o arquivo `.env.example` para `.env`:
   ```bash
   cp .env.example .env
   ```

2. **Chaves de API:**
   Abra o arquivo `.env` e preencha as chaves:
   - `OPENAI_API_KEY`: Crie uma chave grátis no [Google AI Studio](https://aistudio.google.com/app/apikey).
   - `LANGFUSE_PUBLIC_KEY` e `LANGFUSE_SECRET_KEY`: Pegue no painel do Langfuse criado no passo anterior.

3. **Iniciando a Aplicação (Com Live Reload 🔄):**
   Execute a aplicação Spring Boot pelo terminal ou sua IDE favorita (IntelliJ, VS Code):
   ```bash
   ./mvnw spring-boot:run
   ```
   *Nota: O projeto utiliza o **Spring Boot DevTools**. Ao alterar o código da classe `FraudDetectionAgent.java`, basta salvar o arquivo (e acionar a compilação na IDE) para que o contexto da aplicação seja recarregado automaticamente, refletindo as alterações instantaneamente.*

---

## 📝 O Desafio Hands-on

A branch `main` contém a estrutura do projeto (`Controllers`, `Services`, `Models`) pronta, mas o Cérebro do Agente está vazio!
Seu objetivo durante o minicurso é abrir a classe **`FraudDetectionAgent.java`** e implementar a inteligência dela.

👉 **[CLIQUE AQUI PARA ABRIR O GUIA PASSO A PASSO](PASSO_A_PASSO.md)** 👈

Se ficar travado, você também pode consultar o código completo na branch **`solucao`**.

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