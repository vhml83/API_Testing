package com.cucumber.steps;

import io.cucumber.java.en.Then;
import logger.Log;
import java.io.File;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ApiTests {

    protected static String baseURI;
    protected static int responseStatusCode;
    protected static String statusLine;
    protected static String responseHeaders;
    protected static String contentType;
    protected static String responseBodyString;
    protected static String responseBodyMessage;
    protected static String jsonSchemaPath;

    @Then("the response code is {int}")
    public void validateResponseCode(int expectedResponseCode) {
        // Validate the Response Code
        Log.info("Test Status Code: " + expectedResponseCode);
        assertEquals(expectedResponseCode,  responseStatusCode);
    }

    @Then("the response status line is {string}")
    public void validateResponseStatusLine(String responseStatusLine) {
        // Test: Response status line
        Log.info("Test Response Status Line: " + responseStatusLine);
        assertEquals(responseStatusLine, statusLine);
    }

    @Then("the response headers contain {string}")
    public void responseHeadersContain(String value) {
        // Test: Content-Type is present
        Log.info("Test Response Headers contain: " + value);
        assertTrue(responseHeaders.contains(value));
    }

    @Then("the content type header equals {string}")
    public void responseHeaderEquals(String value) {
        // Test: content type value
        Log.info("Test Content Type Header equals: " + value);
        if (value.equalsIgnoreCase("null")) {
            value = null;
        }
        assertEquals(value, contentType);
    }

    @Then("the response body is not null")
    public void responseBodyNotNull() {
        // Test: Response body is not null
        Log.info("Test Response Body is not Null");
        assertNotNull(responseBodyString);
    }

    @Then("the json schema is correct")
    public void validateJsonSchema() {
        Log.info("Test JSON Schema: " + jsonSchemaPath);
        File schema = new File(jsonSchemaPath);
        given().
                get(baseURI).
                then().
                body(matchesJsonSchema(schema));
    }

    @Then("the response body message contains: {string}")
    public void validateResponseBody(String value) {
        Log.info("Test Response Body Message contains: " + value);
        assertTrue(responseBodyMessage.contains(value));
    }

    @Then("the response body is empty")
    public void responseBodyIsEmpty() {
        // Test: Response body is empty
        Log.info("Test Response Body is Empty");
        assertTrue(ApiTests.responseBodyString.isEmpty());
    }
}
