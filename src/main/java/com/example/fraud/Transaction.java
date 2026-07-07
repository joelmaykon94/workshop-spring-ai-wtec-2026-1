package com.example.fraud;

public record Transaction(
        String id,
        String userId,
        double amount,
        String merchant,
        String location,
        String deviceId,
        String ipAddress,
        String transactionTime
) {
}
