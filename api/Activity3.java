package activities;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

public class Activity3 {

	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;

	@BeforeClass
	public void setup() {
		requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON)
				.setBaseUri("https://petstore.swagger.io/v2/pet").build();
		responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON)
				.expectBody("status", equalTo("alive")).build();
	}

	@DataProvider
	public Object[][] providePetData() {
		Object[][] petData = new Object[][] { { 77232, "Riley", "alive" }, { 77233, "Hansel", "alive" } };
		return petData;
	}

	@Test(priority = 1)
	public void addPets() {
		Response response = given().spec(requestSpec)
				.body("{\n" + "   \"id\":77232,\n" + "   \"name\":\"Riley\",\n" + "   \"status\":\"alive\"\n" + "}")
				.when().post();

		response = given().spec(requestSpec)
				.body("{\n" + "   \"id\":77233,\n" + "   \"name\":\"Hansel\",\n" + "   \"status\":\"alive\"\n" + "}")
				.when().post();

		response.then().spec(responseSpec);
	}

	@Test(dataProvider = "providePetData", priority = 2)
	public void getPets(int id, String name, String status) {
		Response response = given().spec(requestSpec).pathParam("petId", id).when().get("/{petId}");
		System.out.println(response.prettyPrint());
		response.then().spec(responseSpec).assertThat().body("name", equalTo(name)).body("status", equalTo(status));
	}

	@Test(dataProvider = "providePetData", priority=3)
	public void deletePets(int id, String name, String status) {
		Response response = given().spec(requestSpec).pathParam("petId", id).when().delete("/{petId}");
		System.out.println(response.prettyPrint());
		response.then().body("code", equalTo(200));
	}
}