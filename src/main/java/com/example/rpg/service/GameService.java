package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.repository.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion du mouvement et de la logique de jeu
 * Serveur AUTORITAIRE - Toute la logique s'exécute côté serveur
 */
@Service
public class GameService {
    
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    
    private final CharacterRepository characterRepository;
    
    // Limites de la map (à ajuster selon votre grille)
    private static final double MAP_MIN_X = 0.0;
    private static final double MAP_MAX_X = 100.0;
    private static final double MAP_MIN_Y = 0.0;
    private static final double MAP_MAX_Y = 100.0;
    
    public GameService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }
    
    /**
     * Traite un mouvement de personnage (AUTORITAIRE)
     * Valide le mouvement et met à jour la position
     */
    @Transactional
    public Character processMove(Long characterId, Double newX, Double newY, String mapId) {
        // Récupérer le personnage
        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé: " + characterId));
        
        // Valider les nouvelles coordonnées
        if (!isValidPosition(newX, newY)) {
            logger.warn("Mouvement invalide pour {} - Position hors limites: ({}, {})", 
                       character.getName(), newX, newY);
            throw new IllegalArgumentException("Position hors limites");
        }
        
        // Calculer la distance du mouvement
        double distance = calculateDistance(character.getPositionX(), character.getPositionY(), newX, newY);
        double maxMoveDistance = character.getMove(); // Stat FFT "Move"
        
        if (distance > maxMoveDistance) {
            logger.warn("Mouvement trop grand pour {} - Distance: {}, Max: {}", 
                       character.getName(), distance, maxMoveDistance);
            throw new IllegalArgumentException("Mouvement trop grand");
        }
        
        // Mettre à jour la position
        Double oldX = character.getPositionX();
        Double oldY = character.getPositionY();
        
        character.setPositionX(newX);
        character.setPositionY(newY);
        
        Character updatedCharacter = characterRepository.save(character);
        
        logger.info("Mouvement validé - {} de ({}, {}) vers ({}, {})", 
                   character.getName(), oldX, oldY, newX, newY);
        
        return updatedCharacter;
    }
    
    /**
     * Valide si une position est dans les limites de la map
     */
    private boolean isValidPosition(Double x, Double y) {
        return x >= MAP_MIN_X && x <= MAP_MAX_X && y >= MAP_MIN_Y && y <= MAP_MAX_Y;
    }
    
    /**
     * Calcule la distance euclidienne entre deux points
     */
    private double calculateDistance(Double x1, Double y1, Double x2, Double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Récupère un personnage par son ID
     */
    public Character getCharacter(Long characterId) {
        return characterRepository.findById(characterId)
            .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé: " + characterId));
    }
    
    /**
     * Téléporte un personnage à une position (pour admin/debug)
     */
    @Transactional
    public Character teleport(Long characterId, Double x, Double y) {
        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé: " + characterId));
        
        if (!isValidPosition(x, y)) {
            throw new IllegalArgumentException("Position de téléportation invalide");
        }
        
        character.setPositionX(x);
        character.setPositionY(y);
        
        logger.info("Téléportation - {} vers ({}, {})", character.getName(), x, y);
        
        return characterRepository.save(character);
    }
}
