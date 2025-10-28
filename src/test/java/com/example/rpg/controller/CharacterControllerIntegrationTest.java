package com.example.rpg.controller;

import com.example.rpg.dto.RegisterRequest;
import com.example.rpg.dto.LoginRequest;
import com.example.rpg.dto.CreateCharacterRequest;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.example.test.TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CharacterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @BeforeEach
    void setUp() {
        characterRepository.deleteAll();
        utilisateurRepository.deleteAll();
    }

    private String registerAndLoginAndGetToken(String username) throws Exception {
        // Register
        RegisterRequest registerRequest = new RegisterRequest(username, username + "@example.com", "password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk());

        // Login
        LoginRequest loginRequest = new LoginRequest(username, "password123");
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()))
            .andReturn();

        String json = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(json);
        return node.get("token").asText();
    }

    @Test
    void createCharacter_success() throws Exception {
        String token = registerAndLoginAndGetToken("player1");

        CreateCharacterRequest req = new CreateCharacterRequest("HeroOne", CharacterClass.KNIGHT);
        mockMvc.perform(post("/api/character/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("HeroOne")))
            .andExpect(jsonPath("$.characterClass", is("KNIGHT")))
            .andExpect(jsonPath("$.currentHp", greaterThan(0)))
            .andExpect(jsonPath("$.message", containsString("créé")));
    }

    @Test
    void createCharacter_duplicateName_conflict() throws Exception {
        String token1 = registerAndLoginAndGetToken("player2");
        String token2 = registerAndLoginAndGetToken("player3");

        CreateCharacterRequest req1 = new CreateCharacterRequest("SameName", CharacterClass.BLACK_MAGE);
        mockMvc.perform(post("/api/character/create")
                .header("Authorization", "Bearer " + token1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1)))
            .andExpect(status().isOk());

        CreateCharacterRequest req2 = new CreateCharacterRequest("SameName", CharacterClass.THIEF);
        mockMvc.perform(post("/api/character/create")
                .header("Authorization", "Bearer " + token2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message", containsString("déjà utilisé")));
    }

    @Test
    void createCharacter_withoutAuth_forbidden() throws Exception {
        CreateCharacterRequest req = new CreateCharacterRequest("NoAuth", CharacterClass.KNIGHT);
        mockMvc.perform(post("/api/character/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isForbidden());
    }

    @Test
    void getMyCharacter_success_afterCreate() throws Exception {
        String token = registerAndLoginAndGetToken("player4");
        CreateCharacterRequest req = new CreateCharacterRequest("Solo", CharacterClass.THIEF);

        mockMvc.perform(post("/api/character/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/character/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Solo")))
            .andExpect(jsonPath("$.characterClass", is("THIEF")));
    }
}
