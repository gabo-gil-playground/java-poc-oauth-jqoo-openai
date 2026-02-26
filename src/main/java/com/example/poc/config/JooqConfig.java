package com.example.poc.config;

import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jOOQ configuration for the P.o.C. application
 */
@Configuration
public class JooqConfig {

    @Bean
    public Settings jooqSettings() {
        return new Settings().withRenderSchema(false); // disable "public" or any schema prefix
    }
}