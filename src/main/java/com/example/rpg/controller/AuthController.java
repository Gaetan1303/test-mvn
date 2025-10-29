package com.example.rpg.controller;

import com.example.rpg.service.AuthService;
import com.example.rpg.dto.RegisterRequest;
import com.example.rpg.dto.LoginRequest;
import com.example.rpg.dto.AuthResponse;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class AuthController {

    private final AuthService authService;
    private final UtilisateurRepository utilisateurRepository;

    
    public AuthController(AuthService authService, UtilisateurRepository utilisateurRepository) {
        this.authService = authService;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ======== INSCRIPTION ========
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        
        // Récupérer l'utilisateur pour obtenir l'ID
        Utilisateur utilisateur = utilisateurRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Erreur lors de la création de l'utilisateur"));
        
        AuthResponse response = new AuthResponse(
            "Utilisateur créé avec succès", 
            token,
            utilisateur.getId(),
            utilisateur.getUsername()
        );
        return ResponseEntity.ok(response);
    }

    // ======== CONNEXION ========
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        
        // Récupérer l'utilisateur pour obtenir l'ID
        Utilisateur utilisateur = utilisateurRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        AuthResponse response = new AuthResponse(
            "Connexion réussie",
            token,
            utilisateur.getId(),
            utilisateur.getUsername()
        );
        return ResponseEntity.ok(response);
    }
}
