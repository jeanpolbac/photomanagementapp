Feature: Rest API functionalities

  Scenario: Able to manage all or individual albums
    Given A logged in user
    And A list of albums are available
    When I add a album to my list
    Then The album is added
    When I view a single photo in my album
    Then I see a single photo
    When I view the photos in my album
    Then I see a list of photos
    When I add a new photo
    Then The photo is added to my default album
    When I update a photo
    Then The photo is updated
    When I delete a photo from an album
    Then The photo is deleted
    When I delete an album in my list
    Then The album is removed

