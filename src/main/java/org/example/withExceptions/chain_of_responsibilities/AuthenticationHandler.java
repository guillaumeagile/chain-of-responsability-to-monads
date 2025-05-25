package org.example.withExceptions.chain_of_responsibilities;

import org.example.chain_of_responsibilities.Request;
import org.example.monads.ProcessingError;

public  class AuthenticationHandler implements RequestHandler {
    private RequestHandler nextHandler;

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String handleRequest(Request request) throws ProcessingException {
        if (request.originator().isEmpty())
          throw new ProcessingException(ProcessingError.ErrorType.AUTHENTICATION, "Originator is empty");
        return "originator: Ok" + nextHandler.handleRequest(request);
    }
}
