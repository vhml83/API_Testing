package com.cucumber.steps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import logger.Log;

import static io.restassured.RestAssured.given;

public class PracticeApiTesting {

    private static final String BASE_URL = "practice.expandtesting.com/notes/api";
    private static final String USER_EMAIL = System.getenv("userEmail");
    private static final String USER_PASSWORD = System.getenv("userPassword");
    private static String accessToken;
    private static Response response;

    @Given("A POST request is made to authenticate a user and return access token")
    public void getAccessToken() {
        Log.info("Requesting Access Token...");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/users/login", BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        response = given()
                .log().all()
                .header("Content-type","application/x-www-form-urlencoded")
                .header("accept", "application/json")
                .formParam("email", USER_EMAIL)
                .formParam("password", USER_PASSWORD)
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();
        Log.info("Response status code: " + ApiTests.responseStatusCode);

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();
        Log.info("response body: " + ApiTests.responseBodyString);

        // Get the access token
        JsonPath jsonPathEvaluator = response.jsonPath();
        Log.info("access token: " + jsonPathEvaluator.get("data.token"));
        // accessToken = jsonPathEvaluator.get("data.token");
        //return accessToken;
    }

    @Given("A GET request is made to check if the server is running and healthy")
    public void getHealthCheck() {
        Log.info("--- TEST CASE: GET Health Check");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/health-check", BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        response = given()
                // .log().all()
                .headers("accept", "application/json")
                .get();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();
        Log.info("Response status code: " + ApiTests.responseStatusCode);

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        if (ApiTests.responseStatusCode == 200) {
            // Set Json Schema Path
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/practiceapi/GetHealthCheck.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }

    }
}
