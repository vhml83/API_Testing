package rest_assured;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class eCommerce {

    private final String baseURL = "webapi.segundamano.mx";
    private final String loginEmail = "user_test2023@mailinator.com";
    private final String password = "test_user_123";
    private static String accessToken;
    private static String accountId;
    private static String uuid;

    @DisplayName("Obtener Token")
    private String getAccessToken() {
        RestAssured.baseURI=String.format("https://%s/nga/api/v1.1/private/accounts", baseURL);

        Response response = given()
                .log().all()
                .queryParam("lang","es")
                .auth().preemptive().basic(loginEmail,password)
                .post();

        JsonPath jsonResponse = response.jsonPath();

        // Set accessToken variable
        this.accessToken = jsonResponse.get("access_token");
        // Set accountId variable
        this.accountId = jsonResponse.get("account.account_id");
        // Set uuid variable
        this.uuid = jsonResponse.get("account.uuid");

        return this.accessToken;
    }

    @DisplayName("Obtener addressID")
    private String getAddressID() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/create?", baseURL);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,accessToken)
                .formParam("contact","Raul Gomez")
                .formParam("phone","5588779944")
                .formParam("rfc","GOLR720425")
                .formParam("zipCode","56871")
                .formParam("exteriorInfo","Calle Zuno 2023")
                .formParam("interiorInfo","14")
                .formParam("region","9")
                .formParam("municipality","211")
                .formParam("area","68135")
                .formParam("alias","Oficina")
                .post();

        JsonPath jsonPathEvaluator = response.jsonPath();
        String addressID = jsonPathEvaluator.get("addressID");
        return addressID;
    }
    @DisplayName("Obtener AdID")
    private String getAdID() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up" , baseURL, uuid);

        String bodyRequest = "{" +
                "\"category\":\"8143\"," +
                "\"subject\":\"Avaluobienesraices\"," +
                "\"body\":\"Presupuestosincompromiso\"," +
                "\"region\":\"5\"," +
                "\"municipality\":\"86131\"," +
                "\"area\":\"86181\"," +
                "\"price\":\"9999\"," +
                "\"phone_hidden\":\"true\"," +
                "\"show_phone\":\"false\"," +
                "\"contact_phone\":\"6062575099\"}";

        Response response = given()
                .header("Content-type","application/json")
                .header("Accept","application/json, text/plain, */*")
                .header("x-source","PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid,accessToken)
                .body(bodyRequest)
                .post();

        JsonPath jsonPathEvaluator = response.jsonPath();
        String adID = jsonPathEvaluator.get("data.ad.ad_id");
        return adID;
    }

    @Test
    @Order(1)
    @DisplayName("Test case: GET /GetCategories - 200")
    public void getCategories_200() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/public/categories/filter?lang=es" , baseURL);

        Response response = given()
                .headers("Accept", "*/*")
                .queryParam("lang", "es")
                .get();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an id
        assertTrue(bodyResponse.contains("id"));
        // Test: Response body has categories
        assertTrue(bodyResponse.contains("categories"));
        // Test: Response body has all_label
        assertTrue(bodyResponse.contains("all_label"));
        // Test: All_label field equals "Todas de la categoría"
        JsonPath jsonPathEvaluator = response.jsonPath();
        String allLabel = jsonPathEvaluator.get("categories[0].all_label");
        allLabel =  StringUtils.stripAccents(allLabel);
        assertEquals(allLabel, "Todas de la categoria");
        // Test: Code field has value equal to '1020'
        String code = jsonPathEvaluator.get("categories[0].categories[0].code");
        assertEquals(code, "1020");
    }

    @Test
    @Order(2)
    @DisplayName("Test case: POST /CreateUser - 401")
    public void postCreateUser_401() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts?" , baseURL);

        // Create user
        String newUser = "agente_ventas" + (Math.floor(Math.random()*987)) + "@maillinator.com";
        String password = "12345";
        String bodyRequest = "{\"account\":{\"email\":\""+newUser+"\"}}";

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .auth().preemptive().basic(newUser, password)
                .body(bodyRequest)
                .post();

        // Validate the Response Code
        // Test: Response Code == 401
        assertEquals(401, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 401 UNAUTHORIZED"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 401 UNAUTHORIZED", "401 UNAUTHORIZED message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an error
        assertTrue(bodyResponse.contains("error"));
        // Test: Response body has a code
        assertTrue(bodyResponse.contains("code"));
        // Test: Error code message ACCOUNT_VERIFICATION_REQUIRED
        assertTrue(bodyResponse.contains("ACCOUNT_VERIFICATION_REQUIRED"));
    }

    @Test
    @Order(3)
    @DisplayName("Test case: POST /LoginUser - 200")
    public void postLoginUser_200() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts?", baseURL);

        String bodyRequest = "{\"account\":{\"email\":\"" + loginEmail + "\"}}";

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .auth().preemptive().basic(loginEmail, password)
                .body(bodyRequest)
                .post();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an Access Token
        assertTrue(bodyResponse.contains("access_token"));
        // Test: Response body has a valid email
        String responseEmail = jsonPathEvaluator.get("account.email");
        assertEquals(responseEmail, this.loginEmail);
    }

    @Test
    @Order(4)
    @DisplayName("Test case: POST /LoginUser with incorrect credentials - 401")
    public void postLoginUser_401() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts?" , baseURL);
        String bodyRequest = "{\"account\":{\"email\":\""+loginEmail+"\"}}";
        String incorrectPassword = "test123Abc";
        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .auth().preemptive().basic(loginEmail, incorrectPassword)
                .body(bodyRequest)
                .post();

        // Validate the Response Code
        // Test: Response Code == 401
        assertEquals(401, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 401 UNAUTHORIZED"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 401 UNAUTHORIZED", "401 UNAUTHORIZED message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an Access Token
        assertTrue(bodyResponse.contains("error"));
        // Test: Response body has a Error code == LOGIN_FAILED
        String responseErrorCode = jsonPathEvaluator.get("error.code");
        assertEquals(responseErrorCode, "LOGIN_FAILED");
    }

    @Test
    @Order(5)
    @DisplayName("Test case: POST /GetListsByFilter - 200")
    public void postGetListsByFilter_200() {
        RestAssured.baseURI = String.format("https://%s/urls/v1/public/ad-listing?", baseURL);

        String bodyRequest = "{\"filters\":[{\"price\":\"-60000\",\"category\":\"2020\"}," +
                "{\"price\":\"60000-80000\",\"category\":\"2020\"}," +
                "{\"price\":\"80000-100000\",\"category\":\"2020\"}," +
                "{\"price\":\"100000-150000\",\"category\":\"2020\"}," +
                "{\"price\":\"150000-\",\"category\":\"2020\"}]}";

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .body(bodyRequest)
                .post();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has a list of urls
        assertTrue(bodyResponse.contains("urls"));
    }

    @Test
    @Order(6)
    @DisplayName("Test case: GET /GetUserData - 200")
    public void getUserData_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s?lang=es" , baseURL, accountId);

        Response response = given()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .headers("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .get();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an account_id
        assertTrue(bodyResponse.contains("account_id"));
        // Test: Response body has an email
        assertTrue(bodyResponse.contains("email"));
        // Test: Response body has a valid email
        String responseEmail = jsonPathEvaluator.get("account.email");
        assertEquals(responseEmail, loginEmail);
    }

    @Test
    @Order(7)
    @DisplayName("Test case: PATCH /UpdateUserData - 200")
    public void patchUpdateUserData_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s?lang=es" , baseURL, accountId);

        String bodyRequest = "{\"account\":" +
                "{\"name\":\"Leopoldo\"," +
                "\"phone\":\"3312589400\"," +
                "\"locations\":[{\"code\":\"5\"," +
                "\"key\":\"region\"," +
                "\"label\":\"BajaCaliforniaSur\"," +
                "\"locations\":[{\"code\":\"86131\"," +
                "\"key\":\"municipality\"," +
                "\"label\":\"Loreto\"}]}]," +
                "\"professional\":false," +
                "\"phone_hidden\":false}}";

        Response response = given()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .headers("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .body(bodyRequest)
                .patch();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an account_id
        assertTrue(bodyResponse.contains("account_id"));
        // Test: Response body has an email
        assertTrue(bodyResponse.contains("email"));
        // Test: Response body has a valid email
        String responseEmail = jsonPathEvaluator.get("account.email");
        assertEquals(responseEmail, loginEmail);
        // Test: Response body has an updated name
        String responseName = jsonPathEvaluator.get("account.name");
        assertEquals(responseName, "Leopoldo");
        // Test: Response body has an updated phone number
        String responsePhoneNumber = jsonPathEvaluator.get("account.phone");
        assertEquals(responsePhoneNumber, "3312589400");
    }

    @Test
    @Order(8)
    @DisplayName("Test case: PATCH /UpdatePhoneNumber - 200")
    public void patchUpdatePhoneNumber_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s?lang=es" , baseURL, accountId);
        // Random 10 digit phone number
        long randPhone = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        String bodyRequest = "{\"account\":" +
                "{\"name\":\"Leopoldo\"," +
                "\"phone\":\""+randPhone+"\"," +
                "\"locations\":[{\"code\":\"5\"," +
                "\"key\":\"region\"," +
                "\"label\":\"BajaCaliforniaSur\"," +
                "\"locations\":[{\"code\":\"86131\"," +
                "\"key\":\"municipality\"," +
                "\"label\":\"Loreto\"}]}]," +
                "\"professional\":false," +
                "\"phone_hidden\":false}}";

        Response response = given()
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .headers("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .body(bodyRequest)
                .patch();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an account_id
        assertTrue(bodyResponse.contains("account_id"));
        // Test: Response body has an email
        assertTrue(bodyResponse.contains("email"));
        // Test: Response body has a valid email
        String responseEmail = jsonPathEvaluator.get("account.email");
        assertEquals(responseEmail, loginEmail);
        // Test: Response body has an updated phone number
        String responsePhoneNumber = jsonPathEvaluator.get("account.phone");
        assertEquals(responsePhoneNumber, Long.toString(randPhone));
    }

    @Test
    @Order(9)
    @DisplayName("Test case: POST /CreateAddress - 201")
    public void postCreateAddress_201() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/create?", baseURL);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,accessToken)
                .formParam("contact","Raul Gomez")
                .formParam("phone","5588779944")
                .formParam("rfc","GOLR720425")
                .formParam("zipCode","56871")
                .formParam("exteriorInfo","Calle Zuno 2023")
                .formParam("interiorInfo","14")
                .formParam("region","9")
                .formParam("municipality","211")
                .formParam("area","68135")
                .formParam("alias","Oficina")
                .post();

        // Validate the Response Code
        // Test: Response Code == 201
        assertEquals(201, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 201 Created"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 201 Created", "201 Created message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an Address ID
        assertTrue(bodyResponse.contains("addressID"));
    }

    @Test
    @Order(10)
    @DisplayName("Test case: PUT /UpdateAddress - 200")
    public void putUpdateAddress_200() {
        String accessToken = getAccessToken();
        String addressID = getAddressID();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/modify/%s", baseURL, addressID);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,accessToken)
                .formParam("contact","Raul Gomez")
                .formParam("phone","5588669944")
                .formParam("rfc","GOLR720425")
                .formParam("zipCode","56872")
                .formParam("exteriorInfo","Calle Zuno 2023")
                .formParam("interiorInfo","14")
                .formParam("region","9")
                .formParam("municipality","211")
                .formParam("area","68135")
                .formParam("alias","Oficina")
                .put();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has a message
        assertTrue(bodyResponse.contains("message"));
        // Test: Response body has a valid
        String responseMessage = jsonPathEvaluator.get("message");
        assertEquals(responseMessage, addressID + " modified correctly");
    }

    @Test
    @Order(11)
    @DisplayName("Test case: DELETE /DeleteAddress - 200")
    public void deleteDeleteAddress_200() {
        String accessToken = getAccessToken();
        String addressID = getAddressID();
        RestAssured.baseURI = String.format("https://%s/addresses/v1/delete/%s", baseURL, addressID);

        Response response = given()
                .log().all()
                .header("Accept","*/*")
                .auth().preemptive().basic(uuid,accessToken)
                .delete();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has a message
        assertTrue(bodyResponse.contains("message"));
        // Test: Response body has a valid
        String responseMessage = jsonPathEvaluator.get("message");
        assertEquals(responseMessage, addressID + " deleted correctly");
    }

    @Test
    @Order(12)
    @DisplayName("Test case: GET /GetCounter - 200")
    public void getGetCounter_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/api/users/%s/counter?lang=es" , baseURL, uuid);

        Response response = given()
                .queryParam("lang", "es")
                .accept("application/json, text/plain, */*")
                .headers("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .get();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an 'unread' field
        assertTrue(bodyResponse.contains("unread"));
    }

    @Test
    @Order(13)
    @DisplayName("Test case: POST /CreateAdd - 200")
    public void postCreateAdd_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up" , baseURL, uuid);

        String bodyRequest = "{" +
                "\"category\":\"8143\"," +
                "\"subject\":\"Avaluobienesraices\"," +
                "\"body\":\"Presupuestosincompromiso\"," +
                "\"region\":\"5\"," +
                "\"municipality\":\"86131\"," +
                "\"area\":\"86181\"," +
                "\"price\":\"9999\"," +
                "\"phone_hidden\":\"true\"," +
                "\"show_phone\":\"false\"," +
                "\"contact_phone\":\"6062575099\"}";

        Response response = given()
                .header("Content-type","application/json")
                .header("Accept","application/json, text/plain, */*")
                .header("x-source","PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid,accessToken)
                .body(bodyRequest)
                .post();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an ad_id
        assertTrue(bodyResponse.contains("ad_id"));
    }

    @Test
    @Order(14)
    @DisplayName("Test case: PUT /EditAdd - 200")
    public void putEditAdd_200() {
        String accessToken = getAccessToken();
        String adID = getAdID();
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up/%s" , baseURL, uuid, adID);

        // Random 10 digit phone number
        long randPhone = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        String bodyRequest = "{" +
                "\"category\":\"8143\"," +
                "\"subject\":\"Avaluobienesraices\"," +
                "\"body\":\"Presupuestosincompromiso.Llamenos\"," +
                "\"region\":\"5\"," +
                "\"municipality\":\"86131\"," +
                "\"area\":\"86181\"," +
                "\"price\":\"9999\"," +
                "\"phone_hidden\":\"true\"," +
                "\"show_phone\":\"false\"," +
                "\"contact_phone\":\""+randPhone+"\"}";

        Response response = given()
                .header("Content-type","application/json")
                .header("Accept","application/json, text/plain, */*")
                .header("x-source","PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid,accessToken)
                .body(bodyRequest)
                .put();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an ad_id
        assertTrue(bodyResponse.contains("ad_id"));
        // Test: Response body has a valid ad_id
        String responseAdId = jsonPathEvaluator.get("data.ad.ad_id");
        assertEquals(responseAdId, adID);
    }
    @Test
    @Order(15)
    @DisplayName("Test case: DELETE /DeleteAdd - 403 Anuncio no indexado")
    public void deleteDeleteAdd_403() {
        String accessToken = getAccessToken();
        String adID = getAdID();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s/klsft/%s" , baseURL, accountId, adID);

        String bodyRequest = "{\"delete_reason\":{\"code\":\"0\"}}";

        Response response = given()
                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .header("Content-type","application/json")
                .header("Connection", "keep-alive")
                .body(bodyRequest)
                .delete();

        // Validate the Response Code
        // Test: Response Code == 403
        assertEquals(403, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 403 FORBIDDEN"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 403 FORBIDDEN", "403 FORBIDDEN message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
    }
    @Test
    @Order(16)
    @DisplayName("Test case: GET /GetPendingAdds - 200")
    public void getGetPendingAdds_200() {
        String accessToken = getAccessToken();
        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s/klfst?", baseURL, accountId);

        Response response = given()
                .accept("application/json, text/plain, */*")
                .queryParam("status", "pending")
                .queryParam("lim", "20")
                .queryParam("o", "0")
                .queryParam("query", "")
                .queryParam("lang", "es")
                .header("Authorization", "tag:scmcoord.com,2013:api " + accessToken)
                .get();

        // Validate the Response Code
        // Test: Response Code == 200
        assertEquals(200, response.getStatusCode(), "Correct status code returned");
        // Test: Response status line is "HTTP/1.1 200 OK"
        String statusLine = response.getStatusLine();
        assertEquals(statusLine, "HTTP/1.1 200 OK", "200 OK message is returned");

        // Validate Headers Response
        String headersResponse = response.getHeaders().toString();
        // Test: Content-Type is present
        assertTrue(headersResponse.contains("Content-Type"));
        // Test: Content-Type header equals "application/json charset=utf-8"
        String contentType = response.header("Content-Type");
        assertEquals(contentType, "application/json; charset=utf-8");

        // Validate the Response Body
        String bodyResponse = response.getBody().prettyPrint();
        // Test: Response body is not empty
        assertNotNull(bodyResponse);
        // Test: Response body has an 'counter_map' field
        assertTrue(bodyResponse.contains("counter_map"));
    }


}
