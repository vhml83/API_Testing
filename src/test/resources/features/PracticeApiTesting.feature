@PracticeApiTesting
Feature: Practice Api Testing Automation

  Background: Authenticated user is able to login
    Given The server is up and running
    And the user is able to authenticate

  Scenario: Returns the profile information for the logged-in current user.
    When A GET request is made to retrieve user profile information
    Then the response code is 200
    Then the response body for user profile contains message: "Profile successful"
    Then the response body for user profile contains the registered user id
    Then the response body for user profile contains the registered user name
    Then the response body for user profile contains the registered user email
