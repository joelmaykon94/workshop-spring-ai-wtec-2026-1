package com.example.fraud;
import com.example.fraud.model.*;
import com.example.fraud.service.*;
import com.example.fraud.config.*;
import com.example.fraud.controller.*;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FraudDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionApplication.class, args);
	}

}
