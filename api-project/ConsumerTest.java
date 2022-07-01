package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> headers = new HashMap<>();
    //API Resource path
    String resourcePath = "/api/users";

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        // Create headers
        headers.put("Content-Type", "application/json");

        //Create body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        //Create Pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .path(resourcePath)
                .headers(headers)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest(){
        //Set the BaseURI
        final String baseURI = "http://localhost:8282";

        //Create request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 333);
        reqBody.put("firstName", "Venkateshwar");
        reqBody.put("lastName", "Gudla");
        reqBody.put("email", "gvenky@gmal.com");

        //Generate Response
        Response response = given().headers(headers).body(reqBody)
                .when().post(baseURI+resourcePath);

        System.out.println(response.asPrettyString());

        //Assertions
        response.then().statusCode(201);
    }
}
