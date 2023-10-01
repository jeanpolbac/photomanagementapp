package definitions;

import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.service.AlbumService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AlbumManagementTestDefs extends TestSetupDefs {
    private static final Logger logger = Logger.getLogger(definitions.AlbumManagementTestDefs.class.getName());

    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;

    private static ResponseEntity<String> response;


    public String getJWTKey() throws JSONException {
        logger.info("TestJWTKey: Generated");
        // Set the base URI and create a request
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        // Set the content-type header to indicate JSON data
        request.header("Content-Type", "application/json");

        // Create a JSON request body with user email and password
        JSONObject requestBody = new JSONObject();
        requestBody.put("emailAddress", "john.doe@example.com");
        requestBody.put("password", "hashed_password123");

        // Send a POST request to the authentication endpoint
        String endpoint = "/auth/users/login/";
        Response response = request.body(requestBody.toString()).post(BASE_URL + port + endpoint);

        // Extract and return the JWT key from the authentication response
        return response.jsonPath().getString("jwt");
    }

    @Given("A logged in user")
    public void aLoggedInUser() throws JSONException {
        logger.info("Step: A logged in user");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + getJWTKey());
    }

    @And("A list of albums are available")
    public void aListOfAlbumsAreAvailable() throws JSONException {
        logger.info("Step: A list of albums are available");
        // Obtain the JWT token for authorization
        String jwtToken = getJWTKey();

        // Create HTTP headers with the JWT token for authentication
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Create an HTTP entity with the headers
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            // Send a GET request to retrieve the list of albums
            ResponseEntity<String> response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/", HttpMethod.GET, entity, String.class);
            String responseBody = String.valueOf(response.getBody());
            logger.info("Response body: " + responseBody);

            if (responseBody != null && !responseBody.isEmpty()) {
                // Parse the JSON response to extract the list of albums
                List<Map<String, String>> albums = JsonPath.from(String.valueOf(response.getBody())).get("data");

                // Assert that the response status is OK and there are albums available
                Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
                Assert.assertTrue(albums.size() > 0);
            } else {
                // If the response is null or empty, fail the test
                Assert.fail("Received null or empty response");
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }


    @When("I add a album to my list")
    public void iAddAAlbumToMyList() throws JSONException {
        logger.info("Step: I add a album to my list");
        // Creating authorization and content type
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTKey());
        headers.add("Content-Type", "application/json");

        // Creating an object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Test Album");
        requestBody.put("description", "New Description");

        // Build our post request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/", HttpMethod.POST, entity, String.class);
    }


    @Then("The album is added")
    public void theAlbumIsAdded() {
        logger.info("Step: The album is added");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @When("I delete an album in my list")
    public void iDeleteAnAlbumInMyList() throws JSONException {
        logger.info("Step: I delete an album in my list");
        // Creating authorization and content type
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + getJWTKey());
            headers.add("Content-Type", "application/json");

            // Build our post request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/1", HttpMethod.DELETE, entity, String.class);
            logger.info("Response Status: " + response.getStatusCode());
        } catch (Exception e) {
            logger.warning("Test: Error while deleting an album " + e.getMessage());
        }
    }


    @Then("The album is removed")
    public void theAlbumIsRemoved() {
        logger.info("Step: The album is removed");
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());    }
}
