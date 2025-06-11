package org.example.monads.vavr;


public record ProcessingError(ErrorType type, String message) {

    public enum ErrorType {
        VALIDATION,
        AUTHENTICATION,
        AUTHORIZATION,
        BUSINESS_LOGIC
    }
}
