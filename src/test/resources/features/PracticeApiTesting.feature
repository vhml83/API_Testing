@PracticeApiTesting
Feature: Practice Api Testing Automation

  Background: Authenticated user is able to login
    Given The server is up and running
    And the user is able to authenticate

  Scenario: Returns the profile information for the logged-in current user.
    When A GET request is made to retrieve user profile information
    Then the response code is 200
    Then the response body is not null
