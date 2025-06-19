package org.example.chain_of_responsibilities;

public class RequestProcessor {
    private final RequestHandler chain;

    public RequestProcessor() {
        // Create handlers
        RequestHandler authenticationHandler = new AuthenticationHandler();
        RequestHandler authorizationHandler = new AuthorizationHandler();
        RequestHandler validationHandler = new DataValidationHandler();
        RequestHandler logicHandler = new BusinessLogicHandler();

        // Set up the chain
        authenticationHandler.setNext(authorizationHandler);
        authorizationHandler.setNext(validationHandler);
        validationHandler.setNext(logicHandler);

        this.chain = authenticationHandler; // Start of the chain
    }

    public String processRequest(Request request) {
        return chain.handleRequest(request);
    }
}