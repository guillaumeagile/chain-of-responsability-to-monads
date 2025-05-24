package org.example.monads;


public record ProcessingError(ErrorType type, String message) {
    
    @Override
    public String toString() {
        return type + ": " + message;
    }

    public enum ErrorType {
        VALIDATION,
        AUTHENTICATION,
        AUTHORIZATION,
        BUSINESS_LOGIC
    }
}
