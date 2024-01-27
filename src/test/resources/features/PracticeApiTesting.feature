@PracticeApiTesting
Feature: Practice Api Testing Automation

  @Health-Check
  Scenario: Check if the server is running and healthy
    Given A GET request is made to check if the server is running and healthy
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json; charset=utf-8"
    Then the response body is not null
    Then the json schema is correct

  @User-Login
  Scenario: Authenticate a user and return access token
    Given A POST request is made to authenticate a user and return access token
