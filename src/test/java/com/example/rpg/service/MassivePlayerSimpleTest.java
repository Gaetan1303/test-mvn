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
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
class MassivePlayerSimpleTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private static final int PLAYER_COUNT = 10; // Réduisons à 10 pour test
    private static final double ZONE_SIZE = 10.0;
    private static final int MOVEMENT_ROUNDS = 3;

    @Test
    void testMassivePlayerMovementSimple() throws Exception {
        System.out.println("\n🎮 === SIMULATION SIMPLE DE DÉPLACEMENT MASSIF ===");
        System.out.println("Joueurs: " + PLAYER_COUNT);
        System.out.println("Zone: " + ZONE_SIZE + "x" + ZONE_SIZE);
        
        // 1. Créer utilisateur de test
        Utilisateur testUser = new Utilisateur();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        testUser = utilisateurRepository.save(testUser);
        
        // 2. Créer les personnages directement dans le test
        List<Character> characters = new ArrayList<>();
        for (int i = 1; i <= PLAYER_COUNT; i++) {
            Character character = new Character();
            character.setName("Hero" + i);
            character.setCharacterClass(CharacterClass.values()[i % CharacterClass.values().length]);
            character.setState(PlayerState.HUB);
            character.setUtilisateur(testUser);
            
            // Position aléatoire dans la zone
            double startX = Math.random() * ZONE_SIZE;
            double startY = Math.random() * ZONE_SIZE;
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
            
            Character savedChar = characterRepository.save(character);
            characters.add(savedChar);
            
            System.out.println("✅ Créé personnage ID: " + savedChar.getId() + " - " + savedChar.getName() + 
                " à position (" + String.format("%.1f", startX) + ", " + String.format("%.1f", startY) + ")");
        }
        
        // 3. Vérifier que les personnages sont bien en base
        List<Character> fromDb = characterRepository.findAll();
        System.out.println("🔍 Personnages trouvés en base: " + fromDb.size());
        
        // 4. Statistiques de simulation
        AtomicInteger totalMovements = new AtomicInteger(0);
        AtomicInteger successfulMovements = new AtomicInteger(0);
        AtomicInteger collisionDetected = new AtomicInteger(0);
        AtomicInteger distanceViolations = new AtomicInteger(0);
        
        // 5. Simulation avec ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
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
                                System.out.println("❌ Personnage non trouvé: " + character.getId());
                                return;
                            }
                            
                            // Mouvement aléatoire de 1-2 unités
                            double currentX = currentChar.getPositionX();
                            double currentY = currentChar.getPositionY();
                            
                            double angle = Math.random() * 2 * Math.PI;
                            double distance = 1.0 + Math.random(); // 1-2 unités
                            
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
                                    System.out.println("✅ " + result.getName() + " moved to (" + 
                                        String.format("%.1f", newX) + ", " + String.format("%.1f", newY) + ")");
                                }
                                
                            } catch (Exception e) {
                                if (e.getMessage().contains("Position occupée")) {
                                    collisionDetected.incrementAndGet();
                                    System.out.println("🔄 Collision détectée pour " + currentChar.getName());
                                } else if (e.getMessage().contains("distance")) {
                                    distanceViolations.incrementAndGet();
                                    System.out.println("⚠️ Violation distance pour " + currentChar.getName());
                                } else {
                                    System.out.println("❌ Erreur: " + e.getMessage());
                                }
                            }
                            
                        } catch (Exception e) {
                            System.out.println("❌ Erreur thread: " + e.getMessage());
                            e.printStackTrace();
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
            }
            
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        
        // 6. Résultats finaux
        System.out.println("\n📊 === RÉSULTATS FINAUX ===");
        System.out.println("Total mouvements tentés: " + totalMovements.get());
        System.out.println("Mouvements réussis: " + successfulMovements.get());
        System.out.println("Collisions détectées: " + collisionDetected.get());
        System.out.println("Violations distance: " + distanceViolations.get());
        
        if (totalMovements.get() > 0) {
            double successRate = (double) successfulMovements.get() / totalMovements.get() * 100;
            System.out.println("Taux de succès: " + String.format("%.1f", successRate) + "%");
        }
        
        // 7. Vérifications
        assertTrue(totalMovements.get() > 0, "Aucun mouvement tenté");
        assertTrue(successfulMovements.get() > 0, "Aucun mouvement réussi");
        // Note: On s'attend à des collisions avec plusieurs joueurs dans une petite zone
        
        System.out.println("\n✅ Test de simulation simple réussi !");
    }
}