package com.example.cardapp.security;

import com.example.cardapp.model.CardProductType;

/**
 * A ThreadLocal context to store the authenticated user's information.
 * This allows access to the user's permissions throughout the request lifecycle.
 */
public class AuthContext {
    private static final ThreadLocal<AuthUser> currentUser = new ThreadLocal<>();

    /**
     * Sets the authenticated user for the current thread.
     * @param user The AuthUser object representing the authenticated user.
     */
    public static void setCurrentUser(AuthUser user) {
        currentUser.set(user);
    }

    /**
     * Retrieves the authenticated user for the current thread.
     * @return The AuthUser object, or null if no user is authenticated.
     */
    public static AuthUser getCurrentUser() {
        return currentUser.get();
    }

    /**
     * Clears the authenticated user from the current thread's context.
     * Important to prevent memory leaks in thread pools.
     */
    public static void clear() {
        currentUser.remove();
    }
}