package org.example.monads;

import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.example.monads.ProcessingError.ErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ChainOfMonadsTest {

    @Test
    public void testSuccessfulRequest() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        Assertions.assertTrue(result.isRight());
        Assertions.assertEquals("Finally: Successfully processed request for: John Doe", result.get());
    }
    
    @Test
    public void testValidationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals(ErrorType.VALIDATION, result.getLeft().type());
        Assertions.assertEquals("Data is empty", result.getLeft().message());
    }
    
    @Test
    public void testAuthenticationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("", "admin", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals(ErrorType.AUTHENTICATION, result.getLeft().type());
        Assertions.assertEquals("Originator is empty", result.getLeft().message());
    }
    
    @Test
    public void testAuthorizationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "user", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals(ErrorType.AUTHORIZATION, result.getLeft().type());
        Assertions.assertEquals("User does not have admin role", result.getLeft().message());
    }
    
    @Test
    public void testBusinessLogicFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "data with error");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals(ErrorType.BUSINESS_LOGIC, result.getLeft().type());
        Assertions.assertEquals("Error processing data", result.getLeft().message());
    }

    @Test
    public void testRoleVerificationBeforeBusinessLogic() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "not-admin", "data with error");

        Either<ProcessingError, String> result = chainOfMonads.processChain(request);

        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals(ErrorType.AUTHORIZATION, result.getLeft().type());
        Assertions.assertEquals("User does not have admin role", result.getLeft().message());
    }
}
