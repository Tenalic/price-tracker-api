package com.pricetracker.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pricetracker.categories.dto.CategoryResponse;
import com.pricetracker.model.UnitType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductListResponse {
    private UUID id;
    private String name;
    private CategoryResponse category;
    private UnitType unit;
    private BigDecimal lastPrice;
    private String lastStore;
    private Instant updatedAt;

    // Constructors
    public ProductListResponse() {
    }

    public ProductListResponse(UUID id, String name, CategoryResponse category, UnitType unit,
                              BigDecimal lastPrice, String lastStore, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.lastPrice = lastPrice;
        this.lastStore = lastStore;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastStore() {
        return lastStore;
    }

    public void setLastStore(String lastStore) {
        this.lastStore = lastStore;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
