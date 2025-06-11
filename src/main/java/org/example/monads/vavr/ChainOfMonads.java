package org.example.monads.vavr;

import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.example.monads.vavr.ProcessingError.ErrorType;

public class ChainOfMonads {


    public static Either<ProcessingError, Request> validateRequest(Request request) {
        if (request.data() == null || request.data().isEmpty()) {
            return Either.left(new ProcessingError(ErrorType.VALIDATION, "Data is empty"));
        }
        return Either.right(request);
    }

    public static Either<ProcessingError, Request> authenticateRequest(Request request) {
        if (request.originator() == null || request.originator().isEmpty()) {
            return Either.left(new ProcessingError(ErrorType.AUTHENTICATION, "Originator is empty"));
        }
        return Either.right(request);
    }

    public static Either<ProcessingError, Request> authorizeRequest(Request request) {
        if (request.role() == null || !request.role().equals("admin")) {
            return Either.left(new ProcessingError(ErrorType.AUTHORIZATION, "User does not have admin role"));
        }
        return Either.right(request);
    }

    public static Either<ProcessingError, String> processBusinessLogic(Request request) {
        try {
            // Simulate some processing that might throw an exception
            if (request.data().contains("error")) {
                throw new RuntimeException("Error processing data");
            }
            return Either.right("Finally: Successfully processed request for: " + request.originator());
        } catch (Exception e) {
            return Either.left(new ProcessingError(ErrorType.BUSINESS_LOGIC, e.getMessage()));
        }
    }
    // map BusinessResponse to ViewDto




    public Either<ProcessingError, String> processChain(Request request) {
        return ChainOfMonads.validateRequest(request)
                .flatMap(ChainOfMonads::authenticateRequest)
                .flatMap(ChainOfMonads::authorizeRequest)
                .flatMap(ChainOfMonads::processBusinessLogic);
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



}
