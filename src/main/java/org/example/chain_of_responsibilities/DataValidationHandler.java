package org.example.chain_of_responsibilities;

public class DataValidationHandler extends BaseRequestHandler {


    @Override
    public String handleRequest(Request request) {
        // HINT the problem is here!
        return ", validation: Ok" ;
    }
}
