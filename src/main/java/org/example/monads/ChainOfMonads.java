package org.example.monads;

import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;

public class ChainOfMonads {

    public Either<String, String> processRequest(Request request) {
        return validateRequest(request)
                .flatMap(this::authenticateRequest)
                .flatMap(this::authorizeRequest)
                .flatMap(this::processBusinessLogic);
    }
    /*
What flatMap Does
    Mapping: Transforms a value inside a container (like Either) using a function
    Flattening: Prevents nested containers by "flattening" the result

Key Benefits in Your Error Handling Chain
    Short-circuiting: If any step fails, the rest are skipped
    Error preservation: The first error is captured and returned
    Clean composition: Multiple operations chain together without nesting
    Type safety: The compiler ensures your chain handles errors properly
    This is why monadic composition with flatMap is so powerful for error handling
        - it elegantly manages the "happy path" and error cases without complex if/else structures or exception handling.
     */



    private Either<String, Request> validateRequest(Request request) {
        if (request.data() == null || request.data().isEmpty()) {
            return Either.left("Validation failed: Data is empty");
        }
        return Either.right(request);
    }

    private Either<String, Request> authenticateRequest(Request request) {
        if (request.originator() == null || request.originator().isEmpty()) {
            return Either.left("Authentication failed: Originator is empty");
        }
        return Either.right(request);
    }

    private Either<String, Request> authorizeRequest(Request request) {
        if (request.role() == null || !request.role().equals("admin")) {
            return Either.left("Authorization failed: User does not have admin role");
        }
        return Either.right(request);
    }

    private Either<String, String> processBusinessLogic(Request request) {
        // Simulate some business logic processing
        try {
            // Simulate some processing that might throw an exception
            if (request.data().contains("error")) {
                throw new RuntimeException("Error processing data");
            }
            return Either.right("Finally: Successfully processed request for: " + request.originator());
        } catch (Exception e) {
            return Either.left("Business logic error: " + e.getMessage());
        }
    }
}
