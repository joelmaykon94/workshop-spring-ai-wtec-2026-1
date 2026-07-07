package com.example.fraud.model;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


public record FraudAnalysis(
        boolean isFraud,
        String reason,
        double confidenceScore
) {
}
