package com.pricetracker.auth.dto;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String email;
    private String username;

    // Constructors
    public UserResponse() {
    }

    public UserResponse(UUID id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
