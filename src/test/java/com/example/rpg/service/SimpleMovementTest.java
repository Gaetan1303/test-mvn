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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.test.TestApplication.class)
@ActiveProfiles("test")
class SimpleMovementTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Test
    void testSimpleSequentialMovement() throws Exception {
        System.out.println("\n🎮 === TEST SÉQUENTIEL SIMPLE ===");
        
        // 1. Créer utilisateur
        Utilisateur testUser = new Utilisateur();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        testUser = utilisateurRepository.save(testUser);
        System.out.println("✅ Utilisateur créé: " + testUser.getId());
        
        // 2. Créer 3 personnages
        List<Character> characters = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Character character = new Character();
            character.setName("Hero" + i);
            character.setCharacterClass(CharacterClass.KNIGHT);
            character.setState(PlayerState.HUB);
            character.setUtilisateur(testUser);
            character.setPositionX((double) i);
            character.setPositionY((double) i);
            
            // Stats
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
            System.out.println("✅ Personnage créé: " + saved.getId() + " - " + saved.getName() + 
                " à position (" + saved.getPositionX() + ", " + saved.getPositionY() + ")");
        }
        
        // 3. Vérifier que les personnages sont en base
        List<Character> fromDb = characterRepository.findAll();
        System.out.println("🔍 Total personnages en base: " + fromDb.size());
        
        int successfulMoves = 0;
        int totalMoves = 0;
        
        // 4. Tester des mouvements séquentiels (un par un)
        for (Character character : characters) {
            System.out.println("\n--- Test mouvement pour " + character.getName() + " ---");
            
            // Récupérer position actuelle
            Character current = characterRepository.findById(character.getId()).orElse(null);
            assertNotNull(current, "Personnage introuvable: " + character.getId());
            
            double oldX = current.getPositionX();
            double oldY = current.getPositionY();
            double newX = oldX + 1.0;
            double newY = oldY + 1.0;
            
            System.out.println("Position actuelle: (" + oldX + ", " + oldY + ")");
            System.out.println("Nouvelle position: (" + newX + ", " + newY + ")");
            
            totalMoves++;
            
            try {
                Character result = gameService.processMove(character.getId(), newX, newY, "zone1");
                
                if (result != null) {
                    successfulMoves++;
                    System.out.println("✅ Mouvement réussi: " + result.getName() + " maintenant à (" + 
                        result.getPositionX() + ", " + result.getPositionY() + ")");
                } else {
                    System.out.println("❌ Mouvement échoué (result null)");
                }
                
            } catch (Exception e) {
                System.out.println("❌ Exception: " + e.getMessage());
            }
        }
        
        // 5. Résultats
        System.out.println("\n📊 === RÉSULTATS ===");
        System.out.println("Total mouvements tentés: " + totalMoves);
        System.out.println("Mouvements réussis: " + successfulMoves);
        
        // Vérifications
        assertTrue(totalMoves > 0, "Aucun mouvement tenté");
        assertTrue(successfulMoves > 0, "Aucun mouvement réussi");
        
        System.out.println("\n✅ Test séquentiel réussi !");
    }
}