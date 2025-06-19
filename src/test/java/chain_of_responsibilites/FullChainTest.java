package chain_of_responsibilites;

import org.example.chain_of_responsibilities.Request;
import org.example.chain_of_responsibilities.RequestProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class FullChainTest {

    @Test
    public void TestThatNeedToBeFixed() {
        RequestProcessor processor = new RequestProcessor();
        Request request = new Request("John Doe", "admin", "secret data");
        var result = processor.processRequest(request);
        // You might want to add assertions here
         Assertions.assertNotNull(result);

         //something missing :(
         Assertions.assertEquals("originator: Ok, authorization: Ok, validation: Ok, logic: Ok", result);
         // we can fix it
        // but
        // too bad that the compiler doesn't prevent us for such errrors
    }
}
