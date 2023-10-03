package definitions;


import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.repository.PhotoRepository;
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
    private static ResponseEntity<String> response;


    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private HttpHeaders createAuthHeaders() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTToken());
        headers.add("Content-Type", TypeJson);
        return headers;
    }


    public String getJWTToken() throws JSONException {
        logger.info("TestJWTToken: Generated");
        // Set the base URI and create a request
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        // Set the content-type header to indicate JSON data
        request.header("Content-Type", TypeJson);

        // Create a JSON request body with user email and password
        JSONObject requestBody = new JSONObject();
        requestBody.put("emailAddress", "john.doe@photofiesta.com");
        requestBody.put("password", "password123");

        // Send a POST request to the authentication endpoint
        Response response = request.body(requestBody.toString()).post(BASE_URL + port + loginEndpoint);

        // Extract and return the JWT token from the authentication response
        return response.jsonPath().getString("jwt");
    }

    @Given("A logged in user")
    public void aLoggedInUser() throws JSONException {
        logger.info("Step: A logged in user");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + getJWTToken());
    }

    @And("A list of albums are available")
    public void aListOfAlbumsAreAvailable() throws JSONException {
        logger.info("Step: A list of albums are available");
        // Obtain the JWT token for authorization
        // Create HTTP headers with the JWT token for authentication
        HttpHeaders headers = createAuthHeaders();

        // Create an HTTP entity with the headers
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            // Send a GET request to retrieve the list of albums
            ResponseEntity<String> response = new RestTemplate().exchange(BASE_URL + port + userAlbumsEndpoint, HttpMethod.GET, entity, String.class);
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
        HttpHeaders headers = createAuthHeaders();

        // Creating an object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Test Album");
        requestBody.put("description", "New Description");

        // Build our post request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        response = new RestTemplate().exchange(BASE_URL + port + userAlbumsEndpoint, HttpMethod.POST, entity, String.class);
    }


    @Then("The album is added")
    public void theAlbumIsAdded() {
        logger.info("Step: The album is added");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @When("I delete an album in my list")
    public void iDeleteAnAlbumInMyList() {
        logger.info("Step: I delete an album in my list");
        // Creating authorization and content type
        try {
            HttpHeaders headers = createAuthHeaders();

            // Build our post request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            response = new RestTemplate().exchange(BASE_URL + port + userSpecificAlbumEndpoint, HttpMethod.DELETE, entity, String.class);
            logger.info("Response Status: " + response.getStatusCode());
        } catch (Exception e) {
            logger.warning("Test: Error while deleting an album " + e.getMessage());
        }

    }

    @Then("The album is removed")
    public void theAlbumIsRemoved() {
        logger.info("Step: The album is removed");
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

// error starts here
    @When("I view a single photo in my album")
    public void iViewASinglePhotoInMyAlbum() throws JSONException {
    response = new RestTemplate().exchange(BASE_URL + port + albumSpecificPhotoEndpoint, HttpMethod.GET, new HttpEntity<>(createAuthHeaders()), String.class);
    }

    @Then("I see a single photo")
    public void iSeeASinglePhoto() {
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @When("I view the photos in my album")
    public void iViewThePhotosInMyAlbum() throws JSONException {
        HttpHeaders headers = createAuthHeaders();

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = new RestTemplate().exchange(BASE_URL + port + albumPhotosEndpoint, HttpMethod.GET, entity, String.class);
    }

    @Then("I see a list of photos")
    public void iSeeAListOfPhotos() {
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @When("I add a new photo")
    public void iAddANewPhoto() throws JSONException {

        // Creating object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Test Photo");
        requestBody.put("description", "New Description");
        requestBody.put("imageUrl","http://test-image/image.jpeg");
        // Build our post request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), createAuthHeaders());
        response = new RestTemplate().exchange(BASE_URL + port + albumPhotosEndpoint, HttpMethod.POST, entity, String.class);
    }

    @Then("The photo is added to my default album")
    public void thePhotoIsAddedToMyDefaultAlbum() {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @When("I update a photo")
    public void iUpdateAPhoto() throws JSONException {
        // Creating object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Updated Test Photo");
        requestBody.put("description", "Updated Description");
        requestBody.put("imageUrl", "http://images/update-image-test.jpeg");

        // Build our put request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), createAuthHeaders());
        response = new RestTemplate().exchange(BASE_URL + port + albumSpecificPhotoEndpoint, HttpMethod.PUT, entity, String.class);
    }

    @Then("The photo is updated")
    public void thePhotoIsUpdated() {
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @When("I delete a photo from an album")
    public void iDeleteAPhotoFromAnAlbum() throws JSONException {
        // Build and send the DELETE request to your endpoint
        response = new RestTemplate().exchange(BASE_URL + port + albumSpecificPhotoEndpoint, HttpMethod.DELETE, new HttpEntity<>(createAuthHeaders()), String.class);

    }

    @Then("The photo is deleted")
    public void thePhotoIsDeleted() {
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
