Feature: Rest API functionalities
# ToDO Complete based on necessary scenarios
  Scenario: User able to access public endpoints
    Given A valid public endpoint
    When I say hello
    Then Hello is shown