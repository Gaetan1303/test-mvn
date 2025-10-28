package com.example.rpg.controller;

import com.example.rpg.dto.RegisterRequest;
import com.example.rpg.dto.LoginRequest;
import com.example.rpg.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests d'intégration pour les endpoints d'authentification
 */
@SpringBootTest(classes = com.example.test.TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    void setUp() {
        // Nettoyer la base avant chaque test
        utilisateurRepository.deleteAll();
    }

    // ========== TESTS INSCRIPTION ==========

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Utilisateur créé avec succès")))
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void testRegisterDuplicateUsername() throws Exception {
        // Premier utilisateur
        RegisterRequest firstRequest = new RegisterRequest("testuser", "test1@example.com", "password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Tentative avec même username
        RegisterRequest duplicateRequest = new RegisterRequest("testuser", "test2@example.com", "password456");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Conflit")))
                .andExpect(jsonPath("$.message", is("Nom d'utilisateur déjà utilisé")));
    }

    @Test
    void testRegisterDuplicateEmail() throws Exception {
        // Premier utilisateur
        RegisterRequest firstRequest = new RegisterRequest("user1", "test@example.com", "password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Tentative avec même email
        RegisterRequest duplicateRequest = new RegisterRequest("user2", "test@example.com", "password456");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Conflit")))
                .andExpect(jsonPath("$.message", is("Email déjà utilisé")));
    }

    @Test
    void testRegisterInvalidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser", "invalid-email", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation échouée")))
                .andExpect(jsonPath("$.errors.email", is("Format d'email invalide")));
    }

    @Test
    void testRegisterUsernameTooShort() throws Exception {
        RegisterRequest request = new RegisterRequest("ab", "test@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation échouée")))
                .andExpect(jsonPath("$.errors.username", containsString("entre 3 et 50 caractères")));
    }

    @Test
    void testRegisterPasswordTooShort() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "12345");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation échouée")))
                .andExpect(jsonPath("$.errors.password", containsString("au moins 6 caractères")));
    }

    @Test
    void testRegisterMissingFields() throws Exception {
        RegisterRequest request = new RegisterRequest("", "", "");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation échouée")))
                .andExpect(jsonPath("$.errors.username", notNullValue()))
                .andExpect(jsonPath("$.errors.email", notNullValue()))
                .andExpect(jsonPath("$.errors.password", notNullValue()));
    }

    // ========== TESTS CONNEXION ==========

    @Test
    void testLoginSuccess() throws Exception {
        // Créer un utilisateur d'abord
        RegisterRequest registerRequest = new RegisterRequest("testuser", "test@example.com", "password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Se connecter
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Connexion réussie")))
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void testLoginInvalidUsername() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent", "password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Non autorisé")))
                .andExpect(jsonPath("$.message", is("Utilisateur non trouvé")));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        // Créer un utilisateur
        RegisterRequest registerRequest = new RegisterRequest("testuser", "test@example.com", "password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Tenter connexion avec mauvais mot de passe
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Non autorisé")))
                .andExpect(jsonPath("$.message", is("Mot de passe invalide")));
    }

    @Test
    void testLoginMissingFields() throws Exception {
        LoginRequest request = new LoginRequest("", "");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation échouée")));
    }
}
