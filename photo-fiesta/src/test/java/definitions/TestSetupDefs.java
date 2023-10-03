package definitions;

import com.example.photofiesta.PhotoFiestaApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PhotoFiestaApplication.class)
public class TestSetupDefs {
    // Base URL for testing
    public static final String BASE_URL = "http://localhost:";

    // Authentication Endpoints
    public static final String helloEndpoint = "/auth/users/hello/";
    public static final String registerEndpoint = "/auth/users/register/";
    public static final String loginEndpoint= "/auth/users/login/";


    // Album Endpoints
    public static final String userAlbumsEndpoint = "/api/albums/";
    public static final String userSpecificAlbumEndpoint = "/api/albums/1/";
    // Photo Endpoints
    public static final String albumPhotosEndpoint = "/api/albums/1/photos/";
    public static final String albumSpecificPhotoEndpoint = "/api/albums/1/photos/1/";


    // Content-Type Json
    public static final String TypeJson = "application/json";



    @LocalServerPort
    public String port;
}
