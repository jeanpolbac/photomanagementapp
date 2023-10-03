package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

public class UserControllerTestDefs extends TestSetupDefs {

    private static final Logger logger = Logger.getLogger(UserControllerTestDefs.class.getName());

    private static Response response;

    @Given("A valid public endpoint")
    public void aValidPublicEndpoint() {
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(BASE_URL + port + helloEndpoint, HttpMethod.GET, null, String.class);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @When("I say hello")
    public void iSayHello() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.get(BASE_URL + port + helloEndpoint);
    }

    @Then("Hello is shown")
    public void helloIsShown() {
        JsonPath jsonPath = response.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("Hello", message);
    }


    @Given("The register url is {string}")
    public void theRegisterUrl(String url) {
        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + port + url)
                .then()
                .extract()
                .statusCode(); // Extract the status code from the response
        // Assert that the status code is not 403
        assert statusCode != 403 : "Expected status code to not be 403, but it was: " + statusCode;
    }

    @When("User sends a POST request with user details")
    public void userSendsAPOSTRequestWithUserDetails() throws JSONException {
        logger.info("Calling User sends a POST request with user details");
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName","photofiestateam");
        requestBody.put("emailAddress","photofiestateam@gmail.com");
        requestBody.put("password","password12345");
        request.header("Content-Type","application/json");
        response = request.body(requestBody.toString()).post(BASE_URL + port + registerEndpoint);
    }

    @Then("The response status should be ok")
    public void theResponseStatusShouldBeOk() {
        logger.info("Calling the user is registered");
        Assert.assertEquals(201,response.getStatusCode());
    }

    @And("The response should contain the user details")
    public void theResponseShouldContainTheUserDetails() {
        JsonPath jsonPath = response.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertEquals("success", message);
    }

    @Given("The registered user exists")
    public void theRegisteredUserExists() throws JSONException {
        RequestSpecification request = RestAssured.given();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emailAddress", "john.doe@photofiesta.com");
        jsonObject.put("password","password123");
        response = request.contentType(ContentType.JSON).body(jsonObject.toString()).post(BASE_URL + port + loginEndpoint);
    }
    @When("The user details are validated")
    public void theUserDetailsAreValidated() {
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Then("The user receives a jwt token")
    public void theUserReceivesAJwtToken() {
        Assert.assertNotNull(response.jsonPath().getString("jwt"));
    }
}

