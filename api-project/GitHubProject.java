package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GitHubProject {

    RequestSpecification requestSpec;
    String sshKey;
    int id;

    @BeforeClass
    public void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token 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")
                .build();
    }

    @Test(priority = 1)
    public void postRequestTest(){
        //Request Body
        String reqBody = "{\"title\": \"FST GitHub Token Test APIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCWgIx1HXiTw28efoEv+jW/6C9q2Dn2M3WHUL6J5I8wa6e+nCbmZofKrVnW97Tj2dR0o1Z5wMTjxKEqWU2Qt5SlAAD9XCM/jz7pyZ04MlT7EW65R1P/9Ar+jRsQgzi0vLNVqDX7Zd4HdlM9dJ3fZaFhbntnqVwvN8PwuCtA8MTSePtaFwhSR3rhFclCMK8TmND9IRuA7telr9sGRbNEBUNK/hRw54CR3wmMgZPb/2dKvvh4AFjZ3OqppgXIOZR1Q2m6MWoydPie81LI7FBawxZxq9N7hl+StLyZItEN0Nk27dJrrZvkQtbf64c8FdO8zzG8LE9v6zq5Aeo3VQhHrUiD \"}";
        //Create resource
        Response response = given().spec(requestSpec).body(reqBody).when().post("/user/keys");
        id = response.then().extract().body().path("id");
        //Logging
        Reporter.log(response.asPrettyString());
        //Assertions
        response.then().statusCode(201);
    }

    @Test(priority =2)
    public void getRequestTest() {
        //Get resource
        Response response = given().spec(requestSpec)
                .when().pathParam("keyId", id).get("/user/keys/{keyId}");
        //Logging
        Reporter.log(response.asPrettyString());
        //Assertions
        response.then().statusCode(200);
    }

    @Test(priority =3)
    public void deleteRequestTest(){
        //Delete resource
        Response response = given().spec(requestSpec)
                .when().pathParam("keyId", id).delete("/user/keys/{keyId}");
        //Logging
        Reporter.log(response.asPrettyString());
        //Assertions
        response.then().statusCode(204);
    }
}
