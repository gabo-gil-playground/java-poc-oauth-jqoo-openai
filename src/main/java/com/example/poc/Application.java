package com.example.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot application main class
 */
@SpringBootApplication
public class Application {

    /**
     * Starts {@link Application} as Spring boot application with web-server feature
     *
     * @param args {@link String[]}
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}