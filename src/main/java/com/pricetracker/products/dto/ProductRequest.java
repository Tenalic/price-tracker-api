package com.pricetracker.products.dto;

import com.pricetracker.model.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Unit type is required")
    private UnitType unit;

    // Constructors
    public ProductRequest() {
    }

    public ProductRequest(String name, UUID categoryId, UnitType unit) {
        this.name = name;
        this.categoryId = categoryId;
        this.unit = unit;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }
}
