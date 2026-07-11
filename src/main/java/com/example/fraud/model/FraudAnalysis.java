package com.example.fraud.model;



public record FraudAnalysis(
        boolean isFraud,
        String reason,
        double confidenceScore
) {
}
