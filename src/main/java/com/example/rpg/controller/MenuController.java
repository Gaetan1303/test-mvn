package com.example.rpg.controller;

import com.example.rpg.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final CharacterService characterService;

    public MenuController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMenu(Authentication authentication) {
        String username = authentication.getName();
        boolean hasCharacter = characterService.hasCharacter(username);

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("hasCharacter", hasCharacter);
        body.put("next", hasCharacter ? "/api/character/me" : "/api/character/create");
        body.put("message", hasCharacter ? "Prêt à lancer le jeu" : "Veuillez créer votre personnage");

        return ResponseEntity.ok(body);
    }
}
