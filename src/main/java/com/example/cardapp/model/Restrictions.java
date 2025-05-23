package com.example.cardapp.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents restrictions for a 'Limited Use' card product.
 * Includes expiryDate, maxSwipes, and perTransactionLimit.
 */
public class Restrictions {
    private LocalDate expiryDate;
    private Integer maxSwipes;
    private Double perTransactionLimit;

    // Constructors
    public Restrictions() {
    }

    public Restrictions(LocalDate expiryDate, Integer maxSwipes, Double perTransactionLimit) {
        this.expiryDate = expiryDate;
        this.maxSwipes = maxSwipes;
        this.perTransactionLimit = perTransactionLimit;
    }

    // Getters
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Integer getMaxSwipes() {
        return maxSwipes;
    }

    public Double getPerTransactionLimit() {
        return perTransactionLimit;
    }

    // Setters
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setMaxSwipes(Integer maxSwipes) {
        this.maxSwipes = maxSwipes;
    }

    public void setPerTransactionLimit(Double perTransactionLimit) {
        this.perTransactionLimit = perTransactionLimit;
    }

    // hashCode, equals, toString for good practice
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restrictions that = (Restrictions) o;
        return Objects.equals(expiryDate, that.expiryDate) &&
                Objects.equals(maxSwipes, that.maxSwipes) &&
                Objects.equals(perTransactionLimit, that.perTransactionLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiryDate, maxSwipes, perTransactionLimit);
    }

    @Override
    public String toString() {
        return "Restrictions{" +
                "expiryDate=" + expiryDate +
                ", maxSwipes=" + maxSwipes +
                ", perTransactionLimit=" + perTransactionLimit +
                '}';
    }
}