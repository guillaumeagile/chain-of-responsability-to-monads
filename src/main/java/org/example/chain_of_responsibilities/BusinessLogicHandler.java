package org.example.chain_of_responsibilities;

public class BusinessLogicHandler implements RequestHandler {
    @Override
    public void setNext(RequestHandler nextHandler) {

    }

    @Override
    public String handleRequest(Request request) {
        return "";
    }
}
