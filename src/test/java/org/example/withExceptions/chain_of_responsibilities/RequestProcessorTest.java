package org.example.withExceptions.chain_of_responsibilities;

import org.example.chain_of_responsibilities.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestProcessorTest {
    private RequestProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new RequestProcessor();
    }

    @Test
    void testSuccessfulProcessing() {
        // Given a valid request with admin role
        Request validRequest = new Request("admin", "admin", "valid data");

        // When processing the request
        String result = processor.processRequest(validRequest);

        // Then the request should be processed successfully
        assertTrue(result.contains("originator: Ok"));
        assertTrue(result.contains("authorization: Ok"));
    }

    @Test
    void testAuthenticationFailure() {
        // Given a request with empty originator
        Request invalidRequest = new Request("", "admin", "valid data");

        // When processing the request
        String result = processor.processRequest(invalidRequest);

        // Then an authentication error message should be returned
        assertEquals("AUTHENTICATION error", result);
    }

    @Test
    void testAuthorizationFailure() {
        // Given a request with non-admin originator
        Request invalidRequest = new Request("user", "user", "valid data");

        // When processing the request
        String result = processor.processRequest(invalidRequest);

        // Then an authorization error message should be returned
        assertEquals("AUTHORIZATION error", result);
    }

}