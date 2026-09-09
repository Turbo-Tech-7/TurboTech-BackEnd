package com.garagem52;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients(basePackages = "com.garagem52.adapter.output.client")
public class Garagem52Application {
    public static void main(String[] args) {
        SpringApplication.run(Garagem52Application.class, args);
    }
}
