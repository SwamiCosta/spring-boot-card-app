package com.example.cardapp.controller;

import com.example.cardapp.dto.CardCreationRequest;
import com.example.cardapp.dto.CardResponse;
import com.example.cardapp.dto.ErrorResponse;
import com.example.cardapp.model.CardProductType;
import com.example.cardapp.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing card-related operations.
 * Exposes endpoints for card creation validation and listing all cards.
 * Includes exception handling to return standardized ErrorResponse objects.
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Endpoint for creating a new card.
     * This endpoint primarily validates the incoming request payload against business rules.
     * Authentication is handled by AuthInterceptor.
     *
     * @param request The CardCreationRequest DTO containing card details.
     * @return A ResponseEntity with a success message (200 OK) or an error message (400 Bad Request).
     */
    @PostMapping("/create")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardCreationRequest request, HttpServletRequest httpRequest) {
        try {
            String message = cardService.createCard(request);
            return ResponseEntity.ok(message);
        } catch (ResponseStatusException ex) {
            // Catch exceptions thrown by the service layer (e.g., business rule violations, forbidden access)
            HttpStatus status = (HttpStatus) ex.getStatusCode();
            ErrorResponse errorResponse = new ErrorResponse(
                    status.value(),
                    status.getReasonPhrase(),
                    ex.getReason(), // Use the reason from ResponseStatusException as the message
                    httpRequest.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, status);
        }
    }

    /**
     * Endpoint for listing all existing cards.
     * Returns a hardcoded list of card information, filtered by card product type.
     * Authentication is handled by AuthInterceptor.
     *
     * @param cardProduct The type of cards to filter by (PREPAID or LIMITED_USE).
     * @return A List of CardResponse DTOs.
     */
    @GetMapping
    public ResponseEntity<?> getAllCards(@RequestParam String cardProduct, HttpServletRequest httpRequest) {
        CardProductType type;
        try {
            type = CardProductType.valueOf(cardProduct.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid cardProduct query parameter specifically
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Invalid cardProduct parameter. Must be 'PREPAID' or 'LIMITED_USE'.",
                    httpRequest.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            List<CardResponse> cards = cardService.getAllCards(type);
            return ResponseEntity.ok(cards);
        } catch (ResponseStatusException ex) {
            HttpStatus status = (HttpStatus) ex.getStatusCode();
            ErrorResponse errorResponse = new ErrorResponse(
                    status.value(),
                    status.getReasonPhrase(),
                    ex.getReason(),
                    httpRequest.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, status);
        }
    }

    /**
     * Exception handler for @Valid DTO validation errors.
     * This catches validation failures that occur before the controller method body is executed.
     *
     * @param ex The MethodArgumentNotValidException.
     * @param httpRequest The HTTP request.
     * @return A ResponseEntity with an ErrorResponse and 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest httpRequest) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation Error: " + errorMessage,
                httpRequest.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}