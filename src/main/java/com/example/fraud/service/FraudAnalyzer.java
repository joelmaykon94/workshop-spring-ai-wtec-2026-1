package com.example.fraud.service;
import com.example.fraud.model.FraudAnalysis;
import com.example.fraud.model.Transaction;
public interface FraudAnalyzer {
    FraudAnalysis analyze(Transaction transaction);
}
