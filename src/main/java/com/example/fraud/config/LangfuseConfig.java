package com.example.fraud.config;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


import com.langfuse.client.LangfuseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangfuseConfig {

    @Value("${langfuse.base-url}")
    private String langfuseUrl;
    
    @Value("${langfuse.secret-key}")
    private String secretKey;
    
    @Value("${langfuse.public-key}")
    private String publicKey;

    @Bean
    public LangfuseClient langfuseClient() {
        return LangfuseClient.builder()
                .url(langfuseUrl)
                .credentials(publicKey, secretKey)
                .build();
    }
}
