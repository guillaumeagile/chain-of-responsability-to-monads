package org.example.monads;

import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ChainOfMonadsTest {

    @Test
    public void testSuccessfulRequest() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "valid data");
        
        Either<String, String> result = chainOfMonads.processRequest(request);
        
        Assertions.assertTrue(result.isRight());
        Assertions.assertEquals("Finally: Successfully processed request for: John Doe", result.get());
    }
    
    @Test
    public void testValidationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "");
        
        Either<String, String> result = chainOfMonads.processRequest(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Validation failed: Data is empty", result.getLeft());
    }
    
    @Test
    public void testAuthenticationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("", "admin", "valid data");
        
        Either<String, String> result = chainOfMonads.processRequest(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Authentication failed: Originator is empty", result.getLeft());
    }
    
    @Test
    public void testAuthorizationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "user", "valid data");
        
        Either<String, String> result = chainOfMonads.processRequest(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Authorization failed: User does not have admin role", result.getLeft());
    }
    
    @Test
    public void testBusinessLogicFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "data with error");
        
        Either<String, String> result = chainOfMonads.processRequest(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Business logic error: Error processing data", result.getLeft());
    }

    @Test
    public void testRoleVerificationBeforeBusinessLogic() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "not-admin", "data with error");

        Either<String, String> result = chainOfMonads.processRequest(request);

        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Authorization failed: User does not have admin role", result.getLeft());
    }
}
