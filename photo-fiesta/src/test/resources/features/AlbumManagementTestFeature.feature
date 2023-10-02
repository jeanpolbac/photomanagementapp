Feature: Rest API functionalities

  Scenario: Able to manage all or individual albums
    Given A logged in user
    And A list of albums are available
    When I add a album to my list
    Then The album is added
#    When I delete an album in my list
#    Then The album is removed
    When I view the photos in my album
    Then I see a list of photos
    When I view a single photo in my album
    Then I see a single photo