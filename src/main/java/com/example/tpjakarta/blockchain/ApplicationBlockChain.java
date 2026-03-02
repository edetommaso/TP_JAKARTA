package com.example.tpjakarta.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationBlockChain {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBlockChain.class, args);
        System.out.println("\n--- Blockchain API démarré sur http://localhost:8080 ---");
    }
}
