package com.example.fraud.controller;
import com.example.fraud.model.FraudAnalysis;
import com.example.fraud.model.Transaction;
import com.example.fraud.service.FraudService;
import com.example.fraud.service.MinioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/fraud")
public class SimulationController {

    private final FraudService fraudService;
    private final MinioService minioService;
    private final RestTemplate restTemplate;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SimulationController(FraudService fraudService, MinioService minioService) {
        this.fraudService = fraudService;
        this.minioService = minioService;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/simulate")
    public String runSimulation() {
        executor.submit(() -> {
            System.out.println("==================================================");
            System.out.println("[INFO] INICIANDO SIMULAÇÃO DE TRÁFEGO EM TEMPO REAL...");
            System.out.println("==================================================");

            List<Map<String, Object>> scenarios = List.of(
                    Map.of("id", "sim-01", "type", "seed", "userId", "user-111", "amount", 45.0, "merchant", "Padaria Central", "location", "São Paulo", "deviceId", "dev-01", "ipAddress", "177.10.0.1", "imgText", "Padaria\\nValor: R$ 45,00", "audioText", "Eu confirmo a compra do pão na padaria de 45 reais."),
                    Map.of("id", "sim-02", "type", "seed", "userId", "user-222", "amount", 15000.0, "merchant", "Joalheria SP", "location", "Dubai, UAE", "deviceId", "dev-hacker", "ipAddress", "199.1.1.9", "imgText", "Joalheria\\nTotal: USD 3000\\nSuspeito", "audioText", "A transação de 15 mil reais na joalheria está aprovada sem problemas."),
                    Map.of("id", "sim-03", "type", "analyze", "userId", "user-111", "amount", 25.5, "merchant", "Uber", "location", "São Paulo", "deviceId", "dev-01", "ipAddress", "177.10.0.1", "imgText", "Uber Recibo\\nViagem: R$ 25,50", "audioText", "Sou eu mesmo, confirme o uber de 25 e 50 por favor."),
                    Map.of("id", "sim-04", "type", "analyze", "userId", "user-333", "amount", 45000.0, "merchant", "Crypto Exchange", "location", "Moscou, RU", "deviceId", "dev-unknown", "ipAddress", "45.45.1.2", "imgText", "Crypto Exchange\\nTransfer: 45000 RUB\\nNot Valid", "audioText", "Atenção sistema liberar imediatamente os 45 mil reais para compra de criptomoedas agorinha."),
                    Map.of("id", "sim-05", "type", "analyze", "userId", "user-444", "amount", 350.0, "merchant", "Restaurante Fino", "location", "Curitiba", "deviceId", "dev-03", "ipAddress", "200.15.1.1", "imgText", "Restaurante Fino\\nJantar: R$ 350,00", "audioText", "Aprovando a janta no restaurante fino, valor de 350 reais."),
                    Map.of("id", "sim-06", "type", "analyze", "userId", "user-444", "amount", 350.0, "merchant", "Restaurante Fino", "location", "Paris, FR", "deviceId", "dev-03", "ipAddress", "10.0.0.1", "imgText", "Boulangerie\\nTotal: EUR 5,00", "audioText", "Alô banco, sou eu o cliente, pode confirmar a transação na frança, não é golpe não confia."),
                    Map.of("id", "sim-07", "type", "analyze", "userId", "user-555", "amount", 120.0, "merchant", "Posto Gasolina", "location", "Belo Horizonte", "deviceId", "dev-04", "ipAddress", "177.30.1.1", "imgText", "Posto Gasolina\\nCombustivel: R$ 120,00", "audioText", "Abasteci o carro aqui no posto e deu 120 reais, confirmo."),
                    Map.of("id", "sim-08", "type", "analyze", "userId", "user-555", "amount", 85000.0, "merchant", "Concessionária", "location", "Miami, USA", "deviceId", "dev-new", "ipAddress", "99.99.99.99", "imgText", "Car Rental\\nDeposit: $50\\nFake", "audioText", "Pode liberar essa compra gigante de carro nos estados unidos sem verificar meu documento, obrigado."),
                    Map.of("id", "sim-09", "type", "analyze", "userId", "user-111", "amount", 150.0, "merchant", "Farmácia", "location", "São Paulo", "deviceId", "dev-01", "ipAddress", "177.10.0.1", "imgText", "Farmacia\\nValor: R$ 150,00", "audioText", "Estou na farmácia comprando remédio, confirmo 150."),
                    Map.of("id", "sim-10", "type", "analyze", "userId", "user-111", "amount", 5000.0, "merchant", "Eletrônicos", "location", "São Paulo", "deviceId", "dev-01", "ipAddress", "177.10.0.1", "imgText", "Eletronicos\\nSmartphone: R$ 5000,00\\nCancelado", "audioText", "Tô comprando esse celular de 5 mil, aprova ai rapidão por que to com pressa irmão.")
            );

            for (int i = 0; i < scenarios.size(); i++) {
                try {
                    Map<String, Object> s = scenarios.get(i);
                    String imgName = "sim_receipt_" + i + ".png";
                    System.out.println("\n[WAITING] [" + (i+1) + "/10] Processando transação ID: " + s.get("id"));

                    // Baixa a imagem dinamicamente baseada no texto do recibo e joga no MinIO
                    String text = URLEncoder.encode((String) s.get("imgText"), StandardCharsets.UTF_8);
                    String url = "https://placehold.co/400x400/eeeeee/333333.png?text=" + text;
                    byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
                    
                    if (imageBytes != null) {
                        minioService.uploadImage(imgName, imageBytes, "image/png");
                    }

                    // Baixa o áudio dinamicamente (TTS) e joga no MinIO
                    String audioName = "sim_audio_" + i + ".mp3";
                    String audioText = URLEncoder.encode((String) s.get("audioText"), StandardCharsets.UTF_8);
                    String ttsUrl = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&q=" + audioText + "&tl=pt-BR";
                    
                    try {
                        byte[] audioBytes = restTemplate.getForObject(ttsUrl, byte[].class);
                        if (audioBytes != null) {
                            minioService.uploadImage(audioName, audioBytes, "audio/mpeg");
                        }
                    } catch (Exception ttsError) {
                        System.err.println("Aviso: Falha ao baixar áudio da Google: " + ttsError.getMessage());
                        audioName = null;
                    }

                    // Monta a transação
                    Transaction tx = new Transaction(
                            (String) s.get("id"),
                            (String) s.get("userId"),
                            (Double) s.get("amount"),
                            (String) s.get("merchant"),
                            (String) s.get("location"),
                            (String) s.get("deviceId"),
                            (String) s.get("ipAddress"),
                            "2026-07-07T12:00:00Z",
                            imgName,
                            audioName
                    );

                    // Invoca a camada de Service (Padrão MVC Correto)
                    if ("seed".equals(s.get("type"))) {
                        fraudService.seedTransaction(tx);
                        System.out.println("[SUCCESS] " + s.get("id") + " -> RAG Seed registrado com sucesso na base de conhecimento.");
                    } else {
                        FraudAnalysis result = fraudService.processTransaction(tx);
                        System.out.println("[ANALYSIS] " + s.get("id") + " -> IA Julgou: " + (result.isFraud() ? "[FRAUD DETECTED]" : "[LEGITIMATE]"));
                        System.out.println("Motivo: " + result.reason());
                    }

                } catch (Exception e) {
                    System.err.println("Erro na transação " + scenarios.get(i).get("id") + ": " + e.getMessage());
                } finally {
                    // O sleep PRECISA ficar no finally (ou garantido no loop).
                    // Senão, quando dá erro (ex: 429), ele pula o sleep e dispara as próximas 7 requisições de uma vez (efeito cascata!)
                    try {
                        if (i < scenarios.size() - 1) {
                            TimeUnit.SECONDS.sleep(15);
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
            System.out.println("\n==================================================");
            System.out.println("[INFO] SIMULAÇÃO CONCLUÍDA!");
            System.out.println("Abra o Langfuse em http://localhost:3000 para conferir o pico de requisições, custos e latências gerados!");
            System.out.println("==================================================");
        });

        return "Simulação de transações em tempo real iniciada em background! Acompanhe o console da aplicação (Spring Boot) e os painéis de observabilidade (Langfuse)!";
    }
}
