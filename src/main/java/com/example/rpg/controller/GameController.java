package com.example.rpg.controller;

import com.example.rpg.dto.MovementRequest;
import com.example.rpg.dto.PositionUpdate;
import com.example.rpg.model.Character;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.service.GameService;
import com.example.rpg.websocket.GameSessionManager;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * Contrôleur WebSocket pour la logique de jeu temps réel
 * Gère le mouvement, la synchronisation et les événements de jeu
 */
@Controller
public class GameController {
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    private final GameService gameService;
    private final GameSessionManager sessionManager;
    private final SimpMessagingTemplate messagingTemplate;
    private final CharacterRepository characterRepository;
    
    public GameController(GameService gameService, 
                         GameSessionManager sessionManager,
                         SimpMessagingTemplate messagingTemplate,
                         CharacterRepository characterRepository) {
        this.gameService = gameService;
        this.sessionManager = sessionManager;
        this.messagingTemplate = messagingTemplate;
        this.characterRepository = characterRepository;
    }
    
    /**
     * Gestion de la connexion d'un joueur au jeu
     * Endpoint: /app/game/connect
     */
    @MessageMapping("/game/connect")
    public void handleConnect(@Payload Long characterId, 
                             SimpMessageHeaderAccessor headerAccessor,
                             Principal principal) {
        String sessionId = headerAccessor.getSessionId();
        String username = principal.getName();
        
        // Vérifier que le personnage appartient bien à l'utilisateur
        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé"));
        
        if (!character.getUtilisateur().getUsername().equals(username)) {
            logger.warn("Tentative de connexion avec personnage non autorisé - User: {}, CharId: {}", 
                       username, characterId);
            throw new SecurityException("Personnage non autorisé");
        }
        
        // Enregistrer la session
        String mapId = "world_1"; // Map par défaut
        sessionManager.registerSession(sessionId, characterId, mapId);
        
        // Envoyer la position actuelle à tous les joueurs de la map
        PositionUpdate positionUpdate = new PositionUpdate(
            character.getId(),
            character.getName(),
            character.getPositionX(),
            character.getPositionY(),
            mapId
        );
        
        messagingTemplate.convertAndSend("/topic/game/position", positionUpdate);
        
        logger.info("Joueur connecté au jeu - User: {}, Character: {} (ID: {}), Position: ({}, {})", 
                   username, character.getName(), characterId, 
                   character.getPositionX(), character.getPositionY());
        
        // Envoyer les positions de tous les autres joueurs au nouveau connecté
        sessionManager.getPlayersOnMap(mapId).forEach((otherCharId, otherSessionId) -> {
            if (!otherCharId.equals(characterId)) {
                try {
                    Character otherChar = characterRepository.findById(otherCharId).orElse(null);
                    if (otherChar != null) {
                        PositionUpdate otherPosition = new PositionUpdate(
                            otherChar.getId(),
                            otherChar.getName(),
                            otherChar.getPositionX(),
                            otherChar.getPositionY(),
                            mapId
                        );
                        messagingTemplate.convertAndSendToUser(
                            username, 
                            "/queue/game/initial-state", 
                            otherPosition
                        );
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de l'envoi des positions initiales", e);
                }
            }
        });
    }
    
    /**
     * Endpoint REST pour tester les mouvements (utilisé par les tests d'intégration)
     * Endpoint: POST /api/game/move/{characterId}
     */
    @PostMapping("/api/game/move/{characterId}")
    public ResponseEntity<?> moveCharacter(
            @PathVariable Long characterId,
            @Valid @RequestBody MovementRequest request) {
        
        try {
            Character character = gameService.processMove(
                characterId, 
                request.getNewX(), 
                request.getNewY(), 
                request.getMapId()
            );
            
            return ResponseEntity.ok(Map.of(
                "message", "Mouvement effectué avec succès",
                "character", character
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Erreur lors du mouvement du personnage {}", characterId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur interne du serveur"));
        }
    }

    /**
     * Gestion du mouvement d'un personnage
     * Endpoint: /app/game/move
     */
    @MessageMapping("/game/move")
    public void handleMove(@Valid @Payload MovementRequest request,
                          SimpMessageHeaderAccessor headerAccessor,
                          Principal principal) {
        String sessionId = headerAccessor.getSessionId();
        Long characterId = sessionManager.getCharacterId(sessionId);
        
        if (characterId == null) {
            logger.warn("Tentative de mouvement sans session enregistrée - User: {}", principal.getName());
            throw new IllegalStateException("Session non enregistrée. Connectez-vous d'abord avec /app/game/connect");
        }
        
        try {
            // Traiter le mouvement (validation autoritaire)
            Character character = gameService.processMove(
                characterId, 
                request.getNewX(), 
                request.getNewY(), 
                request.getMapId()
            );
            
            // Créer l'update de position
            PositionUpdate positionUpdate = new PositionUpdate(
                character.getId(),
                character.getName(),
                character.getPositionX(),
                character.getPositionY(),
                request.getMapId()
            );
            
            // Diffuser la nouvelle position à tous les joueurs de la map
            messagingTemplate.convertAndSend("/topic/game/position", positionUpdate);
            
            logger.debug("Position diffusée - Character: {}, Position: ({}, {})", 
                        character.getName(), character.getPositionX(), character.getPositionY());
            
        } catch (IllegalArgumentException e) {
            // Mouvement invalide - envoyer une erreur au client
            logger.warn("Mouvement refusé pour CharId {}: {}", characterId, e.getMessage());
            messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                "Mouvement invalide: " + e.getMessage()
            );
        }
    }
    
    /**
     * Subscription à la position des joueurs
     * Endpoint: /topic/game/position
     */
    @SubscribeMapping("/game/position")
    public void onSubscribePosition(Principal principal) {
        logger.info("{} s'est abonné aux mises à jour de position", principal.getName());
    }
    
    /**
     * Gestion de la déconnexion
     * Automatiquement appelé par Spring lors de la fermeture de la connexion WebSocket
     */
    // Note: La déconnexion sera gérée dans un EventListener séparé
}
