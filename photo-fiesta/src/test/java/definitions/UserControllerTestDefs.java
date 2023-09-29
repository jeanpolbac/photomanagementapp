package definitions;

import com.example.photofiesta.PhotoFiestaApplication;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PhotoFiestaApplication.class)


public class UserControllerTestDefs {

    private static final Logger logger = Logger.getLogger(UserControllerTestDefs.class.getName());

    private static final String BASE_URL = "http://localhost:";
    private static final String basePath = "/auth/users/hello/";


    @LocalServerPort
    String port;

    private static Response response;

    @Given("A valid public endpoint")
    public void aValidPublicEndpoint() {
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(BASE_URL + port + basePath, HttpMethod.GET, null, String.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @When("I say hello")
    public void iSayHello() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.get(BASE_URL + port + basePath);
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
        response = RestAssured.given().contentType(ContentType.JSON).when().post(BASE_URL + port + url);
        Assert.assertEquals(400, response.getStatusCode());
    }

    @When("User sends a POST request with user details")
    public void userSendsAPOSTRequestWithUserDetails() throws JSONException {
        logger.info("Calling User sends a POST request with user details");
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName","slickrick");
        requestBody.put("emailAddress","slick@gmail.com");
        requestBody.put("password","password123");
        request.header("Content-Type","application/json");
        response = request.body(requestBody.toString()).post(BASE_URL + port + "/auth/users/register/");
    }

    @Then("The response status should be ok")
    public void theResponseStatusShouldBeOk() {
        logger.info("Calling the user is registered");
        Assert.assertEquals(201,response.getStatusCode());
    }
}

