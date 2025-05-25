package org.example.chain_of_responsibilities;

public class AuthenticationHandler extends BaseRequestHandler implements RequestHandler {


    @Override
    public String handleRequest(Request request) {
        if (request.originator().isEmpty())
            return "originator: KO";
        return "originator: Ok" + handleNextRequest(request);
    }

}
