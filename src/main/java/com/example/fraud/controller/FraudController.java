package com.example.fraud.controller;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudService fraudService;

    public FraudController(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @GetMapping("/index")
    public String index() {
        return "Fraud Detection API is running!";
    }

    @PostMapping("/analyze")
    public FraudAnalysis analyzeTransaction(@RequestBody Transaction transaction) {
        return fraudService.processTransaction(transaction);
    }

    @PostMapping("/seed")
    public String seedSuspiciousTransaction(@RequestBody Transaction transaction) {
        return fraudService.seedTransaction(transaction);
    }
}
