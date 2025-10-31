package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.PlayerState;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.test.TestApplication.class)
@ActiveProfiles("test")
@Transactional
public class MassivePlayerServiceSimulationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private List<Character> characters = new ArrayList<>();
    private static final int PLAYER_COUNT = 50;
    private static final int ZONE_SIZE = 20; // Zone 20x20
    private static final int MOVEMENT_ROUNDS = 5;

    @BeforeEach
    void setUp() {
        // Créer 50 utilisateurs et personnages
        characters.clear();
        
        for (int i = 1; i <= PLAYER_COUNT; i++) {
            // Créer utilisateur
            Utilisateur user = new Utilisateur();
            user.setUsername("Player" + i);
            user.setEmail("player" + i + "@test.com");
            user.setPassword("password");
            user.setEnabled(true);
            user = utilisateurRepository.save(user);

            // Créer personnage à position aléatoire dans la zone
            Character character = new Character();
            character.setName("Hero" + i);
            character.setCharacterClass(CharacterClass.values()[i % CharacterClass.values().length]);
            character.setState(PlayerState.HUB);
            character.setUtilisateur(user);
            
            // Position aléatoire unique dans la zone 20x20
            double startX, startY;
            boolean positionFound = false;
            int attempts = 0;
            
            do {
                startX = Math.random() * ZONE_SIZE;
                startY = Math.random() * ZONE_SIZE;
                
                // Vérifier que la position n'est pas déjà occupée
                final double finalX = startX;
                final double finalY = startY;
                positionFound = characters.stream()
                    .noneMatch(c -> Math.abs(c.getPositionX() - finalX) < 0.1 && 
                                   Math.abs(c.getPositionY() - finalY) < 0.1);
                
                attempts++;
            } while (!positionFound && attempts < 100);
            
            character.setPositionX(startX);
            character.setPositionY(startY);
            
            // Stats de base avec les bonnes méthodes
            character.setLevel(1);
            character.setExperience(0);
            character.setMaxHp(100);
            character.setCurrentHp(100);
            character.setMaxMp(50);
            character.setCurrentMp(50);
            character.setPa(10);  // Physical Attack
            character.setPDef(5); // Physical Defense
            character.setMa(8);   // Magic Attack
            character.setMDef(6); // Magic Defense
            character.setSpeed(3);
            character.setMove(3);
            character.setHit(90);
            character.setEvade(10);
            character.setMagicHit(85);
            character.setMagicEvade(15);
            character.setCritRate(5);
            character.setDestiny(50);
            
            characters.add(characterRepository.save(character));
        }
        
        System.out.println("✅ Créé " + characters.size() + " joueurs dans une zone " + ZONE_SIZE + "x" + ZONE_SIZE);
    }

    @Test
    void testMassivePlayerMovementSimulationService() throws Exception {
        System.out.println("\n🎮 === SIMULATION DE DÉPLACEMENT MASSIF (SERVICE DIRECT) ===");
        System.out.println("Joueurs: " + PLAYER_COUNT);
        System.out.println("Zone: " + ZONE_SIZE + "x" + ZONE_SIZE);
        System.out.println("Rounds de mouvement: " + MOVEMENT_ROUNDS);
        
        // Récupérer les IDs réels des personnages fraîchement créés
        List<Character> realCharacters = characterRepository.findAll();
        System.out.println("✅ Trouvé " + realCharacters.size() + " personnages en base avec IDs: " + 
            realCharacters.stream().map(c -> c.getId().toString()).collect(java.util.stream.Collectors.joining(", ")));
        
        // Statistiques de simulation
        AtomicInteger totalMovements = new AtomicInteger(0);
        AtomicInteger successfulMovements = new AtomicInteger(0);
        AtomicInteger collisionDetected = new AtomicInteger(0);
        AtomicInteger distanceViolations = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        try {
            for (int round = 1; round <= MOVEMENT_ROUNDS; round++) {
                System.out.println("\n🔄 Round " + round + "/" + MOVEMENT_ROUNDS);
                
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                
                // Utiliser les personnages réels de la base de données
                for (Character character : realCharacters) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            // Récupérer la position actuelle depuis la base
                            Character currentChar = characterRepository.findById(character.getId()).orElse(null);
                            if (currentChar == null) {
                                System.out.println("❌ Personnage non trouvé: " + character.getId());
                                return;
                            }
                            
                            // Générer mouvement aléatoire dans la limite de move (max 3)
                            double currentX = currentChar.getPositionX();
                            double currentY = currentChar.getPositionY();
                            
                            // Mouvement aléatoire dans un rayon de 1-3 unités
                            double angle = Math.random() * 2 * Math.PI;
                            double distance = 1 + Math.random() * 2; // 1-3 unités
                            
                            double newX = currentX + Math.cos(angle) * distance;
                            double newY = currentY + Math.sin(angle) * distance;
                            
                            // Maintenir dans les limites de la zone
                            newX = Math.max(0, Math.min(ZONE_SIZE - 1, newX));
                            newY = Math.max(0, Math.min(ZONE_SIZE - 1, newY));
                            
                            totalMovements.incrementAndGet();
                            
                            // Tenter le mouvement directement via le service
                            try {
                                Character result = gameService.processMove(character.getId(), newX, newY, "zone1");
                                
                                if (result != null) {
                                    successfulMovements.incrementAndGet();
                                }
                                
                            } catch (Exception e) {
                                if (e.getMessage().contains("Position occupée")) {
                                    collisionDetected.incrementAndGet();
                                } else if (e.getMessage().contains("distance")) {
                                    distanceViolations.incrementAndGet();
                                } else {
                                    System.err.println("❌ Erreur unexpected: " + e.getMessage());
                                }
                            }
                            
                        } catch (Exception e) {
                            System.err.println("❌ Erreur mouvement " + character.getName() + ": " + e.getMessage());
                        }
                    }, executor);
                    
                    futures.add(future);
                }
                
                // Attendre que tous les mouvements du round soient terminés
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(30, TimeUnit.SECONDS);
                
                // Statistiques du round
                System.out.println("  Mouvements tentés: " + totalMovements.get());
                System.out.println("  Succès: " + successfulMovements.get());
                System.out.println("  Collisions détectées: " + collisionDetected.get());
                System.out.println("  Violations distance: " + distanceViolations.get());
                
                // Pause entre les rounds
                Thread.sleep(100);
            }
            
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        
        // Vérifications finales
        System.out.println("\n📊 === RÉSULTATS FINAUX ===");
        System.out.println("Total mouvements tentés: " + totalMovements.get());
        System.out.println("Mouvements réussis: " + successfulMovements.get());
        System.out.println("Collisions détectées: " + collisionDetected.get());
        System.out.println("Violations distance: " + distanceViolations.get());
        
        double successRate = (double) successfulMovements.get() / totalMovements.get() * 100;
        System.out.println("Taux de succès: " + String.format("%.1f", successRate) + "%");
        
        // Vérification de l'intégrité des positions
        List<Character> finalCharacters = characterRepository.findAll();
        Map<String, Integer> positionCounts = new HashMap<>();
        
        for (Character character : finalCharacters) {
            String posKey = String.format("%.1f,%.1f", character.getPositionX(), character.getPositionY());
            positionCounts.put(posKey, positionCounts.getOrDefault(posKey, 0) + 1);
        }
        
        long duplicatePositions = positionCounts.values().stream()
            .filter(count -> count > 1)
            .count();
        
        System.out.println("Positions dupliquées détectées: " + duplicatePositions);
        
        // Affichage des positions problématiques
        if (duplicatePositions > 0) {
            System.out.println("\n🚨 Positions dupliquées détectées:");
            positionCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .forEach(entry -> System.out.println("  Position " + entry.getKey() + ": " + entry.getValue() + " joueurs"));
        }
        
        // Assertions
        assertTrue(totalMovements.get() > 0, "Aucun mouvement tenté");
        assertTrue(successfulMovements.get() > 0, "Aucun mouvement réussi");
        assertTrue(collisionDetected.get() > 0, "Aucune collision détectée - le système ne fonctionne pas");
        assertEquals(0, duplicatePositions, "Des positions dupliquées ont été trouvées en base");
        assertTrue(successRate > 10, "Taux de succès trop faible: " + successRate + "%");
        
        System.out.println("\n✅ Test de simulation massif réussi !");
    }

    @Test
    void testConcurrentMovementToSamePositionService() throws Exception {
        System.out.println("\n⚡ === TEST DE CONCURRENCE EXTRÊME (SERVICE) ===");
        
        // Prendre les 10 premiers joueurs
        List<Character> testCharacters = characters.subList(0, 10);
        
        // Position cible commune
        double targetX = 10.0;
        double targetY = 10.0;
        
        AtomicInteger attempts = new AtomicInteger(0);
        AtomicInteger successes = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            // Tous les joueurs tentent de se déplacer vers la même position simultanément
            for (Character character : testCharacters) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        attempts.incrementAndGet();
                        
                        Character result = gameService.processMove(character.getId(), targetX, targetY, "zone1");
                        
                        if (result != null) {
                            successes.incrementAndGet();
                        }
                        
                    } catch (Exception e) {
                        // Les exceptions sont attendues pour les collisions
                        System.out.println("Collision attendue: " + e.getMessage());
                    }
                }, executor);
                
                futures.add(future);
            }
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(10, TimeUnit.SECONDS);
                
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        
        System.out.println("Tentatives simultanées: " + attempts.get());
        System.out.println("Succès: " + successes.get());
        
        // Vérification qu'un seul joueur a réussi
        long charactersAtTarget = characterRepository.findAll().stream()
            .filter(c -> Math.abs(c.getPositionX() - targetX) < 0.1 && 
                        Math.abs(c.getPositionY() - targetY) < 0.1)
            .count();
        
        System.out.println("Joueurs à la position cible: " + charactersAtTarget);
        
        assertTrue(successes.get() <= 1, "Un seul joueur maximum devrait réussir à atteindre la position");
        assertTrue(charactersAtTarget <= 1, "Un seul joueur maximum devrait être à la position cible");
        
        System.out.println("✅ Test de concurrence extrême réussi !");
    }
}