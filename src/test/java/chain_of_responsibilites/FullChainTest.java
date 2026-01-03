package chain_of_responsibilites;

import org.example.chain_of_responsibilities.Request;
import org.example.chain_of_responsibilities.RequestProcessor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class FullChainTest {

    @Test
    public void TestThatNeedToBeFixed() {
        RequestProcessor processor = new RequestProcessor();
        Request request = new Request("John Doe", "admin", "secret data");
        var result = processor.processRequest(request);

         assertThat(result)
             .isNotNull()
             .isEqualTo("originator: Ok, authorization: Ok, validation: Ok, logic: Ok");
    }
}
