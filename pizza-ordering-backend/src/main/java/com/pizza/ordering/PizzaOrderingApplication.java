package com.pizza.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application
 */
@SpringBootApplication
@EnableJpaAuditing
public class PizzaOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaOrderingApplication.class, args);
    }
}
