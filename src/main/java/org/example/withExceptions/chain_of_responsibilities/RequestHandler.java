package org.example.withExceptions.chain_of_responsibilities;

import org.example.chain_of_responsibilities.Request;

public interface RequestHandler {

    void setNext(RequestHandler nextHandler);

    String handleRequest(Request request) throws ProcessingException;
}
