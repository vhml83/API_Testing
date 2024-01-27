package com.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import logger.Log;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.*;

public class PetStoreApi {

    private static final String PET_STORE_BASE_URL = "petstore.swagger.io";
    private static String baseURI;
    private static int responseStatusCode;
    private static String responseBodyString;
    private static Response response;
    private static String responseBodyMessage;
    private static String statusLine;
    private static String responseHeaders;
    private static String contentType;
    private static String jsonSchemaPath;
    static Logger log = Logger.getLogger(PetStoreApi.class.getName());


    @Given("A GET request is made to get single pet with id: {string}")
    public void getPetById(String id) {
        Log.info("--- TEST CASE: GET Pet data with id: " + id + " ---");
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = baseURI;
        Log.info("Base URI set to: " + baseURI);

        response = given()
                // .log().all()
                .headers("accept", "application/json")
                .get();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();
        Log.info("Response status code: " + responseStatusCode);

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        if (responseStatusCode == 200) {
            // Set Json Schema Path
            jsonSchemaPath = "src/test/resources/schemas/GetPetId.json";
            Log.info("JSON Schema set to: " + jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A GET request is made to get single pet with id: {float}")
    public void getPetById(float id) {
        Log.info("--- TEST CASE: GET Pet data with id: " + id + " ---");
        // Set Base URI
        RestAssured.baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);

        response = given()
                // .log().all()
                .headers("accept", "application/json")
                .get();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        responseBodyMessage = jsonPathEvaluator.get("message");

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A GET request is made to get single pet with a valid id: {int}, but missing the request header")
    public void getPetByIdNoRequestHeader(int id) {
        Log.info("--- TEST CASE: GET Pet data with id: " + id + " but missing request header ---");
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = baseURI;

        response = given()
                .get();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        // Set Json Schema Path
        jsonSchemaPath = "src/test/resources/schemas/GetPetId.json";
    }

    @Given("A GET request is made to get a list of pets with status: {string}")
    public void getPetListByStatus(String status) {
        Log.info("--- TEST CASE: GET Pet list with status: " + status + " ---");
        // Set Base URI
        RestAssured.baseURI = String.format("https://%s/v2/pet/findByStatus?", PET_STORE_BASE_URL);

        response = given()
                .headers("accept", "application/json")
                .queryParam("status", status)
                .get();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet")
    public void postAddNewPet() {
        Log.info("--- TEST CASE: Add a new Pet ---");
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPet.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with a very long name")
    public void postAddNewPetLongName() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetLongName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with an empty name")
    public void postAddNewPetEmptyName() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetEmptyName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with a null name")
    public void postAddNewPetNullName() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetNullName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with no name")
    public void postAddNewPetNoName() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetNoName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A POST request is made to add a new pet with no id")
    public void postAddNewPetNoId() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetNoId.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A POST request is made to add a new pet with a null id")
    public void postAddNewPetNullId() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PostNewPetNullId.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A PUT request is made to update an existing pet")
    public void putUpdateExistingPet() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PutUpdateExistingPet.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .put();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Given("A PUT request is made to update an existing pet to a null status")
    public void putUpdateExistingPetNullStatus() {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/payloads/PutUpdatePetNullStatus.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .put();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }



    @Given("A POST request is made to update an existing pet entry with id: {string}, new name: {string} and new status {string}")
    public void putUpdateExistingPetFormData(String id, String name, String status) {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = baseURI;

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .queryParam("name", name)
                .queryParam("status", status)
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        if (responseStatusCode == 404) {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A POST request is made to upload a pet image for pet entry: {string}")
    public void uploadPetImage(String id) {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet/%s/uploadImage", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = baseURI;

        String requestPayload = "src/test/resources/others/dog.png";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "multipart/form-data")
                .multiPart( new File(requestPayload))
                .multiPart("additionalMetadata", "test image")
                .post();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().prettyPrint();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");

        if (responseStatusCode == 200) {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A DELETE request is made to delete a pet entry with id: {string}")
    public void deleteExistingPet(String id) {
        // Set Base URI
        baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = baseURI;

        response = given()
                .headers("accept", "application/json")
                .delete();

        // Get actual Response Code
        responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        responseBodyString = response.getBody().asString();

        // Get response status line
        statusLine = response.getStatusLine();

        // Get response headers
        responseHeaders= response.getHeaders().toString();

        // Get response content type
        contentType = response.header("Content-Type");
    }

    @Then("the response code is {int}")
    public void validateResponseCode(int expectedResponseCode) {
        // Validate the Response Code
        Log.info("Test Status Code: " + expectedResponseCode);
        assertEquals(expectedResponseCode,  responseStatusCode);
        Log.info("Test Status Code: PASS");
    }

    @Then("the response status line is {string}")
    public void validateResponseStatusLine(String responseStatusLine) {
        // Test: Response status line
        Log.info("Test Response Status Line: " + responseStatusLine);
        assertEquals(responseStatusLine, statusLine);
        Log.info("Test Response Status Line: PASS");
    }

    @Then("the response headers contain {string}")
    public void responseHeadersContain(String value) {
        // Test: Content-Type is present
        Log.info("Test Response Headers contain: " + value);
        assertTrue(responseHeaders.contains(value));
        Log.info("Test Response Headers: PASS");
    }

    @Then("the content type header equals {string}")
    public void responseHeaderEquals(String value) {
        // Test: content type value
        Log.info("Test Content Type Header equals: " + value);
        if (value.equalsIgnoreCase("null")) {
            value = null;
        }
        assertEquals(value, contentType);
        Log.info("Test Content Type Header: PASS");
    }

    @Then("the response body is not null")
    public void responseBodyNotNull() {
        // Test: Response body is not null
        Log.info("Test Response Body is not Null");
        assertNotNull(responseBodyString);
        Log.info("Test Response Body is not Null: PASS");
    }

    @Then("the response body is empty")
    public void responseBodyIsEmpty() {
        // Test: Response body is empty
        Log.info("Test Response Body is Empty");
        assertTrue(responseBodyString.isEmpty());
        Log.info("Test Response Body is Empty: PASS");
    }

    @Then("the json schema is correct")
    public void validateJsonSchema() {
        Log.info("Test JSON Schema: " + jsonSchemaPath);
        File schema = new File(jsonSchemaPath);
        given().
                get(baseURI).
                then().
                body(matchesJsonSchema(schema));
        Log.info("Test JSON Schema: PASS");
        Log.info("--- TEST CASE COMPLETED ---");
    }

    @Then("the response body message contains: {string}")
    public void validateResponseBody(String value) {
        Log.info("Test Response Body Message contains: " + value);
        assertTrue(responseBodyMessage.contains(value));
        Log.info("Test Response Body Message: PASS");
        Log.info("--- TEST CASE COMPLETED ---");
    }

    @Then("the list of pets is {string}")
    public void validateJsonArraySize(String status) {
        Log.info("Test List of Pets size: " + status);
        List<String> resIDs = response.jsonPath().get("id");
        Log.info("JsonArray Size: " + resIDs.size());
        if (status.equalsIgnoreCase("not empty")) {
            assertFalse(resIDs.isEmpty());
        } else {
            assertTrue(resIDs.isEmpty());
        }
        Log.info("Test List of Pets size: PASS");
        Log.info("--- TEST CASE COMPLETED ---");
    }

}
