package com.example.fraud.service;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;


public interface FraudAnalyzer {
    FraudAnalysis analyze(Transaction transaction);
}
