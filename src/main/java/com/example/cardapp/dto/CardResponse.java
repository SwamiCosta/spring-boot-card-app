package com.example.cardapp.dto;

import com.example.cardapp.model.CardProductType;
import com.example.cardapp.model.Restrictions;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for the Card Response payload.
 * Used for both card creation success and listing all cards.
 */
public class CardResponse {
    private String cardNumber;
    private String cardTitle;
    private String cardDescription;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;
    private CardProductType cardProduct;
    private Double loadAmount;
    private Boolean protectionRequired;
    private Restrictions restrictions; // Optional for Limited Use cards
    private String cardCreator;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    private Double currentLoad;
    private Integer currentNumberOfSwipes; // Null for Prepaid Cards

    public CardResponse() {
    }

    public CardResponse(String cardNumber, String cardTitle, String cardDescription, LocalDate activationDate, CardProductType cardProduct, Double loadAmount, Boolean protectionRequired, Restrictions restrictions, String cardCreator, LocalDateTime creationDate, Double currentLoad, Integer currentNumberOfSwipes) {
        this.cardNumber = cardNumber;
        this.cardTitle = cardTitle;
        this.cardDescription = cardDescription;
        this.activationDate = activationDate;
        this.cardProduct = cardProduct;
        this.loadAmount = loadAmount;
        this.protectionRequired = protectionRequired;
        this.restrictions = restrictions;
        this.cardCreator = cardCreator;
        this.creationDate = creationDate;
        this.currentLoad = currentLoad;
        this.currentNumberOfSwipes = currentNumberOfSwipes;
    }

    public String getCardNumber() {
        return cardNumber;
    }

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

    public String getCardCreator() {
        return cardCreator;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getCurrentLoad() {
        return currentLoad;
    }

    public Integer getCurrentNumberOfSwipes() {
        return currentNumberOfSwipes;
    }

    // Setters
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

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

    public void setCardCreator(String cardCreator) {
        this.cardCreator = cardCreator;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setCurrentLoad(Double currentLoad) {
        this.currentLoad = currentLoad;
    }

    public void setCurrentNumberOfSwipes(Integer currentNumberOfSwipes) {
        this.currentNumberOfSwipes = currentNumberOfSwipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardResponse that = (CardResponse) o;
        return Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(cardTitle, that.cardTitle) &&
                Objects.equals(cardDescription, that.cardDescription) &&
                Objects.equals(activationDate, that.activationDate) &&
                cardProduct == that.cardProduct &&
                Objects.equals(loadAmount, that.loadAmount) &&
                Objects.equals(protectionRequired, that.protectionRequired) &&
                Objects.equals(restrictions, that.restrictions) &&
                Objects.equals(cardCreator, that.cardCreator) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(currentLoad, that.currentLoad) &&
                Objects.equals(currentNumberOfSwipes, that.currentNumberOfSwipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cardTitle, cardDescription, activationDate, cardProduct, loadAmount, protectionRequired, restrictions, cardCreator, creationDate, currentLoad, currentNumberOfSwipes);
    }

    @Override
    public String toString() {
        return "CardResponse{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardTitle='" + cardTitle + '\'' +
                ", cardDescription='" + cardDescription + '\'' +
                ", activationDate=" + activationDate +
                ", cardProduct=" + cardProduct +
                ", loadAmount=" + loadAmount +
                ", protectionRequired=" + protectionRequired +
                ", restrictions=" + restrictions +
                ", cardCreator='" + cardCreator + '\'' +
                ", creationDate=" + creationDate +
                ", currentLoad=" + currentLoad +
                ", currentNumberOfSwipes=" + currentNumberOfSwipes +
                '}';
    }
}