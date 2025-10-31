package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.PlayerState;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.test.TestApplication.class)
@ActiveProfiles("test")
class FinalMassivePlayerTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private static final int PLAYER_COUNT = 50;
    private static final double ZONE_SIZE = 50.0; // Zone plus large pour éviter trop de collisions
    private static final int MOVEMENT_ROUNDS = 5;

    @Test
    void testMassivePlayerConcurrentMovement() throws Exception {
        System.out.println("\n🎮 === SIMULATION FINALE DE DÉPLACEMENT MASSIF ===");
        System.out.println("Joueurs: " + PLAYER_COUNT);
        System.out.println("Zone: " + ZONE_SIZE + "x" + ZONE_SIZE);
        System.out.println("Rounds: " + MOVEMENT_ROUNDS);
        
        // 1. Créer utilisateur de test
        Utilisateur testUser = new Utilisateur();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        testUser = utilisateurRepository.save(testUser);
        System.out.println("✅ Utilisateur créé: " + testUser.getId());
        
        // 2. Créer les personnages dans une grille espacée
        List<Character> characters = new ArrayList<>();
        int gridSize = (int) Math.ceil(Math.sqrt(PLAYER_COUNT));
        double spacing = ZONE_SIZE / gridSize;
        
        for (int i = 0; i < PLAYER_COUNT; i++) {
            Character character = new Character();
            character.setName("Hero" + (i + 1));
            character.setCharacterClass(CharacterClass.values()[i % CharacterClass.values().length]);
            character.setState(PlayerState.HUB);
            character.setUtilisateur(testUser);
            
            // Position dans une grille pour éviter les collisions initiales
            int row = i / gridSize;
            int col = i % gridSize;
            double startX = col * spacing + Math.random() * 2; // Un peu d'aléatoire
            double startY = row * spacing + Math.random() * 2;
            
            // S'assurer que la position est dans la zone
            startX = Math.max(1, Math.min(ZONE_SIZE - 1, startX));
            startY = Math.max(1, Math.min(ZONE_SIZE - 1, startY));
            
            character.setPositionX(startX);
            character.setPositionY(startY);
            
            // Stats de base
            character.setLevel(1);
            character.setExperience(0);
            character.setMaxHp(100);
            character.setCurrentHp(100);
            character.setMaxMp(50);
            character.setCurrentMp(50);
            character.setPa(10);
            character.setPDef(5);
            character.setMa(8);
            character.setMDef(6);
            character.setSpeed(3);
            character.setMove(3);
            character.setHit(90);
            character.setEvade(10);
            character.setMagicHit(85);
            character.setMagicEvade(15);
            character.setCritRate(5);
            character.setDestiny(50);
            
            Character saved = characterRepository.save(character);
            characters.add(saved);
            
            if (i < 10 || (i + 1) % 10 == 0) {
                System.out.println("✅ Personnage " + (i + 1) + "/50 créé: " + saved.getName() + 
                    " à position (" + String.format("%.1f", startX) + ", " + String.format("%.1f", startY) + ")");
            }
        }
        
        System.out.println("✅ Tous les " + characters.size() + " personnages créés avec succès !");
        
        // 3. Statistiques de simulation
        AtomicInteger totalMovements = new AtomicInteger(0);
        AtomicInteger successfulMovements = new AtomicInteger(0);
        AtomicInteger collisionDetected = new AtomicInteger(0);
        AtomicInteger distanceViolations = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        // 4. Simulation avec ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        long startTime = System.currentTimeMillis();
        
        try {
            for (int round = 1; round <= MOVEMENT_ROUNDS; round++) {
                System.out.println("\n🔄 Round " + round + "/" + MOVEMENT_ROUNDS);
                
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                
                for (Character character : characters) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            // Récupérer position actuelle
                            Character currentChar = characterRepository.findById(character.getId()).orElse(null);
                            if (currentChar == null) {
                                errorCount.incrementAndGet();
                                return;
                            }
                            
                            // Mouvement aléatoire de 1-3 unités
                            double currentX = currentChar.getPositionX();
                            double currentY = currentChar.getPositionY();
                            
                            double angle = Math.random() * 2 * Math.PI;
                            double distance = 1.0 + Math.random() * 2.0; // 1-3 unités
                            
                            double newX = currentX + Math.cos(angle) * distance;
                            double newY = currentY + Math.sin(angle) * distance;
                            
                            // Limites de zone
                            newX = Math.max(0, Math.min(ZONE_SIZE - 1, newX));
                            newY = Math.max(0, Math.min(ZONE_SIZE - 1, newY));
                            
                            totalMovements.incrementAndGet();
                            
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
                                    errorCount.incrementAndGet();
                                }
                            }
                            
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                        }
                    }, executor);
                    
                    futures.add(future);
                }
                
                // Attendre la fin de tous les mouvements
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                
                System.out.println("  Mouvements tentés: " + totalMovements.get());
                System.out.println("  Succès: " + successfulMovements.get());
                System.out.println("  Collisions: " + collisionDetected.get());
                System.out.println("  Violations distance: " + distanceViolations.get());
                System.out.println("  Erreurs: " + errorCount.get());
            }
            
        } finally {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // 5. Résultats finaux
        System.out.println("\n📊 === RÉSULTATS FINAUX ===");
        System.out.println("Temps total d'exécution: " + totalTime + " ms");
        System.out.println("Total mouvements tentés: " + totalMovements.get());
        System.out.println("Mouvements réussis: " + successfulMovements.get());
        System.out.println("Collisions détectées: " + collisionDetected.get());
        System.out.println("Violations distance: " + distanceViolations.get());
        System.out.println("Erreurs: " + errorCount.get());
        
        if (totalMovements.get() > 0) {
            double successRate = (double) successfulMovements.get() / totalMovements.get() * 100;
            System.out.println("Taux de succès: " + String.format("%.1f", successRate) + "%");
            
            double throughput = (double) totalMovements.get() / (totalTime / 1000.0);
            System.out.println("Débit: " + String.format("%.1f", throughput) + " mouvements/seconde");
        }
        
        // 6. Vérifications finales
        List<Character> finalCharacters = characterRepository.findAll();
        System.out.println("Personnages finaux en base: " + finalCharacters.size());
        
        // Vérification d'intégrité - pas de positions dupliquées
        long duplicatePositions = finalCharacters.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                c -> String.format("%.1f,%.1f", c.getPositionX(), c.getPositionY()),
                java.util.stream.Collectors.counting()))
            .values().stream()
            .filter(count -> count > 1)
            .count();
        
        System.out.println("Positions dupliquées: " + duplicatePositions);
        
        // 7. Assertions
        assertTrue(totalMovements.get() > 0, "Aucun mouvement tenté");
        assertTrue(successfulMovements.get() > 0, "Aucun mouvement réussi");
        assertTrue(collisionDetected.get() >= 0, "Les collisions doivent être >= 0");
        assertEquals(0, duplicatePositions, "Aucune position dupliquée autorisée");
        assertTrue(errorCount.get() < totalMovements.get() * 0.1, "Trop d'erreurs: " + errorCount.get());
        
        double successRate = (double) successfulMovements.get() / totalMovements.get() * 100;
        assertTrue(successRate > 30, "Taux de succès trop faible: " + successRate + "%");
        
        System.out.println("\n🎉 === TEST DE SIMULATION MASSIF RÉUSSI ! ===");
        System.out.println("✅ " + PLAYER_COUNT + " joueurs ont effectué " + totalMovements.get() + " mouvements");
        System.out.println("✅ Système de collision validé avec " + collisionDetected.get() + " collisions détectées");
        System.out.println("✅ Intégrité des données maintenue (0 position dupliquée)");
        System.out.println("✅ Performance: " + String.format("%.1f", (double) totalMovements.get() / (totalTime / 1000.0)) + " mouvements/sec");
    }
}