package com.example.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Standard startup; properties in application.yml enable virtual threads and lazy-init
        SpringApplication.run(Application.class, args);
    }
}
