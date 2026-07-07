package com.example.fraud;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudDetectionAgent agent;
    private final VectorSearchService vectorSearchService;

    public FraudController(FraudDetectionAgent agent, VectorSearchService vectorSearchService) {
        this.agent = agent;
        this.vectorSearchService = vectorSearchService;
    }

    @GetMapping("/index")
    public String index() {
        return "Fraud Detection API is running!";
    }

    @PostMapping("/analyze")
    public FraudAnalysis analyzeTransaction(@RequestBody Transaction transaction) {
        return agent.analyze(transaction);
    }
}
