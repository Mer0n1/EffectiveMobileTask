package com.example.effectivemobiletask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EffectiveMobileTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffectiveMobileTaskApplication.class, args);
    }
}
