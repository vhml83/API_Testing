package com.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import logger.Log;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class PracticeApiTesting {

    private static final String BASE_URL = "practice.expandtesting.com/notes/api";
    private static final String USER_EMAIL = System.getenv("userEmail");
    private static final String USER_PASSWORD = System.getenv("userPassword");
    private static String accessToken;
    private static Response response;

    @Given("The server is up and running")
    public void getHealthCheck() {
        Log.info("GET Health Check");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/health-check", BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        response = given()
                .headers("accept", "application/json")
                .get();

        // Response status code == 200
        assertEquals(response.getStatusCode(), 200);
    }

    @And("the user is able to authenticate")
    public void getAccessToken() {
        Log.info("Requesting Access Token...");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/users/login", BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        // Set Request Body
        String body = String.format("email=%s&password=%s", USER_EMAIL, USER_PASSWORD);

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
                .body(body)
                .post();

        // Response status code == 200
        assertEquals(response.getStatusCode(), 200);

        // Set the access token
        JsonPath jsonPathEvaluator = response.jsonPath();
        Log.info("access token: " + jsonPathEvaluator.get("data.token"));
        accessToken = jsonPathEvaluator.get("data.token");
    }

    @When("A GET request is made to retrieve user profile information")
    public void getUserProfileInformation() {
        Log.info("Requesting User Profile Information...");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/users/profile", BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        response = given()
                .headers("accept", "application/json")
                .headers("x-auth-token", accessToken)
                .get();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();
        Log.info("Response status code: " + ApiTests.responseStatusCode);

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        if (ApiTests.responseStatusCode == 200) {
            // Set Json Schema Path
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/practiceapi/GetUserProfileInfo.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        }
    }

    @Then("The response body contains message: {string}")
    public void assertMessage(String message) {

    }

}
