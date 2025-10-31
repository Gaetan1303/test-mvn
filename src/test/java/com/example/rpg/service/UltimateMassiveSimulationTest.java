package com.example.rpg.service;

import com.example.rpg.model.*;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.MonsterRepository;
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
class UltimateMassiveSimulationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private MonsterService monsterService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private static final int PLAYER_COUNT = 100;
    private static final int MONSTER_COUNT = 40;
    private static final double ZONE_SIZE = 80.0; // Zone encore plus large pour 140 entités
    private static final int MOVEMENT_ROUNDS = 5;

    @Test
    void testUltimateMassiveSimulation() throws Exception {
        System.out.println("\n🔥 === SIMULATION ULTIME MASSIVE ===");
        System.out.println("🧙‍♂️ Joueurs: " + PLAYER_COUNT);
        System.out.println("👹 Monstres: " + MONSTER_COUNT);
        System.out.println("📊 Total entités: " + (PLAYER_COUNT + MONSTER_COUNT));
        System.out.println("🗺️ Zone: " + ZONE_SIZE + "x" + ZONE_SIZE);
        System.out.println("🔄 Rounds: " + MOVEMENT_ROUNDS);
        
        // 1. Créer utilisateur de test
        Utilisateur testUser = new Utilisateur();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        testUser = utilisateurRepository.save(testUser);
        System.out.println("✅ Utilisateur créé: " + testUser.getId());
        
        // 2. Créer les 100 joueurs
        List<com.example.rpg.model.Character> characters = createPlayers(testUser);
        System.out.println("✅ " + characters.size() + " joueurs créés avec succès !");
        
        // 3. Créer les 40 monstres
        List<Monster> monsters = createMonsters();
        System.out.println("✅ " + monsters.size() + " monstres créés avec succès !");
        
        System.out.println("🎮 === DÉBUT DE LA SIMULATION ===");
        
        // 4. Statistiques de simulation
        AtomicInteger totalPlayerMovements = new AtomicInteger(0);
        AtomicInteger successfulPlayerMovements = new AtomicInteger(0);
        AtomicInteger playerCollisions = new AtomicInteger(0);
        AtomicInteger playerErrors = new AtomicInteger(0);
        
        AtomicInteger totalMonsterMovements = new AtomicInteger(0);
        AtomicInteger successfulMonsterMovements = new AtomicInteger(0);
        AtomicInteger monsterCollisions = new AtomicInteger(0);
        AtomicInteger monsterErrors = new AtomicInteger(0);
        
        // 5. Simulation avec ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(30); // Plus de threads pour 140 entités
        
        long startTime = System.currentTimeMillis();
        
        try {
            for (int round = 1; round <= MOVEMENT_ROUNDS; round++) {
                System.out.println("\n⚔️ Round " + round + "/" + MOVEMENT_ROUNDS);
                
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                
                // Mouvements des joueurs
                for (com.example.rpg.model.Character character : characters) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        movePlayer(character, totalPlayerMovements, successfulPlayerMovements, 
                                 playerCollisions, playerErrors);
                    }, executor);
                    futures.add(future);
                }
                
                // Mouvements des monstres
                for (Monster monster : monsters) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        moveMonster(monster, totalMonsterMovements, successfulMonsterMovements, 
                                  monsterCollisions, monsterErrors);
                    }, executor);
                    futures.add(future);
                }
                
                // Attendre la fin de tous les mouvements
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                
                // Statistiques du round
                System.out.println("  👥 Joueurs - Tentés: " + totalPlayerMovements.get() + 
                                 ", Réussis: " + successfulPlayerMovements.get() + 
                                 ", Collisions: " + playerCollisions.get());
                System.out.println("  👹 Monstres - Tentés: " + totalMonsterMovements.get() + 
                                 ", Réussis: " + successfulMonsterMovements.get() + 
                                 ", Collisions: " + monsterCollisions.get());
            }
            
        } finally {
            executor.shutdown();
            executor.awaitTermination(15, TimeUnit.SECONDS);
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // 6. Résultats finaux
        System.out.println("\n🏆 === RÉSULTATS FINAUX DE LA SIMULATION ULTIME ===");
        System.out.println("⏱️ Temps total d'exécution: " + totalTime + " ms");
        System.out.println();
        
        int totalMovements = totalPlayerMovements.get() + totalMonsterMovements.get();
        int totalSuccessful = successfulPlayerMovements.get() + successfulMonsterMovements.get();
        int totalCollisions = playerCollisions.get() + monsterCollisions.get();
        int totalErrors = playerErrors.get() + monsterErrors.get();
        
        System.out.println("📊 STATISTIQUES GLOBALES:");
        System.out.println("  Total entités: " + (PLAYER_COUNT + MONSTER_COUNT));
        System.out.println("  Total mouvements tentés: " + totalMovements);
        System.out.println("  Mouvements réussis: " + totalSuccessful);
        System.out.println("  Collisions détectées: " + totalCollisions);
        System.out.println("  Erreurs: " + totalErrors);
        
        if (totalMovements > 0) {
            double successRate = (double) totalSuccessful / totalMovements * 100;
            System.out.println("  Taux de succès: " + String.format("%.1f", successRate) + "%");
            
            double throughput = (double) totalMovements / (totalTime / 1000.0);
            System.out.println("  Débit: " + String.format("%.1f", throughput) + " mouvements/seconde");
        }
        
        System.out.println();
        System.out.println("👥 DÉTAIL JOUEURS:");
        System.out.println("  Mouvements tentés: " + totalPlayerMovements.get());
        System.out.println("  Mouvements réussis: " + successfulPlayerMovements.get());
        System.out.println("  Collisions: " + playerCollisions.get());
        System.out.println("  Erreurs: " + playerErrors.get());
        
        System.out.println();
        System.out.println("👹 DÉTAIL MONSTRES:");
        System.out.println("  Mouvements tentés: " + totalMonsterMovements.get());
        System.out.println("  Mouvements réussis: " + successfulMonsterMovements.get());
        System.out.println("  Collisions: " + monsterCollisions.get());
        System.out.println("  Erreurs: " + monsterErrors.get());
        
        // 7. Vérifications finales d'intégrité
        List<com.example.rpg.model.Character> finalCharacters = characterRepository.findAll();
        List<Monster> finalMonsters = monsterRepository.findAll();
        
        System.out.println();
        System.out.println("🔍 VÉRIFICATION D'INTÉGRITÉ:");
        System.out.println("  Joueurs en base: " + finalCharacters.size());
        System.out.println("  Monstres en base: " + finalMonsters.size());
        
        // Vérifier les positions dupliquées (joueurs vs joueurs)
        long duplicatePlayerPositions = finalCharacters.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                c -> String.format("%.1f,%.1f", c.getPositionX(), c.getPositionY()),
                java.util.stream.Collectors.counting()))
            .values().stream()
            .filter(count -> count > 1)
            .count();
        
        // Vérifier les positions dupliquées (monstres vs monstres)
        long duplicateMonsterPositions = finalMonsters.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                m -> String.format("%.1f,%.1f", m.getPositionX(), m.getPositionY()),
                java.util.stream.Collectors.counting()))
            .values().stream()
            .filter(count -> count > 1)
            .count();
        
        // Vérifier les collisions joueur-monstre
        long playerMonsterCollisions = 0;
        for (com.example.rpg.model.Character character : finalCharacters) {
            String charPos = String.format("%.1f,%.1f", character.getPositionX(), character.getPositionY());
            for (Monster monster : finalMonsters) {
                String monsterPos = String.format("%.1f,%.1f", monster.getPositionX(), monster.getPositionY());
                if (charPos.equals(monsterPos)) {
                    playerMonsterCollisions++;
                    break; // Une collision par joueur max
                }
            }
        }
        
        System.out.println("  Positions dupliquées (joueurs): " + duplicatePlayerPositions);
        System.out.println("  Positions dupliquées (monstres): " + duplicateMonsterPositions);
        System.out.println("  Collisions joueur-monstre: " + playerMonsterCollisions);
        
        // 8. Assertions
        assertTrue(totalMovements > 0, "Aucun mouvement tenté");
        assertTrue(totalSuccessful > 0, "Aucun mouvement réussi");
        assertTrue(totalCollisions >= 0, "Les collisions doivent être >= 0");
        assertEquals(0, duplicatePlayerPositions, "Aucune position dupliquée autorisée entre joueurs");
        assertEquals(0, duplicateMonsterPositions, "Aucune position dupliquée autorisée entre monstres");
        assertEquals(0, playerMonsterCollisions, "Aucune collision joueur-monstre autorisée");
        assertTrue(totalErrors < totalMovements * 0.1, "Trop d'erreurs: " + totalErrors);
        
        double successRate = (double) totalSuccessful / totalMovements * 100;
        assertTrue(successRate > 20, "Taux de succès trop faible: " + successRate + "%");
        
        System.out.println("\n🎉 === TEST ULTIME RÉUSSI ! ===");
        System.out.println("✅ " + (PLAYER_COUNT + MONSTER_COUNT) + " entités ont effectué " + totalMovements + " mouvements");
        System.out.println("✅ Système de collision validé avec " + totalCollisions + " collisions détectées");
        System.out.println("✅ Intégrité des données maintenue (0 position dupliquée)");
        System.out.println("✅ Performance: " + String.format("%.1f", (double) totalMovements / (totalTime / 1000.0)) + " mouvements/sec");
        System.out.println("🚀 Votre MMO peut gérer " + (PLAYER_COUNT + MONSTER_COUNT) + " entités simultanément !");
    }

    private List<com.example.rpg.model.Character> createPlayers(Utilisateur testUser) {
        List<com.example.rpg.model.Character> characters = new ArrayList<>();
        int gridSize = (int) Math.ceil(Math.sqrt(PLAYER_COUNT + MONSTER_COUNT));
        double spacing = ZONE_SIZE / gridSize;
        
        for (int i = 0; i < PLAYER_COUNT; i++) {
            com.example.rpg.model.Character character = new com.example.rpg.model.Character();
            character.setName("Player" + (i + 1));
            character.setCharacterClass(CharacterClass.values()[i % CharacterClass.values().length]);
            character.setState(PlayerState.HUB);
            character.setUtilisateur(testUser);
            
            // Position dans une grille élargie
            int row = i / gridSize;
            int col = i % gridSize;
            double startX = col * spacing + Math.random() * 2;
            double startY = row * spacing + Math.random() * 2;
            
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
            
            com.example.rpg.model.Character saved = characterRepository.save(character);
            characters.add(saved);
            
            if (i < 10 || (i + 1) % 20 == 0) {
                System.out.println("✅ Joueur " + (i + 1) + "/" + PLAYER_COUNT + " créé: " + saved.getName());
            }
        }
        
        return characters;
    }

    private List<Monster> createMonsters() {
        List<Monster> monsters = new ArrayList<>();
        int gridSize = (int) Math.ceil(Math.sqrt(PLAYER_COUNT + MONSTER_COUNT));
        double spacing = ZONE_SIZE / gridSize;
        
        for (int i = 0; i < MONSTER_COUNT; i++) {
            Monster monster = new Monster();
            monster.setName(MonsterType.values()[i % MonsterType.values().length].name() + "_" + (i + 1));
            monster.setMonsterType(MonsterType.values()[i % MonsterType.values().length]);
            monster.setState(MonsterState.PATROLLING);
            monster.setAiPattern(AIPattern.RANDOM_WALK);
            
            // Position dans la partie droite de la grille pour séparer des joueurs
            int row = i / gridSize;
            int col = (i % gridSize) + gridSize/2; // Décalage pour séparer des joueurs
            double startX = col * spacing + Math.random() * 2;
            double startY = row * spacing + Math.random() * 2;
            
            startX = Math.max(1, Math.min(ZONE_SIZE - 1, startX));
            startY = Math.max(1, Math.min(ZONE_SIZE - 1, startY));
            
            monster.setPositionX(startX);
            monster.setPositionY(startY);
            
            // Stats de base
            monster.setLevel(1);
            monster.setMaxHp(80);
            monster.setCurrentHp(80);
            monster.setAttackPower(15);
            monster.setDefense(8);
            monster.setSpeed(2);
            monster.setMove(2); // Monstres plus lents que les joueurs
            
            Monster saved = monsterRepository.save(monster);
            monsters.add(saved);
            
            if (i < 5 || (i + 1) % 10 == 0) {
                System.out.println("✅ Monstre " + (i + 1) + "/" + MONSTER_COUNT + " créé: " + saved.getName());
            }
        }
        
        return monsters;
    }

    private void movePlayer(com.example.rpg.model.Character character, AtomicInteger totalMovements, 
                          AtomicInteger successfulMovements, AtomicInteger collisions, 
                          AtomicInteger errors) {
        try {
            com.example.rpg.model.Character currentChar = characterRepository.findById(character.getId()).orElse(null);
            if (currentChar == null) {
                errors.incrementAndGet();
                return;
            }
            
            double currentX = currentChar.getPositionX();
            double currentY = currentChar.getPositionY();
            
            double angle = Math.random() * 2 * Math.PI;
            double distance = 1.0 + Math.random() * 2.0;
            
            double newX = currentX + Math.cos(angle) * distance;
            double newY = currentY + Math.sin(angle) * distance;
            
            newX = Math.max(0, Math.min(ZONE_SIZE - 1, newX));
            newY = Math.max(0, Math.min(ZONE_SIZE - 1, newY));
            
            totalMovements.incrementAndGet();
            
            try {
                com.example.rpg.model.Character result = gameService.processMove(character.getId(), newX, newY, "zone1");
                if (result != null) {
                    successfulMovements.incrementAndGet();
                }
            } catch (Exception e) {
                if (e.getMessage().contains("Position occupée")) {
                    collisions.incrementAndGet();
                } else {
                    errors.incrementAndGet();
                }
            }
            
        } catch (Exception e) {
            errors.incrementAndGet();
        }
    }

    private void moveMonster(Monster monster, AtomicInteger totalMovements, 
                           AtomicInteger successfulMovements, AtomicInteger collisions, 
                           AtomicInteger errors) {
        try {
            Monster currentMonster = monsterRepository.findById(monster.getId()).orElse(null);
            if (currentMonster == null) {
                errors.incrementAndGet();
                return;
            }
            
            double currentX = currentMonster.getPositionX();
            double currentY = currentMonster.getPositionY();
            
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.random() * currentMonster.getMove();
            
            double newX = currentX + Math.cos(angle) * distance;
            double newY = currentY + Math.sin(angle) * distance;
            
            newX = Math.max(0, Math.min(ZONE_SIZE - 1, newX));
            newY = Math.max(0, Math.min(ZONE_SIZE - 1, newY));
            
            totalMovements.incrementAndGet();
            
            try {
                Monster result = monsterService.processMonsterMove(monster.getId(), newX, newY, "zone1");
                if (result != null) {
                    successfulMovements.incrementAndGet();
                }
            } catch (Exception e) {
                if (e.getMessage().contains("Position occupée")) {
                    collisions.incrementAndGet();
                } else {
                    errors.incrementAndGet();
                }
            }
            
        } catch (Exception e) {
            errors.incrementAndGet();
        }
    }
}