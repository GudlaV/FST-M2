package activities;

import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Activity2 {
	String baseURI = "https://petstore.swagger.io/v2/user";
  @Test(priority=1)
  public void postResponseTest() throws IOException {
	  FileInputStream inputJSON = new FileInputStream("src/test/java/activities/input.json");
	  String reqBody = new String(inputJSON.readAllBytes());
	  Response response = given().contentType(ContentType.JSON).body(reqBody).when().post(baseURI);
	  inputJSON.close();
	  response.then().statusCode(200);
	  System.out.println(response.prettyPrint());
  }
  
  @Test(priority=2)
  public void getResponseTest() {
	  File outputJSON = new File("src/test/java/activities/output.json");
	  Response response = given().contentType(ContentType.JSON).pathParam("username", "jdoe").when().get(baseURI+"/{username}");
	  String resBody = response.getBody().asPrettyString();
	  
	  try {
		  outputJSON.createNewFile();
		  FileWriter wr = new FileWriter(outputJSON.getPath());
		  wr.write(resBody);
		  wr.close();
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  response.then().body("id", equalTo(1097));
	  response.then().body("username", equalTo("jdoe"));
	  response.then().body("firstName", equalTo("Justin"));
	  response.then().body("lastName", equalTo("Case"));
	  response.then().body("email", equalTo("justincase@mail.com"));
	  response.then().body("password", equalTo("password123"));
	  response.then().body("phone", equalTo("9812763450"));
  }
  
  @Test(priority=3)
  public void deleteResponseTest() {
	  Response response = given().contentType(ContentType.JSON).pathParam("username", "jdoe").when().delete(baseURI+"/{username}");
	  response.then().statusCode(200);
	  response.then().body("message", equalTo("jdoe"));
  }
}