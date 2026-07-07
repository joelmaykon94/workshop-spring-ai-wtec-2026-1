package com.example.fraud.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.http.HttpStatusCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenAiGeminiUsageInterceptor implements ClientHttpRequestInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        
        // Se a resposta for sucesso, nós interceptamos o JSON para garantir o bloco de uso
        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = response.getBody().readAllBytes();
            String responseStr = new String(responseBody, StandardCharsets.UTF_8);
            
            try {
                JsonNode rootNode = mapper.readTree(responseStr);
                // Se a API do Gemini (compatibilidade OpenAI) não retornou o bloco "usage", a gente injeta!
                if (rootNode.isObject() && !rootNode.has("usage")) {
                    ObjectNode usageNode = mapper.createObjectNode();
                    usageNode.put("prompt_tokens", 1500);
                    usageNode.put("completion_tokens", 250);
                    usageNode.put("total_tokens", 1750);
                    ((ObjectNode) rootNode).set("usage", usageNode);
                    responseStr = mapper.writeValueAsString(rootNode);
                }
            } catch (Exception e) {
                // Falhou o parse do JSON, segue a vida sem mexer
            }
            
            return new CustomClientHttpResponse(response, responseStr.getBytes(StandardCharsets.UTF_8));
        }
        return response;
    }

    @Bean
    public RestClientCustomizer restClientCustomizer(OpenAiGeminiUsageInterceptor interceptor) {
        return restClient -> restClient.requestInterceptor(interceptor);
    }

    private static class CustomClientHttpResponse implements ClientHttpResponse {
        private final ClientHttpResponse delegate;
        private final byte[] body;

        public CustomClientHttpResponse(ClientHttpResponse delegate, byte[] body) {
            this.delegate = delegate;
            this.body = body;
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return delegate.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return delegate.getStatusText();
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }
    }
}
