package com.pricetracker.tracking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceEntryRequest {
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    @NotBlank(message = "Store is required")
    @Size(min = 1, max = 100, message = "Store must be between 1 and 100 characters")
    private String store;

    @NotNull(message = "Date is required")
    private LocalDate date;

    // Constructors
    public PriceEntryRequest() {
    }

    public PriceEntryRequest(BigDecimal price, String store, LocalDate date) {
        this.price = price;
        this.store = store;
        this.date = date;
    }

    // Getters and Setters
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
}
