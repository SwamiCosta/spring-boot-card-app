package com.example.cardapp.security;

/**
 * Represents an authenticated user with their ID and assigned role.
 */
public class AuthUser {
    private String userId;
    private Role role;

    public AuthUser(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Defines the roles available in the simulated authentication system.
     */
    public enum Role {
        PREPAID_ONLY,
        LIMITED_USE_ONLY
    }
}