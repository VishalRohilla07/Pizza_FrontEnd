package com.pizza.ordering.exception;

/**
 * Exception thrown for bad requests (validation failures, business logic
 * violations)
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
