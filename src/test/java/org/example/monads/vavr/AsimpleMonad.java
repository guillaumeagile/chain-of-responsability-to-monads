package org.example.monads.vavr;

import org.example.chain_of_responsibilities.Request;
import org.junit.jupiter.api.Test;

import static org.assertj.vavr.api.VavrAssertions.*;
import static org.assertj.core.api.Assertions.*;

public class AsimpleMonad
{
    @Test
    public void Right() {

         var sut = ChainOfMonads.validateRequest(new Request("John Doe", "admin", "secret data"));
         assertThat(sut).isRight();
         assertThat(sut.get().data()).isEqualTo("secret data");
    }

    @Test
    public void Left() {

        var sut = ChainOfMonads.validateRequest(new Request("John Doe", "admin", ""));
        assertThat(sut).isLeft();
        assertThat(sut.getLeft().type()).isEqualTo(ProcessingError.ErrorType.VALIDATION);
        assertThat(sut.getLeft().message()).isEqualTo("Data is empty");
    }
}
