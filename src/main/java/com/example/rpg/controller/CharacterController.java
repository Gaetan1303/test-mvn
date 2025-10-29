package com.example.rpg.controller;

import com.example.rpg.dto.CharacterClassInfo;
import com.example.rpg.dto.CharacterResponse;
import com.example.rpg.dto.CreateCharacterRequest;
import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion des personnages
 * Tous les endpoints nécessitent une authentification JWT
 */
@RestController
@RequestMapping("/api/character")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * Crée un nouveau personnage pour l'utilisateur connecté
     * 
     * @param request Données du personnage à créer
     * @param authentication Authentification Spring Security (contient le username du JWT)
     * @return Le personnage créé avec toutes ses statistiques
     */
    @PostMapping("/create")
    public ResponseEntity<CharacterResponse> createCharacter(
            @Valid @RequestBody CreateCharacterRequest request,
            Authentication authentication) {
        
        String username = authentication.getName();
        Character character = characterService.createCharacter(
            username, 
            request.getName(), 
            request.getCharacterClass()
        );

        CharacterResponse response = new CharacterResponse(
            character, 
            "Personnage créé avec succès ! Bienvenue " + character.getName() + " !"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Récupère le personnage de l'utilisateur connecté
     * 
     * @param authentication Authentification Spring Security
     * @return Le personnage avec toutes ses informations
     */
    @Deprecated
    @GetMapping("/me")
    public ResponseEntity<CharacterResponse> getMyCharacter(Authentication authentication) {
        String username = authentication.getName();
        Character character = characterService.getCharacterByUsername(username);

        CharacterResponse response = new CharacterResponse(
            character, 
            "Personnage récupéré avec succès"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Récupère la liste de tous les personnages de l'utilisateur connecté
     * 
     * @param authentication Authentification Spring Security
     * @return Liste des personnages
     */
    @GetMapping("/list")
    public ResponseEntity<List<CharacterResponse>> getMyCharacters(Authentication authentication) {
        String username = authentication.getName();
        List<Character> characters = characterService.getAllCharactersByUsername(username);

        List<CharacterResponse> responses = characters.stream()
            .map(ch -> new CharacterResponse(ch, null))
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Récupère un personnage spécifique par son ID
     * 
     * @param characterId ID du personnage
     * @param authentication Authentification Spring Security
     * @return Le personnage
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CharacterResponse> getCharacterById(
            @PathVariable("id") Long characterId,
            Authentication authentication) {
        String username = authentication.getName();
        Character character = characterService.getCharacterById(username, characterId);

        CharacterResponse response = new CharacterResponse(
            character,
            "Personnage récupéré avec succès"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Vérifie si l'utilisateur connecté a déjà un personnage
     * 
     * @param authentication Authentification Spring Security
     * @return true si un personnage existe, false sinon
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> hasCharacter(Authentication authentication) {
        String username = authentication.getName();
        boolean exists = characterService.hasCharacter(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * Supprime le personnage de l'utilisateur connecté
     * 
     * @param authentication Authentification Spring Security
     * @return Message de confirmation
     * @deprecated Utiliser /delete/{id} pour multi-personnages
     */
    @Deprecated
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyCharacter(Authentication authentication) {
        String username = authentication.getName();
        characterService.deleteCharacter(username);
        return ResponseEntity.ok("Personnage supprimé avec succès");
    }

    /**
     * Supprime un personnage spécifique par son ID
     * 
     * @param characterId ID du personnage
     * @param authentication Authentification Spring Security
     * @return Message de confirmation
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCharacterById(
            @PathVariable("id") Long characterId,
            Authentication authentication) {
        String username = authentication.getName();
        characterService.deleteCharacterById(username, characterId);
        return ResponseEntity.ok("Personnage supprimé avec succès");
    }

    /**
     * Récupère la liste de toutes les classes de personnages disponibles
     * Endpoint public (pas besoin d'authentification)
     * 
     * @return Liste des classes avec leurs stats de base (système FFT)
     */
    @GetMapping("/classes")
    public ResponseEntity<List<CharacterClassInfo>> getCharacterClasses() {
        List<CharacterClassInfo> classes = Arrays.stream(CharacterClass.values())
            .map(charClass -> {
                String emoji = switch (charClass) {
                    // Combat et Mêlée
                    case SQUIRE -> "🎓";
                    case KNIGHT -> "🛡️";
                    case MONK -> "🥋";
                    case THIEF -> "🗡️";
                    case DRAGOON -> "🐉";
                    case SAMURAI -> "⚔️";
                    case NINJA -> "🥷";
                    // Magie et Support
                    case CHEMIST -> "⚗️";
                    case WHITE_MAGE -> "✨";
                    case BLACK_MAGE -> "🔮";
                    case TIME_MAGE -> "⏰";
                    case SUMMONER -> "🌟";
                    case MYSTIC -> "🔯";
                    case GEOMANCER -> "🌍";
                    // Spéciaux
                    case BARD -> "🎵";
                    case DANCER -> "💃";
                    case MIME -> "�";
                    case DARK_KNIGHT -> "🗡️";
                };
                
                return new CharacterClassInfo(
                    charClass.name(),
                    charClass.getDisplayName(),
                    charClass.getRole(),
                    charClass.getBaseHp(),
                    charClass.getBaseMp(),
                    charClass.getBasePa(),
                    charClass.getBaseMa(),
                    charClass.getBaseSpeed(),
                    charClass.getBaseMove(),
                    emoji
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(classes);
    }
}
