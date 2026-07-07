# Testando a Aplicação no GitHub Codespaces

Como o nosso ambiente (Spring Boot, Langfuse, MinIO, Postgres, Ollama e Kafka) está rodando inteiramente na nuvem dentro do **GitHub Codespaces**, precisamos usar o recurso de **Encaminhamento de Portas (Port Forwarding)** para conseguir acessar os painéis web e a nossa API através do navegador local.

Siga os passos abaixo para acessar e testar tudo o que construímos:

## 1. Iniciando a Aplicação Spring Boot
Antes de acessar as portas, você precisa garantir que a API Spring Boot (Fase 4) está rodando.
No terminal do seu Codespace, execute:
```bash
./mvnw spring-boot:run
```
Aguarde até visualizar a mensagem de que o Tomcat iniciou na porta `8080`. (Os outros serviços como Langfuse e MinIO já foram iniciados pelo Docker Compose).

---

## 2. Acesse a aba "Portas" (Ports)
Na barra de abas inferior ou lateral do VS Code (ao lado de onde fica o *Terminal*), clique na aba **Ports** (ou Portas).
O Codespaces detecta automaticamente as portas que estão abertas no Docker. Você verá uma lista com várias portas, como:
- **8080** (API Spring Boot)
- **3000** (Painel Web do Langfuse)
- **9001** (Painel Web do MinIO - Console)

---

## 3. Abra no Navegador (Visibilidade)
Na linha correspondente à porta que você quer acessar (ex: a `3000` do Langfuse):
1. Você verá uma coluna chamada **Port Forwarding**.
2. Clique no ícone de globo (**Open in Browser** ou *Abrir no Navegador*).

> **💡 Dica Importante: Visibilidade da Porta**
> Por padrão, a porta encaminhada é **Privada**. Isso significa que a API e o painel só abrem se você estiver logado na sua conta do GitHub no navegador.
> 
> Se você precisar testar a API no Postman, compartilhar o link com um colega, ou se o Langfuse estiver bloqueando a tela de login, **mude a visibilidade para Pública**:
> 1. Na aba Ports, clique com o botão direito sobre a porta (ex: `8080` ou `3000`).
> 2. Selecione **Port Visibility** (Visibilidade da porta).
> 3. Altere para **Public** (Pública).

Agora você pode copiar a URL gerada (ex: `https://<seu-codespace>-3000.app.github.dev`) e usar à vontade!

---

## 4. O que testar?

* **Langfuse (Porta 3000):** Crie uma conta de administrador (qualquer email/senha), gere as chaves em *Settings > API Keys* e cole-as no `application.properties`.
* **MinIO (Porta 9001):** Faça login com `minioadmin` / `minioadmin`. Crie um bucket chamado `fraud-docs` e suba um RG falso ou comprovante de PIX para testar a IA multimodal.
* **API Spring (Porta 8080):** Envie requisições HTTP POST para a URL pública gerada no Codespaces para a porta 8080 (ex: `https://...-8080.app.github.dev/api/fraud/analyze`) para ver a Saga (Kafka) e o Agente de IA agindo!
