package org.example.chain_of_responsibilities;

public record Request (
     String originator,
    String role,
    String data
) {
}
