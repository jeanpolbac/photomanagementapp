package definitions;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AlbumManagementTestDefs extends TestSetupDefs {
    private static final Logger logger = Logger.getLogger(definitions.AlbumManagementTestDefs.class.getName());

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

    @Given("A list of albums are available")
    public void aListOfAlbumsAreAvailable() throws JSONException {
        String jwtToken = getJWTKey();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(BASE_URL + port + "/api/albums/", HttpMethod.GET, entity, String.class);
            String responseBody = String.valueOf(response.getBody());
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
}
