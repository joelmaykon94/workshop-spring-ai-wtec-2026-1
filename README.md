# Workshop Spring AI - WTEC 2026

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/joelmaykon94/workshop-spring-ai-wtec-2026-1)

Repositório oficial do Workshop sobre Agentes de Inteligência Artificial para Detecção de Fraudes usando Spring AI.

## 🚀 Ambiente (Codespaces)

Este projeto foi otimizado para rodar diretamente no GitHub Codespaces. Ao iniciar o seu Codespace, o Docker Compose subirá **automaticamente** todos os serviços (PostgreSQL, Kafka, MinIO, Langfuse e Ollama).

1. **Como verificar se os containers subiram:**
   Abra o terminal do Codespaces e execute:
   ```bash
   docker ps
   ```
2. **Como acompanhar o download da IA (Ollama):**
   O modelo Llama 3.2 Vision tem mais de 3GB. Para ver o progresso do download automático, rode:
   ```bash
   docker logs -f fraud-ollama
   ```
   *(Pressione `Ctrl+C` para sair dos logs).*

## 📊 Dashboards Web (Aba Ports)

No VS Code do Codespaces, clique na aba **Ports** (Portas) na parte inferior. Certifique-se de alterar a *Visibility* (Visibilidade) para **Public** (Pública) clicando com o botão direito na porta desejada:
*   **Langfuse (Porta 3000):** Painel de Observabilidade dos Prompts da IA. Crie uma conta local e gere suas chaves em Settings > API Keys.
*   **MinIO (Porta 9001):** Nosso "S3" local. Login: `minioadmin / minioadmin`.

## 🧪 Testando a API (cURL)

Para testar a aplicação no terminal (após subir o Spring Boot), você pode usar o arquivo `test-payload.json` que preparamos para você. Ele já contém uma imagem Base64 (`recibo.png`) e um Áudio fictício (`voz.wav`).

**1. Semeando uma Transação (PGVector RAG):**
```bash
curl -X POST -H "Content-Type: application/json" -d @test-payload.json http://localhost:8080/api/fraud/seed
```

**2. Analisando uma Transação (IA + Vision):**
```bash
curl -X POST -H "Content-Type: application/json" -d @test-payload.json http://localhost:8080/api/fraud/analyze
```

---

## Estrutura do Workshop

Este repositório contém o código-fonte e o guia prático do workshop ministrado no **WTEC 2026.1**. O objetivo é demonstrar a transição de APIs CRUD tradicionais para **Agentes Cognitivos Multimodais** capazes de correlacionar metadados estruturados, fotos de cupons fiscais (visão) e áudios de autorização de voz (áudio) para identificar fraudes em transações financeiras em tempo real.

---

## 🚀 Como Iniciar a Prática (Passo a Passo)

Para garantir que você consiga codificar e salvar suas alterações sem problemas de permissão, siga a ordem abaixo:

### Passo 1: Fazer Fork do Repositório (Obrigatório)
1. Estando nesta página, clique no botão **Fork** no canto superior direito do GitHub.
2. Isso criará uma cópia idêntica deste projeto em sua conta pessoal do GitHub (ex: `github.com/seu-usuario/workshop-spring-ai-wtec-2026-1`).

### Passo 2: Inicializar o Ambiente em Nuvem
1. No seu repositório bifurcado (Fork), clique no botão verde **<> Code**.
2. Selecione a aba **Codespaces** e clique em **Create codespace on main** (ou clique no badge abaixo no seu fork):

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/joelmaykon94/workshop-spring-ai-wtec-2026-1)

---

## 🛠️ Orquestração Automática (Aguarde os Modelos)

Assim que o Codespace abrir no seu navegador, a configuração interna (`.devcontainer.json`) executará o Docker Compose em segundo plano automaticamente para subir:
- **Ollama** (Servidor de IA Local).
- **PostgreSQL** com a extensão `pgvector`.
- **MinIO** (Armazenamento compatível com S3).
- **Kafka** (Mensageria assíncrona).

### Como Acompanhar a Inicialização da IA:
Abra o terminal do Codespace e execute o comando abaixo para verificar o download em background dos modelos (`llama3.2-vision` e `nomic-embed-text`):
```bash
docker logs -f fraud-ollama
```
Aguarde até visualizar a mensagem **"Modelos prontos!"** no terminal antes de iniciar a execução da aplicação Java.

---

## 📝 O Desafio Hands-on
O repositório na branch `main` contém a estrutura de controllers e modelos pronta, mas as regras de busca vetorial e injeção multimodal estão vazias. Seu objetivo durante o minicurso é:
1. Implementar a busca semântica em **`VectorSearchService.java`**.
2. Configurar o **`ChatClient`** com Advisors de memória/RAG e Tools no **`FraudDetectionAgent.java`**.
3. Construir o fluxo multimodal de processamento de imagem de cupons e áudio no agente.

Se precisar de ajuda ou ficar travado, consulte o código completo na branch **`solucao`** ou faça checkout nos passos sequenciais do workshop (`git checkout passo-1`, etc.).