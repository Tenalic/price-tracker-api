package com.pricetracker.tracking.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class PriceEntryResponse {
    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private String store;
    private LocalDate date;
    private Instant createdAt;

    // Constructors
    public PriceEntryResponse() {
    }

    public PriceEntryResponse(UUID id, UUID productId, BigDecimal price, String store, LocalDate date, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.store = store;
        this.date = date;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
