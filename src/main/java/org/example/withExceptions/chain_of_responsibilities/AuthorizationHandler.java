package org.example.withExceptions.chain_of_responsibilities;

import org.example.chain_of_responsibilities.Request;
import org.example.monads.ProcessingError;

public class AuthorizationHandler implements RequestHandler {

    private RequestHandler nextHandler;

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /// booooooh Java quelle merde
    /// si tu ne mets pas le throws ProcessingException, ca compile quand meme :(
    /// en fait le compilateur ne t'impose pas de respecter l'interface
    /// tu peux throw n'importe quelle exception si tu mets en checked exception autre chose que ce qui est dans l'interface
    /// du coup, ca sert à quoi leur truc????
    @Override
    public String handleRequest(Request request) throws ProcessingException {
        if (!request.originator().equals("admin")) {
            throw new ProcessingException( ProcessingError.ErrorType.AUTHORIZATION , "Authorization failed");
        }
        return ", authorization: Ok";
    }
}
