package com.rydio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RydioBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RydioBackendApplication.class, args);
    }
}