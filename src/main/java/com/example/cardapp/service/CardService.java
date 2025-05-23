package com.example.cardapp.service;

import com.example.cardapp.dto.CardCreationRequest;
import com.example.cardapp.dto.CardResponse;
import com.example.cardapp.model.CardProductType;
import com.example.cardapp.model.Restrictions;
import com.example.cardapp.security.AuthContext;
import com.example.cardapp.security.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer for card operations.
 * Handles business logic for card creation validation and provides hardcoded card data.
 */
@Service
public class CardService {

    /**
     * Validates a card creation request based on defined business rules.
     *
     * @param request The CardCreationRequest DTO.
     * @return A success message if validation passes.
     * @throws ResponseStatusException if validation fails, with a 400 Bad Request status.
     */
    public String createCard(CardCreationRequest request) {
        // Business Rule 1: ProtectionRequired must be true if cardProduct is Prepaid.
        if (request.getCardProduct() == CardProductType.PREPAID && !request.getProtectionRequired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "For Prepaid cards, 'protectionRequired' must be true.");
        }

        // Business Rule 2: Limited Use cards MUST have restrictions defined.
        if (request.getCardProduct() == CardProductType.LIMITED_USE && request.getRestrictions() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "For Limited Use cards, 'restrictions' object must be provided.");
        }

        // Business Rule 3: Prepaid cards MUST NOT have restrictions.
        if (request.getCardProduct() == CardProductType.PREPAID && request.getRestrictions() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prepaid cards cannot have 'restrictions'.");
        }

        // NEW RULE 1: All cards activation date must be in the future.
        // This is already covered by @Future annotation in DTO, but adding a redundant check here
        // for emphasis on business rule validation within the service layer.
        if (request.getActivationDate() != null && request.getActivationDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation date must be in the future.");
        }


        // Business Rule 4: For Limited Use cards, expiryDate must be in the future.
        // NEW RULE 2: Also, expiry date must be at maximum 32 days after current date.
        if (request.getCardProduct() == CardProductType.LIMITED_USE && request.getRestrictions() != null &&
                request.getRestrictions().getExpiryDate() != null) {
            LocalDate expiryDate = request.getRestrictions().getExpiryDate();
            if (expiryDate.isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "For Limited Use cards, 'expiryDate' must be in the future.");
            }
            if (expiryDate.isAfter(LocalDate.now().plusDays(32))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "For Limited Use cards, 'expiryDate' must be at maximum 32 days from today.");
            }
        }

        // Business Rule 5: Load amount must be a multiple of 5 for Prepaid cards.
        if (request.getCardProduct() == CardProductType.PREPAID && request.getLoadAmount() % 5 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "For Prepaid cards, 'loadAmount' must be a multiple of 5.");
        }

        // Authorization check for card creation
        if (AuthContext.getCurrentUser() != null) {
            if (request.getCardProduct() == CardProductType.PREPAID && AuthContext.getCurrentUser().getRole() == AuthUser.Role.LIMITED_USE_ONLY) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Limited Use users cannot create Prepaid cards.");
            }
            if (request.getCardProduct() == CardProductType.LIMITED_USE && AuthContext.getCurrentUser().getRole() == AuthUser.Role.PREPAID_ONLY) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Prepaid users cannot create Limited Use cards.");
            }
        }


        // If all validations and authorization pass
        return "Card Created";
    }

    /**
     * Returns a hardcoded list of CardResponse objects, filtered by card product type.
     * This simulates retrieving card data from a database.
     *
     * @param cardProductType The type of cards to filter by (PREPAID or LIMITED_USE).
     * @return A list of CardResponse objects.
     * @throws ResponseStatusException if the user is not authorized to view the requested card type.
     */
    public List<CardResponse> getAllCards(CardProductType cardProductType) {
        // Authorization check for listing cards
        if (AuthContext.getCurrentUser() != null) {
            if (cardProductType == CardProductType.PREPAID && AuthContext.getCurrentUser().getRole() == AuthUser.Role.LIMITED_USE_ONLY) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found: Limited Use users cannot view Prepaid cards.");
            }
            if (cardProductType == CardProductType.LIMITED_USE && AuthContext.getCurrentUser().getRole() == AuthUser.Role.PREPAID_ONLY) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found: Prepaid users cannot view Limited Use cards.");
            }
        }

        // Simulating hardcoded card data
        List<CardResponse> allCards = Arrays.asList(
                new CardResponse(
                        generateCardNumber(),
                        "Travel Buddy Card",
                        "A versatile card for all your travel needs.",
                        LocalDate.of(2024, 1, 15), // Past date for demonstration of hardcoded data
                        CardProductType.PREPAID,
                        100.00,
                        true,
                        null,
                        "Alice Smith",
                        LocalDateTime.of(2023, 12, 10, 10, 0, 0),
                        85.50,
                        null // Null for Prepaid
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Online Shopping Card",
                        "Secure card for online purchases with spending limits.",
                        LocalDate.of(2024, 3, 1), // Past date for demonstration of hardcoded data
                        CardProductType.LIMITED_USE,
                        50.00,
                        false,
                        new Restrictions(LocalDate.of(2025, 3, 1), 10, 20.00),
                        "Bob Johnson",
                        LocalDateTime.of(2024, 2, 20, 14, 30, 0),
                        30.00,
                        3
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Gaming Pass",
                        "Limited use card for in-game purchases.",
                        LocalDate.of(2024, 5, 10), // Past date for demonstration of hardcoded data
                        CardProductType.LIMITED_USE,
                        25.00,
                        true, // Protection required is true, even for Limited Use (allowed)
                        new Restrictions(LocalDate.of(2024, 12, 31), 5, 10.00),
                        "Charlie Brown",
                        LocalDateTime.of(2024, 5, 5, 9, 0, 0),
                        25.00,
                        0
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Daily Commute Card",
                        "Prepaid card for public transport.",
                        LocalDate.of(2023, 10, 1), // Past date for demonstration of hardcoded data
                        CardProductType.PREPAID,
                        50.00,
                        true,
                        null,
                        "Diana Prince",
                        LocalDateTime.of(2023, 9, 25, 11, 45, 0),
                        10.00,
                        null
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Project Expense Card",
                        "Temporary card for project-related expenses.",
                        LocalDate.of(2024, 6, 1), // Past date for demonstration of hardcoded data
                        CardProductType.LIMITED_USE,
                        500.00,
                        true,
                        new Restrictions(LocalDate.of(2024, 8, 31), 20, 100.00),
                        "Eve Adams",
                        LocalDateTime.of(2024, 5, 28, 16, 0, 0),
                        450.00,
                        5
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Gift Voucher Card",
                        "A simple prepaid gift card.",
                        LocalDate.now().plusDays(5), // Future date
                        CardProductType.PREPAID,
                        20.00,
                        true,
                        null,
                        "Frank Green",
                        LocalDateTime.now().minusDays(2),
                        20.00,
                        null
                ),
                new CardResponse(
                        generateCardNumber(),
                        "Subscription Card",
                        "Limited use card for monthly subscriptions.",
                        LocalDate.now().plusDays(10), // Future date
                        CardProductType.LIMITED_USE,
                        30.00,
                        false,
                        new Restrictions(LocalDate.now().plusDays(20), 1, 30.00), // Expiry within 32 days
                        "Grace Hopper",
                        LocalDateTime.now().minusDays(1),
                        30.00,
                        0
                )
        );

        // Filter based on cardProductType query parameter
        return allCards.stream()
                .filter(card -> card.getCardProduct() == cardProductType)
                .collect(Collectors.toList());
    }

    /**
     * Generates a unique 16-digit card number.
     * @return A string representing a card number.
     */
    private String generateCardNumber() {
        // Simple UUID-based generation for demonstration
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 16).replaceAll("(.{4})", "$1 ").trim();
    }
}