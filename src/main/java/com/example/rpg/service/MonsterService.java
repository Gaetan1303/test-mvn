package com.example.rpg.service;

import com.example.rpg.model.Monster;
import com.example.rpg.model.MonsterState;
import com.example.rpg.repository.MonsterRepository;
import com.example.rpg.repository.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonsterService {

    private static final Logger logger = LoggerFactory.getLogger(MonsterService.class);

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    private CharacterRepository characterRepository;

    /**
     * Traite le mouvement d'un monstre avec détection de collision
     */
    public Monster processMonsterMove(Long monsterId, Double newX, Double newY, String mapId) {
        // Récupérer le monstre
        Optional<Monster> optionalMonster = monsterRepository.findById(monsterId);
        if (optionalMonster.isEmpty()) {
            throw new RuntimeException("Monstre non trouvé: " + monsterId);
        }

        Monster monster = optionalMonster.get();
        double oldX = monster.getPositionX();
        double oldY = monster.getPositionY();

        // Valider la distance de mouvement
        double distance = Math.sqrt(Math.pow(newX - oldX, 2) + Math.pow(newY - oldY, 2));
        if (distance > monster.getMove()) {
            throw new RuntimeException("Distance de mouvement trop grande: " + distance + " > " + monster.getMove());
        }

        // Vérifier collision avec autres monstres
        List<Long> collidingMonsters = monsterRepository.findMonsterAtPosition(newX, newY, monsterId);
        if (!collidingMonsters.isEmpty()) {
            logger.warn("Collision détectée pour {} - Position occupée par monstre: ({}, {})", 
                       monster.getName(), newX, newY);
            throw new RuntimeException("Position occupée par un autre monstre");
        }

        // Vérifier collision avec joueurs (utilisation de CharacterRepository)
        List<Long> collidingCharacters = characterRepository.findCharacterIdsAtPosition(newX, newY);
        if (!collidingCharacters.isEmpty()) {
            logger.warn("Collision détectée pour {} - Position occupée par joueur: ({}, {})", 
                       monster.getName(), newX, newY);
            throw new RuntimeException("Position occupée par un joueur");
        }

        // Mouvement valide - mettre à jour la position
        monster.setPositionX(newX);
        monster.setPositionY(newY);
        
        Monster savedMonster = monsterRepository.save(monster);
        
        logger.info("Mouvement monstre validé - {} de ({}, {}) vers ({}, {})", 
                   monster.getName(), oldX, oldY, newX, newY);
        
        return savedMonster;
    }

    /**
     * Génère un mouvement aléatoire pour un monstre selon son pattern d'IA
     */
    public void performAIMovement(Monster monster, double zoneMinX, double zoneMaxX, double zoneMinY, double zoneMaxY) {
        try {
            double currentX = monster.getPositionX();
            double currentY = monster.getPositionY();
            
            double newX, newY;
            
            switch (monster.getAiPattern()) {
                case RANDOM_WALK:
                    // Mouvement complètement aléatoire
                    double angle = Math.random() * 2 * Math.PI;
                    double distance = Math.random() * monster.getMove();
                    
                    newX = currentX + Math.cos(angle) * distance;
                    newY = currentY + Math.sin(angle) * distance;
                    break;
                    
                case PATROL:
                    // Mouvement de patrouille (pour l'instant, comme random walk)
                    angle = Math.random() * 2 * Math.PI;
                    distance = Math.random() * monster.getMove();
                    
                    newX = currentX + Math.cos(angle) * distance;
                    newY = currentY + Math.sin(angle) * distance;
                    break;
                    
                default:
                    // Par défaut, mouvement aléatoire
                    angle = Math.random() * 2 * Math.PI;
                    distance = Math.random() * monster.getMove();
                    
                    newX = currentX + Math.cos(angle) * distance;
                    newY = currentY + Math.sin(angle) * distance;
            }
            
            // S'assurer que le monstre reste dans la zone
            newX = Math.max(zoneMinX, Math.min(zoneMaxX, newX));
            newY = Math.max(zoneMinY, Math.min(zoneMaxY, newY));
            
            // Tenter le mouvement
            processMonsterMove(monster.getId(), newX, newY, "zone1");
            
        } catch (Exception e) {
            // Mouvement échoué (collision ou autre), ne rien faire
            logger.debug("Mouvement IA échoué pour {}: {}", monster.getName(), e.getMessage());
        }
    }

    /**
     * Met à jour l'état d'un monstre
     */
    public Monster updateMonsterState(Long monsterId, MonsterState newState) {
        Optional<Monster> optionalMonster = monsterRepository.findById(monsterId);
        if (optionalMonster.isEmpty()) {
            throw new RuntimeException("Monstre non trouvé: " + monsterId);
        }

        Monster monster = optionalMonster.get();
        monster.setState(newState);
        return monsterRepository.save(monster);
    }

    /**
     * Trouve tous les monstres dans une zone
     */
    public List<Monster> getMonstersInArea(double minX, double maxX, double minY, double maxY) {
        return monsterRepository.findMonstersInArea(minX, maxX, minY, maxY);
    }

    /**
     * Trouve tous les monstres vivants
     */
    public List<Monster> getAllAliveMonsters() {
        return monsterRepository.findAllAlive();
    }
}