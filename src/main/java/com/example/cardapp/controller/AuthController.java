package com.example.cardapp.controller;

import com.example.cardapp.dto.ErrorResponse;
import com.example.cardapp.dto.LoginRequest;
import com.example.cardapp.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * REST Controller for handling authentication-related operations.
 * Provides an endpoint for user login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;

    @Autowired
    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Endpoint for user login.
     * Authenticates the user with provided credentials and returns a time-based token.
     *
     * @param request The LoginRequest DTO containing userId and password.
     * @return A ResponseEntity containing the generated token or an ErrorResponse.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            String token = tokenService.generateToken(request.getUserId(), request.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (IllegalArgumentException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ex.getMessage(),
                    httpRequest.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Exception handler for @Valid DTO validation errors specific to this controller.
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