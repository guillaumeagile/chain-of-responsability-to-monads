package org.example.chain_of_responsibilities;

public class BusinessLogicHandler extends BaseRequestHandler{

    @Override
    public String handleRequest(Request request)
    {
        return ", logic: Ok"  + handleNextRequest(request);
    }
}
