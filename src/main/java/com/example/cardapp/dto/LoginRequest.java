package com.example.cardapp.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * DTO for the login request payload.
 * Contains the userId and password for authentication.
 */
public class LoginRequest {

    @NotBlank(message = "User ID cannot be empty")
    private String userId;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    // Constructors
    public LoginRequest() {
    }

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // hashCode, equals, toString for good practice
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}