package org.example.chain_of_responsibilities;

public class DataValidationHandler extends BaseRequestHandler {


    @Override
    public String handleRequest(Request request) {
        return ", validation: Ok";
    }
}
