package org.example.monads;

import org.example.chain_of_responsibilities.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AsimpleMonad
{
    @Test
    public void Right() {

         var sut = ChainOfMonads.validateRequest(new Request("John Doe", "admin", "secret data"));
         Assertions.assertTrue(sut.isRight());
         Assertions.assertEquals("secret data", sut.get().data());
    }

    @Test
    public void Left() {

        var sut = ChainOfMonads.validateRequest(new Request("John Doe", "admin", ""));
        Assertions.assertTrue(sut.isLeft());
       // Assertions.assertEquals("what data???", sut.get().data());  // CANNOT GET DATA on Left

        Assertions.assertEquals( ProcessingError.class, sut.getLeft().getClass());

        Assertions.assertEquals(ProcessingError.ErrorType.VALIDATION, sut.getLeft().type());
        Assertions.assertEquals("Data is empty", sut.getLeft().message());
    }
}
