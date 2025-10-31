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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration pour la détection de collisions via l'API WebSocket/REST
 */
@SpringBootTest(classes = com.example.test.TestApplication.class)
@ActiveProfiles("test")
@Transactional
public class MovementCollisionIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
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
        
        // Initialiser toutes les statistiques requises
        character.setLevel(1);
        character.setExperience(0);
        character.setMaxHp(100);
        character.setCurrentHp(100);
        character.setMaxMp(50);
        character.setCurrentMp(50);
        character.setPa(15);
        character.setMa(10);
        character.setPDef(10);
        character.setMDef(8);
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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Créer les utilisateurs de test
        user1 = new Utilisateur();
        user1.setUsername("testuser1");
        user1.setEmail("test1@example.com");
        user1.setPassword("password");
        user1.setEnabled(true);
        user1 = utilisateurRepository.save(user1);

        user2 = new Utilisateur();
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password");
        user2.setEnabled(true);
        user2 = utilisateurRepository.save(user2);

        // Créer les personnages
        character1 = createTestCharacter("TestHero1", CharacterClass.KNIGHT, user1, 5.0, 5.0);
        character1 = characterRepository.save(character1);

        character2 = createTestCharacter("TestHero2", CharacterClass.MONK, user2, 10.0, 10.0);
        character2 = characterRepository.save(character2);
    }

    @Test
    void testMovementAllowedToEmptyPosition() throws Exception {
        // Test : Mouvement vers une position libre (dans la limite de mouvement) devrait réussir
        MovementRequest moveRequest = new MovementRequest(7.0, 7.0, "world_1"); // Distance de ~2.8, dans la limite de 3
        String jsonRequest = objectMapper.writeValueAsString(moveRequest);

        mockMvc.perform(post("/api/game/move/{characterId}", character1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mouvement effectué avec succès"));
    }

    @Test
    void testMovementBlockedByCollision() throws Exception {
        // Test : Mouvement vers une position occupée devrait être bloqué
        MovementRequest moveRequest = new MovementRequest(10.0, 10.0, "world_1"); // Position de character2
        String jsonRequest = objectMapper.writeValueAsString(moveRequest);

        mockMvc.perform(post("/api/game/move/{characterId}", character1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Position occupée par un autre joueur"));
    }
}