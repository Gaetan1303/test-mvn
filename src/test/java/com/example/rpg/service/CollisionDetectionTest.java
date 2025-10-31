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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de détection de collisions pour les mouvements de personnages
 */
@SpringBootTest(classes = com.example.test.TestApplication.class)
@ActiveProfiles("test")
@Transactional
public class CollisionDetectionTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private GameService gameService;

    private Utilisateur user1;
    private Utilisateur user2;
    private Character character1;
    private Character character2;

    private Character createTestCharacter(String name, CharacterClass characterClass, Utilisateur user, double x, double y) {
        Character character = new Character();
        character.setName(name);
        character.setCharacterClass(characterClass);
        character.setState(PlayerState.HUB);
        character.setPositionX(x);
        character.setPositionY(y);
        character.setUtilisateur(user);
        // Note: createdAt et updatedAt sont gérés automatiquement par @PrePersist et @PreUpdate
        
        // Initialiser toutes les statistiques requises avec les noms corrects
        character.setLevel(1);
        character.setExperience(0);
        character.setMaxHp(100);
        character.setCurrentHp(100);
        character.setMaxMp(50);
        character.setCurrentMp(50);
        character.setPa(15);       // Physical Attack
        character.setMa(10);       // Magic Attack  
        character.setPDef(10);     // Physical Defense
        character.setMDef(8);      // Magic Defense
        character.setSpeed(12);
        character.setMove(3);
        character.setHit(95);
        character.setEvade(15);
        character.setMagicHit(90);
        character.setMagicEvade(12);
        character.setCritRate(5);
        character.setDestiny(20);
        
        return character;
    }
    
    @BeforeEach
    void setup() {
        user1 = new Utilisateur();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setEnabled(true);
        // Note: createdAt est géré automatiquement par @PrePersist
        user1 = utilisateurRepository.save(user1);

        user2 = new Utilisateur();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setEnabled(true);
        // Note: createdAt est géré automatiquement par @PrePersist
        user2 = utilisateurRepository.save(user2);

        // Create characters with full stats
        character1 = createTestCharacter("Hero1", CharacterClass.KNIGHT, user1, 1.0, 1.0);
        character1 = characterRepository.save(character1);

        character2 = createTestCharacter("Hero2", CharacterClass.MONK, user2, 2.0, 2.0);
        character2 = characterRepository.save(character2);
    }

    @Test
    void testCollisionDetection_SamePosition() {
        // Test: Déplacer character2 vers la position de character1 (collision)
        boolean result = gameService.isPositionOccupied(1.0, 1.0, character2.getId());
        
        assertThat(result).isTrue();
    }

    @Test
    void testCollisionDetection_DifferentPositions() {
        // Test: Position libre
        boolean result = gameService.isPositionOccupied(5.0, 5.0, character2.getId());
        
        assertThat(result).isFalse();
    }

    @Test
    void testCollisionDetection_SameCharacterCanStayAtPosition() {
        // Test: Un personnage peut rester à sa propre position
        boolean result = gameService.isPositionOccupied(1.0, 1.0, character1.getId());
        
        assertThat(result).isFalse();
    }

    @Test
    void testCollisionDetection_MultipleCharactersScenario() {
        // Créer un troisième personnage pour un test plus complexe
        Utilisateur user3 = new Utilisateur();
        user3.setUsername("user3");
        user3.setEmail("user3@example.com");
        user3.setPassword("password");
        user3.setEnabled(true);
        // Note: createdAt est géré automatiquement par @PrePersist
        user3 = utilisateurRepository.save(user3);

        Character character3 = createTestCharacter("Hero3", CharacterClass.WHITE_MAGE, user3, 3.0, 3.0);
        character3 = characterRepository.save(character3);

        // Vérifier les collisions
        assertThat(gameService.isPositionOccupied(1.0, 1.0, character3.getId())).isTrue();  // Character1 position
        assertThat(gameService.isPositionOccupied(2.0, 2.0, character3.getId())).isTrue();  // Character2 position
        assertThat(gameService.isPositionOccupied(3.0, 3.0, character3.getId())).isFalse(); // Character3 own position
        assertThat(gameService.isPositionOccupied(4.0, 4.0, character3.getId())).isFalse(); // Free position
    }
}