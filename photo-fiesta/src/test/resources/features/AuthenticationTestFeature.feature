Feature: Rest API functionalities

  Scenario: User able to access public endpoints
    Given A valid public endpoint
    When I say hello
    Then Hello is shown

  Scenario: User registration
    Given The register url is "/auth/users/register/"
    When User sends a POST request with user details
    Then The response status should be ok
    And The response should contain the user details

  Scenario: User able to login and receive jwt token
    Given The registered user exists
    When The user inputs their email address and password
    Then The user receives a jwt token

