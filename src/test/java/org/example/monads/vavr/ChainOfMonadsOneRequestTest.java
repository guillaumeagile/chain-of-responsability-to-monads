package org.example.monads.vavr;

import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.example.monads.vavr.ProcessingError.ErrorType;
import org.junit.jupiter.api.Test;

import static org.assertj.vavr.api.VavrAssertions.*;
import static org.assertj.core.api.Assertions.*;

public class ChainOfMonadsOneRequestTest {

    @Test
    public void testSuccessfulRequest() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        assertThat(result).isRight();
        assertThat(result.get()).isEqualTo("Finally: Successfully processed request for: John Doe");
    }
    
    @Test
    public void testValidationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        assertThat(result).isLeft();
        assertThat(result.getLeft().type()).isEqualTo(ErrorType.VALIDATION);
        assertThat(result.getLeft().message()).isEqualTo("Data is empty");
    }
    
    @Test
    public void testAuthenticationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("", "admin", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        assertThat(result).isLeft();
        assertThat(result.getLeft().type()).isEqualTo(ErrorType.AUTHENTICATION);
        assertThat(result.getLeft().message()).isEqualTo("Originator is empty");
    }
    
    @Test
    public void testAuthorizationFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "user", "valid data");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        assertThat(result).isLeft();
        assertThat(result.getLeft().type()).isEqualTo(ErrorType.AUTHORIZATION);
        assertThat(result.getLeft().message()).isEqualTo("User does not have admin role");
    }
    
    @Test
    public void testBusinessLogicFailure() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "admin", "data with error");
        
        Either<ProcessingError, String> result = chainOfMonads.processChain(request);
        
        assertThat(result).isLeft();
        assertThat(result.getLeft().type()).isEqualTo(ErrorType.BUSINESS_LOGIC);
        assertThat(result.getLeft().message()).isEqualTo("Error processing data");
    }

    @Test
    public void testRoleVerificationBeforeBusinessLogic() {
        ChainOfMonads chainOfMonads = new ChainOfMonads();
        Request request = new Request("John Doe", "not-admin", "data with error");

        Either<ProcessingError, String> result = chainOfMonads.processChain(request);

        assertThat(result).isLeft();
        assertThat(result.getLeft().type()).isEqualTo(ErrorType.AUTHORIZATION);
        assertThat(result.getLeft().message()).isEqualTo("User does not have admin role");
    }
}
