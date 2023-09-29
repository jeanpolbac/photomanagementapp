# Photo Fiesta Application
## Project Description
Photo Fiesta is a monolithic backend developed using Spring Boot that provides a RESTful API which allows users to upload photos and organize them into albums. Users can register and login to take advantage of the photo managament system. <br> <br>
**Status**: :construction: Under Development :construction:
## User Stories
<details>
  <summary>User Registration and Login</summary>
  <strong>User Story</strong>: As a new user, I want to be able to register for an account with an email and password. <br>
  <strong>Acceptance Criteria</strong>: Given I am a new user, when I register, then my account is created. <br> <br>
  <strong>User Story</strong>: As a registered user, I want to be able to login to my account securely. <br> 
  <strong>Acceptance Criteria</strong>: Given I am a registered user, when I login with my correct email and password, then I have access to the application features. <br> <br>
</details>
<details>
  <summary>User Album Management</summary>
  <strong>User Story</strong>: As a logged in user, I want to create a new album so that I can organize my photos. <br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I create a new album, then it is added to my photo album collection. <br> <br>
  <strong>User Story</strong>: As a logged in user, I want to edit the title and description of an album so that I can modify it. <br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I edit the title and description of an album, then the album is updated. <br> <br>
  <strong>User Story</strong>: As a logged in user, I want to delete an album that is no longer needed. <br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I delete an album, then the album is deleted. <br> <br>
  <strong>User Story</strong>: As a logged in user, I want to view one or  all of my albums. <br>
  <strong>Acceptance Criteria</strong>: Given a list of albums is available, when I want to view my albums, then the list of albums is displayed. <br>
  <strong>Acceptance Criteria</strong>: Given a list of albums is available, when I want to view a specific album, then the correct album is displayed. <br> <br>
</details>
<details>
  <summary>User Photo Management</summary>
  <strong>User Story</strong>: As a logged in user, I want to be able to add a photo to an album.<br>
  <strong>Acceptance Criteria</strong>: Given I am logged in user, when I add a photo to an album, then the photo is added. <br><br>
  <strong>User Story</strong>: As a logged in user, I want to be able to delete a photo from an album.<br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I delete a photo from an album, then the photo is deleted. <br><br>
  <strong>User Story</strong>: As a logged in user, I want to be able to update the album that the photo is in. <br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I update a photo from an album, then the photo is updated. <br><br>
  <strong>User Story</strong>: As a logged in user, I want to edit the title and description of a photo so that I can modify it. <br>
  <strong>Acceptance Criteria</strong>: Given I am a logged in user, when I edit  the title and description of a photo, then the photo is updated. <br><br>
  <strong>User Story</strong>: As a logged in user, I want to view one or all photos in an album.<br>
  <strong>Acceptance Criteria</strong>: Given a list of photos is available in the specified album, when I want to view all photos, then all of the photos are displayed<br>
  <strong>Acceptance Criteria</strong>: Given a list of photos is available in the specified album, when I want to view a specific photo, then the correct photo is displayed.<br> <br>
</details>

## ERD Diagram
![ERD Diagram Image](./erd-diagram-correct.png)

## REST API Endpoints
| Endpoint     | Request Type | URL                            | Functionality                                      | Access              |
|--------------|--------------|--------------------------------|----------------------------------------------------|---------------------|
| Register     | POST         | `/auth/users/register/`         | Registers a new user with the provided user data. | Public              |

