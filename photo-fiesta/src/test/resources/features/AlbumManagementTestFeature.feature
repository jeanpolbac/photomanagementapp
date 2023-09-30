Feature: Rest API functionalities

  Scenario: Able to manage all or individual albums
    Given A list of albums are available
    When I add a album to my list
    Then The album is added
    When I delete an album in my list
    Then The album is removed