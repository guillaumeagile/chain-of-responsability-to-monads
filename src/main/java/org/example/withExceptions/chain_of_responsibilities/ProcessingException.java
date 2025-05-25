package org.example.withExceptions.chain_of_responsibilities;

import org.example.monads.ProcessingError;

public class ProcessingException extends Throwable {

    private final ProcessingError.ErrorType errorType;

    public ProcessingError.ErrorType getErrorType() {
        return errorType;
    }

    public ProcessingException(ProcessingError.ErrorType errorType, String errorMessage) {
     this.errorType = errorType;
    }
}
