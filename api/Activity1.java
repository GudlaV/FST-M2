package activities;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity1 {
	String id;
	
  @Test(priority=1)
  public void postResponseTest() {
	  String baseURI = "https://petstore.swagger.io/v2/pet";
	  Response response = given().contentType(ContentType.JSON).body("{\n"
	  		+ "   \"id\":77232,\n"
	  		+ "   \"name\":\"Riley\",\n"
	  		+ "   \"status\":\"alive\"\n"
	  		+ "}").when().post(baseURI);
	  
	  System.out.println("POST response: "+response.prettyPrint());
	  
	  response.then().body("id", equalTo(77232));
	  response.then().body("name", equalTo("Riley"));
	  response.then().body("status", equalTo("alive"));
	  
	  id = response.then().extract().body().path("id").toString();
  }
  
  @Test(priority=2)
  public void getResponseTest() {
	  String baseURI = "https://petstore.swagger.io/v2/pet/{petID}";
	  Response response = given().contentType(ContentType.JSON).pathParam("petID", id).when().get(baseURI);
	  
	  System.out.println("GET response: "+response.prettyPrint());
	  
	  response.then().body("id", equalTo(77232));
	  response.then().body("name", equalTo("Riley"));
	  response.then().body("status", equalTo("alive"));
  }
  
  @Test(priority=3)
  public void deleteResponseTest() {
	  String baseURI = "https://petstore.swagger.io/v2/pet/{petId}";
	  Response response = given().contentType(ContentType.JSON).pathParam("petId", "77232").when().delete(baseURI);
	  
	  response.then().statusCode(200);
	  String message = response.then().extract().body().path("message");
	  System.out.println("Delete response: "+response.prettyPrint());
	  System.out.println("Mesage after delete: "+message);
  }
}