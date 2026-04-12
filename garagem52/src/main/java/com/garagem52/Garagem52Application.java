package com.garagem52;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Garagem52Application {
    public static void main(String[] args) {
        SpringApplication.run(Garagem52Application.class, args);
    }
}
