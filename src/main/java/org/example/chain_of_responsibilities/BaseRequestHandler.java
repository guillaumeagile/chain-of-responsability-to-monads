package org.example.chain_of_responsibilities;

public abstract class BaseRequestHandler implements RequestHandler {
    protected RequestHandler nextHandler;

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }


    protected String handleNextRequest(Request request) {
        if (nextHandler != null)
            return nextHandler.handleRequest(request);
        return "";
    }
}
