package com.example.fraud.model;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


public record Transaction(
        String id,
        String userId,
        double amount,
        String merchant,
        String location,
        String deviceId,
        String ipAddress,
        String transactionTime,
        String receiptImage,
        String voiceAuth
) {
}
