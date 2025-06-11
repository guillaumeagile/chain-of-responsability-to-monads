package org.example.monads.vavr;

import io.vavr.collection.List;
import io.vavr.control.Either;
import org.example.chain_of_responsibilities.Request;

public class ChainOfMonadsInLists {

    public List<Either<ProcessingError, String>> processChain(List<Request> requests) {
        return requests
                .map(this::processChain);
    }
    
    public Either<ProcessingError, String> processChain(Request request) {
        return  ChainOfMonads.validateRequest(request)
                .flatMap(ChainOfMonads::authenticateRequest)
                .flatMap(ChainOfMonads::authorizeRequest)
                .flatMap(ChainOfMonads::processBusinessLogic);
    }
    
    // Process all requests and filter only successful ones
    public List<String> processAndGetSuccessful(List<Request> requests) {
        return processChain(requests)
                .filter(Either::isRight)
                .map(Either::get);
    }
    
    // Process all requests and filter only failed ones with their errors
    public List<ProcessingError> processAndGetErrors(List<Request> requests) {
        return processChain(requests)
                .filter(Either::isLeft)
                .map(Either::getLeft);
    }
    
    // Validate a list of requests and return only valid ones
    public List<Request> validateRequests(List<Request> requests) {
        return requests
                .map(ChainOfMonads::validateRequest)
                .filter(Either::isRight)
                .map(Either::get);
    }
    
    // Authenticate a list of requests and return only authenticated ones
    public List<Request> authenticateRequests(List<Request> requests) {
        return requests
                .map(ChainOfMonads::validateRequest)
                .filter(Either::isRight)
                .map(Either::get)
                .map(ChainOfMonads::authenticateRequest)
                .filter(Either::isRight)
                .map(Either::get);
    }




}
