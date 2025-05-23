package chain_of_responsibilites;

import org.example.chain_of_responsibilities.AuthenticationHandler;
import org.example.chain_of_responsibilities.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AuthTest {

    @Test
    public void testPass() {
      var auth = new AuthenticationHandler();
      var result = auth.handleRequest(new Request("John Doe", "admin", "secret data"));
      Assertions.assertEquals("originator: Ok", result);
    }

    @Test
    public void testFail() {
        var auth = new AuthenticationHandler();
        var result = auth.handleRequest(new Request("", "admin", "secret data"));
        Assertions.assertEquals("originator: KO", result);
    }
}
