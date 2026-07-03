package com.pricetracker.categories.dto;

import java.util.UUID;

public class CategoryResponse {
    private UUID id;
    private String name;
    private String icon;
    private boolean isCustom;

    // Constructors
    public CategoryResponse() {
    }

    public CategoryResponse(UUID id, String name, String icon, boolean isCustom) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.isCustom = isCustom;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}
