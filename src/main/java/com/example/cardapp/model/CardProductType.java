package com.example.cardapp.model;

/**
 * Enum to represent the type of card product.
 * Prepaid cards have a fixed load amount and no transaction limits.
 * Limited Use cards can have restrictions like expiry date, max swipes, and per-transaction limit.
 */
public enum CardProductType {
    PREPAID,
    LIMITED_USE
}