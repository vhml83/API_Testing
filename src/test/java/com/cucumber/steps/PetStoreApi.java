package com.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import logger.Log;
import java.io.File;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PetStoreApi {

    private static final String PET_STORE_BASE_URL = "petstore.swagger.io";
    private static Response response;

    @Given("A GET request is made to get single pet with id: {string}")
    public void getPetById(String id) {
        Log.info("--- TEST CASE: GET Pet data with id: " + id + " ---");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
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
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/petstore/GetPetId.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A GET request is made to get single pet with a valid id: {int}, but missing the request header")
    public void getPetByIdNoRequestHeader(int id) {
        Log.info("--- TEST CASE: GET Pet data with id: " + id + " but missing request header ---");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = ApiTests.baseURI;

        response = given()
                .get();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        // Set Json Schema Path
        ApiTests.jsonSchemaPath = "src/test/resources/schemas/petstore/GetPetId.json";
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
        ApiTests.responseStatusCode = response.getStatusCode();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet")
    public void postAddNewPet() {
        Log.info("--- TEST CASE: Add a new Pet ---");
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPet.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with a very long name")
    public void postAddNewPetLongName() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetLongName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with an empty name")
    public void postAddNewPetEmptyName() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetEmptyName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with a null name")
    public void postAddNewPetNullName() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetNullName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to add a new pet with no name")
    public void postAddNewPetNoName() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetNoName.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A POST request is made to add a new pet with no id")
    public void postAddNewPetNoId() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetNoId.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A POST request is made to add a new pet with a null id")
    public void postAddNewPetNullId() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PostNewPetNullId.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        // Get the response body message
        JsonPath jsonPathEvaluator = response.jsonPath();
        ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
    }

    @Given("A PUT request is made to update an existing pet")
    public void putUpdateExistingPet() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PutUpdateExistingPet.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .put();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A PUT request is made to update an existing pet to a null status")
    public void putUpdateExistingPetNullStatus() {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet", PET_STORE_BASE_URL);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/payloads/petstore/PutUpdatePetNullStatus.json";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/json")
                .body(new File(requestPayload))
                .put();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A POST request is made to update an existing pet entry with id: {string}, new name: {string} and new status {string}")
    public void putUpdateExistingPetFormData(String id, String name, String status) {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = ApiTests.baseURI;

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .queryParam("name", name)
                .queryParam("status", status)
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        if (ApiTests.responseStatusCode == 404) {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A POST request is made to upload a pet image for pet entry: {string}")
    public void uploadPetImage(String id) {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet/%s/uploadImage", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = ApiTests.baseURI;

        String requestPayload = "src/test/resources/others/dog.png";

        response = given()
                .headers("accept", "application/json")
                .headers("Content-Type", "multipart/form-data")
                .multiPart( new File(requestPayload))
                .multiPart("additionalMetadata", "test image")
                .post();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().prettyPrint();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");

        if (ApiTests.responseStatusCode == 200) {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A DELETE request is made to delete a pet entry with id: {string}")
    public void deleteExistingPet(String id) {
        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/pet/%s", PET_STORE_BASE_URL, id);
        RestAssured.baseURI = ApiTests.baseURI;

        response = given()
                .headers("accept", "application/json")
                .delete();

        // Get actual Response Code
        ApiTests.responseStatusCode = response.getStatusCode();

        // Convert the Response Body to String format
        ApiTests.responseBodyString = response.getBody().asString();

        // Get response status line
        ApiTests.statusLine = response.getStatusLine();

        // Get response headers
        ApiTests.responseHeaders= response.getHeaders().toString();

        // Get response content type
        ApiTests.contentType = response.header("Content-Type");
    }

    @Given("A GET request is made to get the pet store inventory")
    public void getPetStoreInventory() {
        Log.info("--- TEST CASE: GET Pet Store Inventory ---");

        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/store/inventory", PET_STORE_BASE_URL);
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
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/petstore/GetPetStoreInventory.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A GET request is made to get the purchase order with id: {string}")
    public void getPetStoreInventory(String orderId) {
        Log.info("--- TEST CASE: GET Purchase Order with a Valid Id: '" + orderId + "' ---");

        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/store/order/%s", PET_STORE_BASE_URL, orderId);
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
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/petstore/GetPetStoreOrder.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
    }

    @Given("A DELETE request is made to delete a purchase order by id: {string}")
    public void deletePurchaseOrder(String orderId) {
        Log.info("--- TEST CASE: DELETE Purchase Order by Id: '" + orderId + "' ---");

        // Set Base URI
        ApiTests.baseURI = String.format("https://%s/v2/store/order/%s", PET_STORE_BASE_URL, orderId);
        RestAssured.baseURI = ApiTests.baseURI;
        Log.info("Base URI set to: " + ApiTests.baseURI);

        response = given()
                // .log().all()
                .headers("accept", "application/json")
                .delete();

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
            ApiTests.jsonSchemaPath = "src/test/resources/schemas/petstore/DeletePurchaseOrder.json";
            Log.info("JSON Schema set to: " + ApiTests.jsonSchemaPath);
        } else {
            // Get the response body message
            JsonPath jsonPathEvaluator = response.jsonPath();
            ApiTests.responseBodyMessage = jsonPathEvaluator.get("message");
        }
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
    }

}
