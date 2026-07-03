package com.pricetracker.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pricetracker.categories.dto.CategoryResponse;
import com.pricetracker.model.UnitType;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private UUID id;
    private String name;
    private CategoryResponse category;
    private UnitType unit;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public ProductResponse() {
    }

    public ProductResponse(UUID id, String name, CategoryResponse category, UnitType unit, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
