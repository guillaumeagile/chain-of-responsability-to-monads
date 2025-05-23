package org.example.chain_of_responsibilities;

public  class AuthenticationHandler implements RequestHandler {
    private RequestHandler nextHandler;

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String handleRequest(Request request) {
        if (request.originator().isEmpty())
            return "originator: KO";
        return "originator: Ok" + nextHandler.handleRequest(request);
    }
}
