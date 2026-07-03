package com.pricetracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityContextHelper {
    private final JwtService jwtService;

    public SecurityContextHelper(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getUsername());
        }
        return Optional.empty();
    }

    public String getCurrentUserEmailOrThrow() {
        return getCurrentUserEmail()
                .orElseThrow(() -> new IllegalStateException("No authenticated user found"));
    }
}
