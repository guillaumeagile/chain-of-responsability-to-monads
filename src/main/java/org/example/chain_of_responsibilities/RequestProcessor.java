package org.example.chain_of_responsibilities;

public class RequestProcessor {
    private final RequestHandler chain;

    public RequestProcessor() {
        // Create handlers
        RequestHandler authHandler = new AuthenticationHandler();
        RequestHandler authzHandler = new AuthorizationHandler();
        RequestHandler validationHandler = new DataValidationHandler();
        RequestHandler logicHandler = new BusinessLogicHandler();

        // Set up the chain
        authHandler.setNext(authzHandler);
        authzHandler.setNext(validationHandler);
        validationHandler.setNext(logicHandler);

        this.chain = authHandler; // Start of the chain
    }

    public String processRequest(Request request) {
        return chain.handleRequest(request);
    }
}