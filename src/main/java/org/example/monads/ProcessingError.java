package org.example.monads;


public record ProcessingError(ErrorType type, String message) {

    public enum ErrorType {
        VALIDATION,
        AUTHENTICATION,
        AUTHORIZATION,
        BUSINESS_LOGIC
    }
}
