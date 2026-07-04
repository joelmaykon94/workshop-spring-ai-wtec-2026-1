# Spring AI: Construindo Agentes Cognitivos Multimodais para Detecção de Fraudes 🚀

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

---

## 🔗 Guias de Planejamento Sequencial
Toda a teoria, setup e lógica detalhada do minicurso estão divididos nos seguintes arquivos da pasta `markdown/`:
1. [1. Apresentação e Ementa](markdown/1_planning_apresentacao.md)
2. [2. Arquitetura de Referência & Cronograma](markdown/2_planning_visao_geral.md)
3. [3. Fundamentação Teórica de IA](markdown/3_planning_parte1_conceitos.md)
4. [4. Guia de Setup & Docker](markdown/4_planning_parte2_setup.md)
5. [5. Guia de Codificação Hands-on](markdown/5_planning_parte3_hands_on.md)
6. [6. Validação, Testes & Observabilidade](markdown/6_planning_parte4_testes_obs.md)
7. [7. Checklist de Slides & Repositório](markdown/checklist_preparacao_material.md)
8. [8. Respostas para Organização do Evento](markdown/respostas_organizacao_evento.md)