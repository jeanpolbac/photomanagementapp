package definitions;

import com.example.photofiesta.models.Album;
import com.example.photofiesta.models.Photo;
import com.example.photofiesta.models.User;
import com.example.photofiesta.repository.AlbumRepository;
import com.example.photofiesta.repository.PhotoRepository;
import com.example.photofiesta.service.AlbumService;
import io.cucumber.java.bs.A;
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
import java.util.Optional;
import java.util.logging.Logger;

public class AlbumManagementTestDefs extends TestSetupDefs {
    private static final Logger logger = Logger.getLogger(definitions.AlbumManagementTestDefs.class.getName());

    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private static ResponseEntity<String> response;


    public String getJWTKey() throws JSONException {

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
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + getJWTKey());
    }

    @And("A list of albums are available")
    public void aListOfAlbumsAreAvailable() throws JSONException {
        String jwtToken = getJWTKey();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/", HttpMethod.GET, entity, String.class);
            String responseBody = String.valueOf(response.getBody());
            logger.info("Response body: " + responseBody);
            if (responseBody != null && !responseBody.isEmpty()) {

                List<Map<String, String>> albums = JsonPath.from(String.valueOf(response.getBody())).get("data");

                Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
                Assert.assertTrue(albums.size() > 0);
            } else {
                Assert.fail("Received null or empty response");
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }


    @When("I add a album to my list")
    public void iAddAAlbumToMyList() throws JSONException {
        // Creating authorization and content type
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTKey());
        headers.add("Content-Type", "application/json");

        // Creating object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Test Album");
        requestBody.put("description", "New Description");

        // Build our post request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/", HttpMethod.POST, entity, String.class);
    }


    @Then("The album is added")
    public void theAlbumIsAdded() {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


//    @When("I delete an album in my list")
//    public void iDeleteAnAlbumInMyList() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }


//    @Then("The album is removed")
//    public void theAlbumIsRemoved() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }


    @When("I add a new photo")
    public void iAddANewPhoto() throws JSONException {
        // Creating authorization and content type
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTKey());
        headers.add("Content-Type", "application/json");

        // Creating object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Test Photo");
        requestBody.put("description", "New Description");
        requestBody.put("imageUrl","http://test-image/image.jpeg");
        // Build our post request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/1/photos/", HttpMethod.POST, entity, String.class);
    }

    @Then("The photo is added to my default album")
    public void thePhotoIsAddedToMyDefaultAlbum() {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @When("I update a photo")
    public void iUpdateAPhoto() throws JSONException {
        // Creating authorization and content type
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTKey());
        headers.add("Content-Type", "application/json");
        // Creating object to pass through
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Updated Test Photo");
        requestBody.put("description", "Updated Description");
        requestBody.put("imageUrl", "http://images/update-image-test.jpeg");

        // Build our put request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/1/photos/1/", HttpMethod.PUT, entity, String.class);
    }

    @Then("The photo is updated")
    public void thePhotoIsUpdated() {
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @When("I delete a photo from an album")
    public void iDeleteAPhotoFromAnAlbum() throws JSONException {
        // Creating authorization
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJWTKey());

        // Build and send the DELETE request to your endpoint
        response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/1/photos/1/", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

    }

    @Then("The photo is deleted")
    public void thePhotoIsDeleted() {
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
