package definitions;

import com.example.photofiesta.PhotoFiestaApplication;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


}

