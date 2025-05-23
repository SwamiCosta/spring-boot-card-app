package com.example.cardapp.dto;

import com.example.cardapp.model.CardProductType;
import com.example.cardapp.model.Restrictions;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for the Card Creation Request payload.
 * Includes validation annotations for business rules.
 */
public class CardCreationRequest {

    @NotBlank(message = "Card title cannot be empty")
    @Size(min = 3, max = 100, message = "Card title must be between 3 and 100 characters")
    private String cardTitle;

    @NotBlank(message = "Card description cannot be empty")
    @Size(min = 10, max = 500, message = "Card description must be between 10 and 500 characters")
    private String cardDescription;

    @NotNull(message = "Activation date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;

    @NotNull(message = "Card product type cannot be null")
    private CardProductType cardProduct;

    @NotNull(message = "Load amount cannot be null")
    @Positive(message = "Load amount must be positive")
    private Double loadAmount;

    @NotNull(message = "Protection required cannot be null")
    private Boolean protectionRequired;

    @Valid // Enables validation on nested object
    private Restrictions restrictions; // Optional for Limited Use cards

    // Constructors
    public CardCreationRequest() {
    }

    public CardCreationRequest(String cardTitle, String cardDescription, LocalDate activationDate, CardProductType cardProduct, Double loadAmount, Boolean protectionRequired, Restrictions restrictions) {
        this.cardTitle = cardTitle;
        this.cardDescription = cardDescription;
        this.activationDate = activationDate;
        this.cardProduct = cardProduct;
        this.loadAmount = loadAmount;
        this.protectionRequired = protectionRequired;
        this.restrictions = restrictions;
    }

    // Getters
    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public CardProductType getCardProduct() {
        return cardProduct;
    }

    public Double getLoadAmount() {
        return loadAmount;
    }

    public Boolean getProtectionRequired() {
        return protectionRequired;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    // Setters
    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public void setCardProduct(CardProductType cardProduct) {
        this.cardProduct = cardProduct;
    }

    public void setLoadAmount(Double loadAmount) {
        this.loadAmount = loadAmount;
    }

    public void setProtectionRequired(Boolean protectionRequired) {
        this.protectionRequired = protectionRequired;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    // hashCode, equals, toString for good practice
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCreationRequest that = (CardCreationRequest) o;
        return Objects.equals(cardTitle, that.cardTitle) &&
                Objects.equals(cardDescription, that.cardDescription) &&
                Objects.equals(activationDate, that.activationDate) &&
                cardProduct == that.cardProduct &&
                Objects.equals(loadAmount, that.loadAmount) &&
                Objects.equals(protectionRequired, that.protectionRequired) &&
                Objects.equals(restrictions, that.restrictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardTitle, cardDescription, activationDate, cardProduct, loadAmount, protectionRequired, restrictions);
    }

    @Override
    public String toString() {
        return "CardCreationRequest{" +
                "cardTitle='" + cardTitle + '\'' +
                ", cardDescription='" + cardDescription + '\'' +
                ", activationDate=" + activationDate +
                ", cardProduct=" + cardProduct +
                ", loadAmount=" + loadAmount +
                ", protectionRequired=" + protectionRequired +
                ", restrictions=" + restrictions +
                '}';
    }
}