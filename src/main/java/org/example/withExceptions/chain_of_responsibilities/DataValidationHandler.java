package org.example.withExceptions.chain_of_responsibilities;

import org.example.chain_of_responsibilities.Request;
import org.example.monads.vavr.ProcessingError;

public class DataValidationHandler implements RequestHandler {
    private RequestHandler nextHandler;

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String handleRequest(Request request) throws ProcessingException {

        if  (request.data().isEmpty())
            throw new  ProcessingException( ProcessingError.ErrorType.VALIDATION, "data is empty"  );
        return "";
    }
}
