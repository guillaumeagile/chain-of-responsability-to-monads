package org.example.monads.vavr;

import io.vavr.collection.List;
import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.example.monads.vavr.ProcessingError.ErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ChainOfMonadsSeveralInListsTest {

    @Test
    public void testProcessManyRequests() {
        ChainOfMonadsInLists chainOfMonads = new ChainOfMonadsInLists();
        
        List<Request> requests = List.of(
            new Request("John", "admin", "valid data"),
            new Request("", "admin", "valid data"),       // Authentication failure
            new Request("Jane", "user", "valid data"),    // Authorization failure
            new Request("Bob", "admin", "data with error") // Business logic failure
        );
        
        List<Either<ProcessingError, String>> results = chainOfMonads.processChain(requests);
        
        // Should have 4 results
        Assertions.assertEquals(4, results.size());
        
        // First request should succeed
        Assertions.assertTrue(results.get(0).isRight());
        Assertions.assertEquals("Finally: Successfully processed request for: John", results.get(0).get());
        
        // Second request should fail with authentication error
        Assertions.assertTrue(results.get(1).isLeft());
        Assertions.assertEquals(ErrorType.AUTHENTICATION, results.get(1).getLeft().type());
        
        // Third request should fail with authorization error
        Assertions.assertTrue(results.get(2).isLeft());
        Assertions.assertEquals(ErrorType.AUTHORIZATION, results.get(2).getLeft().type());
        
        // Fourth request should fail with business logic error
        Assertions.assertTrue(results.get(3).isLeft());
        Assertions.assertEquals(ErrorType.BUSINESS_LOGIC, results.get(3).getLeft().type());
    }
    
    @Test
    public void testProcessAndGetSuccessful() {
        ChainOfMonadsInLists chainOfMonads = new ChainOfMonadsInLists();
        
        List<Request> requests = List.of(
            new Request("John", "admin", "valid data"),  // Will succeed
            new Request("", "admin", "valid data"),       // Will fail
            new Request("Jane", "user", "valid data"),    // Will fail
            new Request("Alice", "admin", "valid data")   // Will succeed
        );
        
        List<String> successfulResults = chainOfMonads.processAndGetSuccessful(requests);
        
        // Should have 2 successful results
        Assertions.assertEquals(2, successfulResults.size());
        Assertions.assertEquals("Finally: Successfully processed request for: John", successfulResults.get(0));
        Assertions.assertEquals("Finally: Successfully processed request for: Alice", successfulResults.get(1));
    }
    
    @Test
    public void testProcessAndGetErrors() {
        ChainOfMonadsInLists chainOfMonads = new ChainOfMonadsInLists();
        
        List<Request> requests = List.of(
            new Request("John", "admin", "valid data"),   // Will succeed
            new Request("", "admin", "valid data"),       // Authentication error
            new Request("Jane", "user", "valid data"),    // Authorization error
            new Request("Bob", "admin", "data with error") // Business logic error
        );
        
        List<ProcessingError> errors = chainOfMonads.processAndGetErrors(requests);
        
        // Should have 3 errors
        Assertions.assertEquals(3, errors.size());
        Assertions.assertEquals(ErrorType.AUTHENTICATION, errors.get(0).type());
        Assertions.assertEquals(ErrorType.AUTHORIZATION, errors.get(1).type());
        Assertions.assertEquals(ErrorType.BUSINESS_LOGIC, errors.get(2).type());
    }
    
    @Test
    public void testValidateRequests() {
        ChainOfMonadsInLists chainOfMonads = new ChainOfMonadsInLists();
        
        List<Request> requests = List.of(
            new Request("John", "admin", "valid data"),
            new Request("Jane", "user", ""),              // Validation failure
            new Request("Bob", "admin", "more data")
        );
        
        List<Request> validRequests = chainOfMonads.validateRequests(requests);
        
        // Should have 2 valid requests
        Assertions.assertEquals(2, validRequests.size());
        Assertions.assertEquals("John", validRequests.get(0).originator());
        Assertions.assertEquals("Bob", validRequests.get(1).originator());
    }
    
    @Test
    public void testAuthenticateRequests() {
        ChainOfMonadsInLists chainOfMonads = new ChainOfMonadsInLists();
        
        List<Request> requests = List.of(
            new Request("John", "admin", "valid data"),
            new Request("", "user", "data"),              // Authentication failure
            new Request("Bob", "admin", "more data"),
            new Request("Alice", "admin", "")             // Validation failure
        );
        
        List<Request> authenticatedRequests = chainOfMonads.authenticateRequests(requests);
        
        // Should have 2 authenticated requests (passed both validation and authentication)
        Assertions.assertEquals(2, authenticatedRequests.size());
        Assertions.assertEquals("John", authenticatedRequests.get(0).originator());
        Assertions.assertEquals("Bob", authenticatedRequests.get(1).originator());
    }
}
