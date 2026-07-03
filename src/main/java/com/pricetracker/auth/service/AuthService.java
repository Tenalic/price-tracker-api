package com.pricetracker.auth.service;

import com.pricetracker.auth.dto.AuthResponse;
import com.pricetracker.auth.dto.LoginRequest;
import com.pricetracker.auth.dto.RegisterRequest;
import com.pricetracker.auth.dto.UserResponse;
import com.pricetracker.auth.repository.UserRepository;
import com.pricetracker.exception.BadRequestException;
import com.pricetracker.exception.ConflictException;
import com.pricetracker.model.User;
import com.pricetracker.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = new User(request.getEmail(), request.getUsername(), hashedPassword);
        User savedUser = userRepository.save(newUser);

        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());
        UserResponse userResponse = new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());

        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getUsername());

        return new AuthResponse(token, userResponse);
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }
}
