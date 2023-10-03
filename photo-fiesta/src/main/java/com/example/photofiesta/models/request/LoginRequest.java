package com.example.photofiesta.models.request;

/**
 * A class representing a login request, containing the user's email address and password.
 */
public class LoginRequest {
    private String emailAddress;

    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
