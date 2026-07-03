package com.pricetracker.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Icon is required")
    @Size(min = 1, max = 50, message = "Icon must be between 1 and 50 characters")
    private String icon;

    // Constructors
    public CategoryRequest() {
    }

    public CategoryRequest(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
