@PETSTORE
Feature: Test the Pet Store REST API

  @PET
  Scenario: GET a pet's data with a valid id
    Given A GET request is made to get single pet with id: "5"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the json schema is correct

  @PET @NEGATIVE
  Scenario: GET a pet's data with an invalid id
    Given A GET request is made to get single pet with id: "5ABC"
    Then the response code is 404
    Then the response status line is "HTTP/1.1 404 Not Found"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "java.lang.NumberFormatException"

  @PET @NEGATIVE
  Scenario: GET a pet's data with an invalid id
    Given A GET request is made to get single pet with id: 5.0
    Then the response code is 404
    Then the response status line is "HTTP/1.1 404 Not Found"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "java.lang.NumberFormatException"

  @PET @NEGATIVE
  Scenario: GET a pet's data with a valid id but missing request header
    Given A GET request is made to get single pet with a valid id: 5, but missing the request header
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"

  @PET
  Scenario: GET a list of pets by status available
    Given A GET request is made to get a list of pets with status: "available"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the list of pets is "not empty"

  @PET
  Scenario: GET a list of pets by status pending
    Given A GET request is made to get a list of pets with status: "pending"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the list of pets is "not empty"

  @PET
  Scenario: GET a list of pets by status sold
    Given A GET request is made to get a list of pets with status: "sold"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the list of pets is "not empty"

  @PET @NEGATIVE
  Scenario: GET a list of pets by invalid status
    Given A GET request is made to get a list of pets with status: "ABC123"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the list of pets is "empty"

  @PET
  Scenario: POST a new pet entry
    Given A POST request is made to add a new pet
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: POST a new pet entry with a very long name
    Given A POST request is made to add a new pet with a very long name
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: POST a new pet entry with an empty name
    Given A POST request is made to add a new pet with an empty name
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: POST a new pet entry with a null name
    Given A POST request is made to add a new pet with a null name
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: POST a new pet entry with no name
    Given A POST request is made to add a new pet with no name
    Then the response code is 500
    Then the response status line is "HTTP/1.1 500 Server Error"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "something bad happened"

  @PET @NEGATIVE
  Scenario: POST a new pet entry with no id
    Given A POST request is made to add a new pet with no id
    Then the response code is 400
    Then the response status line is "HTTP/1.1 400 Bad Request"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "bad input"

  @PET @NEGATIVE
  Scenario: POST a new pet entry with null id
    Given A POST request is made to add a new pet with a null id
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET
  Scenario: UPDATE an existing pet entry to a valid status
    Given A PUT request is made to update an existing pet
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: UPDATE an existing pet entry to null status
    Given A PUT request is made to update an existing pet to a null status
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET
  Scenario: UPDATE an existing pet entry with form data
    Given A POST request is made to update an existing pet entry with id: "777", new name: "Pluto" and new status "pending"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: UPDATE an existing pet entry with form data using invalid id
    Given A POST request is made to update an existing pet entry with id: "7ABC", new name: "Pluto" and new status "pending"
    Then the response code is 404
    Then the response status line is "HTTP/1.1 404 Not Found"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "java.lang.NumberFormatException"

  @PET
  Scenario: UPDATE uploads a new image
    Given A POST request is made to upload a pet image for pet entry: "777"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "File uploaded to"

  @PET
  Scenario: DELETE an existing pet entry
    Given A DELETE request is made to delete a pet entry with id: "777"
    Then the response code is 200
    Then the response status line is "HTTP/1.1 200 OK"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null

  @PET @NEGATIVE
  Scenario: DELETE a pet entry that has been deleted
    Given A DELETE request is made to delete a pet entry with id: "777"
    Then the response code is 404
    Then the response status line is "HTTP/1.1 404 Not Found"
    Then the response headers contain "Content-Type"
    Then the content type header equals "null"
    Then the response body is empty

  @PET @NEGATIVE
  Scenario: GET a pet's data of a non existing pet entry
    Given A GET request is made to get single pet with id: "777"
    Then the response code is 404
    Then the response status line is "HTTP/1.1 404 Not Found"
    Then the response headers contain "Content-Type"
    Then the content type header equals "application/json"
    Then the response body is not null
    Then the response body message contains: "Pet not found"


