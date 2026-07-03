package com.pricetracker.auth.controller;

import com.pricetracker.auth.dto.AuthResponse;
import com.pricetracker.auth.dto.LoginRequest;
import com.pricetracker.auth.dto.RegisterRequest;
import com.pricetracker.auth.dto.UserResponse;
import com.pricetracker.auth.service.AuthService;
import com.pricetracker.security.SecurityContextHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final SecurityContextHelper securityContextHelper;

    public AuthController(AuthService authService, SecurityContextHelper securityContextHelper) {
        this.authService = authService;
        this.securityContextHelper = securityContextHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        String email = securityContextHelper.getCurrentUserEmailOrThrow();
        UserResponse response = authService.getCurrentUser(email);
        return ResponseEntity.ok(response);
    }
}
