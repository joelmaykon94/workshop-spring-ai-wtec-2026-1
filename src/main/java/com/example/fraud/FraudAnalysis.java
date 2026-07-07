package com.example.fraud;

public record FraudAnalysis(
        boolean isFraud,
        String reason,
        double confidenceScore
) {
}
