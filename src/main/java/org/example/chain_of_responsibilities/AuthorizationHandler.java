package org.example.chain_of_responsibilities;

public class AuthorizationHandler  extends BaseRequestHandler{


    @Override
    public String handleRequest(Request request) {
        return ", authorization: Ok"  + handleNextRequest(request);
    }
}
