package rest_assured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class EjemploTestAPI {

    @Test
    public void ApiTest() {
        RestAssured.baseURI = String.format("https://reqres.in/api/users?page=1");
        Response response = given().headers("Accept", "*/*").get();
        String bodyResponse = response.getBody().prettyPrint();

        // Validar los headers
        // String headersResponse = response.getHeader().toString();

    }

}
