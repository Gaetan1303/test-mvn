package com.example.rpg.controller;

import com.example.rpg.dto.MovementRequest;
import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.PlayerState;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

@SpringBootTest(classes = com.example.test.TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class MassivePlayerMovementSimulationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testMassivePlayerMovementSimulation() throws Exception {
        System.out.println("\n🎮 === SIMULATION DE DÉPLACEMENT MASSIF ===");
        System.out.println("Joueurs: " + PLAYER_COUNT);
        System.out.println("Zone: " + ZONE_SIZE + "x" + ZONE_SIZE);
        System.out.println("Rounds de mouvement: " + MOVEMENT_ROUNDS);
        
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
                
                // Chaque joueur tente un mouvement simultanément
                for (Character character : characters) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            // Générer mouvement aléatoire dans la limite de move (max 3)
                            double currentX = character.getPositionX();
                            double currentY = character.getPositionY();
                            
                            // Mouvement aléatoire dans un rayon de 1-3 unités
                            double angle = Math.random() * 2 * Math.PI;
                            double distance = 1 + Math.random() * 2; // 1-3 unités
                            
                            double newX = currentX + Math.cos(angle) * distance;
                            double newY = currentY + Math.sin(angle) * distance;
                            
                            // Maintenir dans les limites de la zone
                            newX = Math.max(0, Math.min(ZONE_SIZE - 1, newX));
                            newY = Math.max(0, Math.min(ZONE_SIZE - 1, newY));
                            
                            MovementRequest request = new MovementRequest();
                            request.setNewX(newX);
                            request.setNewY(newY);
                            
                            totalMovements.incrementAndGet();
                            
                            // Tenter le mouvement via API REST
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            HttpEntity<MovementRequest> entity = new HttpEntity<>(request, headers);
                            
                            var response = restTemplate.exchange(
                                "/api/game/move/" + character.getId(),
                                HttpMethod.POST,
                                entity,
                                String.class
                            );
                            
                            String responseBody = response.getBody();
                            int status = response.getStatusCode().value();
                            
                            if (status == 200) {
                                successfulMovements.incrementAndGet();
                                // Mettre à jour position locale pour éviter conflits
                                character.setPositionX(newX);
                                character.setPositionY(newY);
                            } else if (responseBody != null && responseBody.contains("Position occupée")) {
                                collisionDetected.incrementAndGet();
                            } else if (responseBody != null && responseBody.contains("distance de mouvement")) {
                                distanceViolations.incrementAndGet();
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
            String posKey = character.getPositionX() + "," + character.getPositionY();
            positionCounts.put(posKey, positionCounts.getOrDefault(posKey, 0) + 1);
        }
        
        long duplicatePositions = positionCounts.values().stream()
            .filter(count -> count > 1)
            .count();
        
        System.out.println("Positions dupliquées détectées: " + duplicatePositions);
        
        // Assertions
        assertTrue(totalMovements.get() > 0, "Aucun mouvement tenté");
        assertTrue(successfulMovements.get() > 0, "Aucun mouvement réussi");
        assertTrue(collisionDetected.get() > 0, "Aucune collision détectée - le système ne fonctionne pas");
        assertEquals(0, duplicatePositions, "Des positions dupliquées ont été trouvées en base");
        assertTrue(successRate > 20, "Taux de succès trop faible: " + successRate + "%");
        
        System.out.println("\n✅ Test de simulation massif réussi !");
    }
}