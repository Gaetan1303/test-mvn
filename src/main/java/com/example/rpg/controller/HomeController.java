package com.example.rpg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour la page d'accueil publique de l'API
 */
@RestController
public class HomeController {

    /**
     * Page d'accueil de l'API - Accessible sans authentification
     * @return Informations sur l'API
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "RPG Multijoueur - API");
        response.put("version", "1.0.0");
        response.put("status", "online");
        response.put("message", "Bienvenue sur l'API du RPG Multijoueur FFT");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("register", "POST /api/auth/register");
        endpoints.put("login", "POST /api/auth/login");
        endpoints.put("classes", "GET /api/character/classes (JWT requis)");
        endpoints.put("createCharacter", "POST /api/character/create (JWT requis)");
        endpoints.put("listCharacters", "GET /api/character/list (JWT requis)");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> documentation = new HashMap<>();
        documentation.put("mercure", "WebSocket hub: /.well-known/mercure");
        documentation.put("github", "https://github.com/Gaetan1303/test-mvn");
        
        response.put("documentation", documentation);
        
        return response;
    }
    
    /**
     * Health check endpoint
     * @return Status de l'application
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }
}
