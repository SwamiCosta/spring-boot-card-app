package com.example.cardapp.service;

import com.example.cardapp.security.AuthUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for generating and validating time-based authentication tokens.
 * Tokens are generated based on user credentials and the current hour/day,
 * ensuring they are valid for a specific time window.
 */
@Service
public class TokenService {

    @Value("${auth.credentials.prepaid.userId}")
    private String prepaidUserId;
    @Value("${auth.credentials.prepaid.password}")
    private String prepaidPassword;

    @Value("${auth.credentials.limited.userId}")
    private String limitedUserId;
    @Value("${auth.credentials.limited.password}")
    private String limitedPassword;

    private final Map<String, AuthUser.Role> validCredentials = new HashMap<>();

    @jakarta.annotation.PostConstruct
    public void init() {
        validCredentials.put(prepaidUserId + ":" + prepaidPassword, AuthUser.Role.PREPAID_ONLY);
        validCredentials.put(limitedUserId + ":" + limitedPassword, AuthUser.Role.LIMITED_USE_ONLY);
    }

    /**
     * Authenticates a user based on provided credentials and generates a time-based token.
     *
     * @param userId The user's ID.
     * @param password The user's password.
     * @return The generated token string if credentials are valid.
     * @throws IllegalArgumentException if credentials do not match any known user.
     */
    public String generateToken(String userId, String password) {
        AuthUser.Role role = validCredentials.get(userId + ":" + password);
        if (role == null) {
            throw new IllegalArgumentException("Invalid userId or password.");
        }
        return buildTimeBasedToken(userId, password, role, LocalDateTime.now());
    }

    /**
     * Validates a given time-based token.
     * The token is considered valid if it matches a hash generated for the current hour
     * or the previous hour (to handle requests spanning hour boundaries).
     *
     * @param token The token string to validate.
     * @return An Optional containing the AuthUser if the token is valid, empty otherwise.
     */
    public Optional<AuthUser> validateTimeBasedToken(String token) {
        LocalDateTime now = LocalDateTime.now();

        Optional<AuthUser> user = tryValidateTokenForTime(token, now);
        if (user.isPresent()) {
            return user;
        }

        LocalDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        user = tryValidateTokenForTime(token, oneHourAgo);
        if (user.isPresent()) {
            return user;
        }

        return Optional.empty();
    }

    /**
     * Attempts to validate a token against a hash generated for a specific time.
     *
     * @param token The token to validate.
     * @param dateTime The LocalDateTime to use for hash generation.
     * @return An Optional containing the AuthUser if the token matches, empty otherwise.
     */
    private Optional<AuthUser> tryValidateTokenForTime(String token, LocalDateTime dateTime) {
        for (Map.Entry<String, AuthUser.Role> entry : validCredentials.entrySet()) {
            String[] credentials = entry.getKey().split(":");
            String userId = credentials[0];
            String password = credentials[1];
            AuthUser.Role role = entry.getValue();

            String expectedToken = buildTimeBasedToken(userId, password, role, dateTime);
            if (expectedToken.equals(token)) {
                return Optional.of(new AuthUser(userId, role));
            }
        }
        return Optional.empty();
    }

    /**
     * Builds a time-based token by hashing user credentials, current hour, and day of the year.
     *
     * @param userId The user's ID.
     * @param password The user's password.
     * @param role The user's role.
     * @param dateTime The LocalDateTime to base the token on.
     * @return A Base64 encoded SHA-256 hash representing the token.
     */
    private String buildTimeBasedToken(String userId, String password, AuthUser.Role role, LocalDateTime dateTime) {
        LocalDateTime normalizedTime = dateTime.truncatedTo(ChronoUnit.HOURS);
        int hourOfDay = normalizedTime.getHour();
        int dayOfYear = normalizedTime.getDayOfYear();
        int year = normalizedTime.getYear();

        String dataToHash = String.format("%s:%s:%s:%d:%d:%d", userId, password, role.name(), hourOfDay, dayOfYear, year);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}