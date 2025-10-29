package com.example.rpg.controller;

import com.example.rpg.service.AuthService;
import com.example.rpg.dto.RegisterRequest;
import com.example.rpg.dto.LoginRequest;
import com.example.rpg.dto.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class AuthController {

    private final AuthService authService;

    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ======== INSCRIPTION ========
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        AuthResponse response = new AuthResponse("Utilisateur créé avec succès", token);
        return ResponseEntity.ok(response);
    }

    // ======== CONNEXION ========
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        AuthResponse response = new AuthResponse("Connexion réussie", token);
        return ResponseEntity.ok(response);
    }
}
