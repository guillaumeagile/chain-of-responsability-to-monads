package org.example.chain_of_responsibilities;

public interface RequestHandler {
    void setNext(RequestHandler nextHandler);

    String handleRequest(Request request);
}
