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

### Passo 3: Inicializar a Infraestrutura Base (Automático)
Preparamos scripts para automatizar a criação do arquivo `.env` e a subida dos containers no Docker.
No terminal, dentro da pasta do projeto, rode o script correspondente ao seu sistema:

**Se usar Mac/Linux:**
```bash
chmod +x setup.sh
./setup.sh
```

**Se usar Windows:**
```cmd
setup.bat
```-

## 🛠️ Orquestração Automática e Infraestrutura

Assim que você rodar o Docker Compose, ele subirá em segundo plano a infraestrutura base:
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
   Renomeie o arquivo `.env.example` para `.env` na raiz do projeto:
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
   *Dica: O projeto está configurado com o **Spring Boot DevTools**. Isso significa que enquanto você estiver alterando a classe `FraudDetectionAgent.java` durante o curso, basta salvar o arquivo (e disparar a compilação na IDE) que a aplicação reiniciará automaticamente em 1-2 segundos e refletirá as mudanças para testarmos!*

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