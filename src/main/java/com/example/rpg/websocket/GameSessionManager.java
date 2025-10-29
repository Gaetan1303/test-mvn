package com.example.rpg.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire de sessions WebSocket
 * Maintient la correspondance entre sessionId WebSocket et Character
 */
@Component
public class GameSessionManager {
    
    private static final Logger logger = LoggerFactory.getLogger(GameSessionManager.class);
    
    // Map: WebSocket sessionId -> CharacterId
    private final Map<String, Long> sessionToCharacter = new ConcurrentHashMap<>();
    
    // Map: CharacterId -> WebSocket sessionId
    private final Map<Long, String> characterToSession = new ConcurrentHashMap<>();
    
    // Map: CharacterId -> MapId (zone actuelle)
    private final Map<Long, String> characterToMap = new ConcurrentHashMap<>();
    
    /**
     * Enregistre une nouvelle session de jeu
     */
    public void registerSession(String sessionId, Long characterId, String mapId) {
        sessionToCharacter.put(sessionId, characterId);
        characterToSession.put(characterId, sessionId);
        characterToMap.put(characterId, mapId);
        
        logger.info("Session enregistrée - SessionId: {}, CharacterId: {}, Map: {}", 
                    sessionId, characterId, mapId);
    }
    
    /**
     * Supprime une session (déconnexion)
     */
    public void removeSession(String sessionId) {
        Long characterId = sessionToCharacter.remove(sessionId);
        if (characterId != null) {
            characterToSession.remove(characterId);
            characterToMap.remove(characterId);
            logger.info("Session supprimée - SessionId: {}, CharacterId: {}", sessionId, characterId);
        }
    }
    
    /**
     * Récupère le CharacterId depuis le sessionId WebSocket
     */
    public Long getCharacterId(String sessionId) {
        return sessionToCharacter.get(sessionId);
    }
    
    /**
     * Récupère le sessionId WebSocket depuis le CharacterId
     */
    public String getSessionId(Long characterId) {
        return characterToSession.get(characterId);
    }
    
    /**
     * Récupère la map actuelle du personnage
     */
    public String getMapId(Long characterId) {
        return characterToMap.getOrDefault(characterId, "world_1");
    }
    
    /**
     * Change la map du personnage
     */
    public void updateMap(Long characterId, String newMapId) {
        characterToMap.put(characterId, newMapId);
        logger.info("CharacterId {} changé de map vers: {}", characterId, newMapId);
    }
    
    /**
     * Vérifie si un personnage est connecté
     */
    public boolean isConnected(Long characterId) {
        return characterToSession.containsKey(characterId);
    }
    
    /**
     * Récupère tous les personnages connectés sur une map
     */
    public Map<Long, String> getPlayersOnMap(String mapId) {
        Map<Long, String> players = new ConcurrentHashMap<>();
        characterToMap.forEach((characterId, currentMapId) -> {
            if (currentMapId.equals(mapId)) {
                players.put(characterId, characterToSession.get(characterId));
            }
        });
        return players;
    }
    
    /**
     * Statistiques des sessions actives
     */
    public int getActiveSessions() {
        return sessionToCharacter.size();
    }
    
    /**
     * Affiche les statistiques
     */
    public void logStats() {
        logger.info("Sessions actives: {}, Maps occupées: {}", 
                    sessionToCharacter.size(), 
                    characterToMap.values().stream().distinct().count());
    }
}
