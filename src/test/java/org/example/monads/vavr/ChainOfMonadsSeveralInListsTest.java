package org.example.monads.vavr;

import io.vavr.collection.List;
import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;
import org.example.monads.vavr.ProcessingError.ErrorType;
import org.junit.jupiter.api.Test;

import static org.assertj.vavr.api.VavrAssertions.*;
import static org.assertj.core.api.Assertions.*;

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
        
        assertThat(results).hasSize(4);
        
        assertThat(results.get(0)).isRight();
        assertThat(results.get(0).get()).isEqualTo("Finally: Successfully processed request for: John");
        
        assertThat(results.get(1)).isLeft();
        assertThat(results.get(1).getLeft().type()).isEqualTo(ErrorType.AUTHENTICATION);
        
        assertThat(results.get(2)).isLeft();
        assertThat(results.get(2).getLeft().type()).isEqualTo(ErrorType.AUTHORIZATION);
        
        assertThat(results.get(3)).isLeft();
        assertThat(results.get(3).getLeft().type()).isEqualTo(ErrorType.BUSINESS_LOGIC);
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
        
        assertThat(successfulResults)
            .hasSize(2)
            .containsExactly(
                "Finally: Successfully processed request for: John",
                "Finally: Successfully processed request for: Alice"
            );
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
        
        assertThat(errors)
            .hasSize(3)
            .extracting(ProcessingError::type)
            .containsExactly(
                ErrorType.AUTHENTICATION,
                ErrorType.AUTHORIZATION,
                ErrorType.BUSINESS_LOGIC
            );
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
        
        assertThat(validRequests)
            .hasSize(2)
            .extracting(Request::originator)
            .containsExactly("John", "Bob");
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
        
        assertThat(authenticatedRequests)
            .hasSize(2)
            .extracting(Request::originator)
            .containsExactly("John", "Bob");
    }
}
