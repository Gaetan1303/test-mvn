package com.example.rpg.service;

/**
 * Interface du service d'authentification
 * Respecte le principe I (Interface Segregation) : contrat minimal et clair
 * Respecte le principe D (Dependency Inversion) : dépendre d'abstraction, pas d'implémentation
 */
public interface IAuthService {

    /**
     * Inscrit un nouvel utilisateur
     * 
     * @param username Nom d'utilisateur
     * @param email Email de l'utilisateur
     * @param password Mot de passe en clair
     * @return Token JWT
     */
    String register(String username, String email, String password);

    /**
     * Authentifie un utilisateur existant
     * 
     * @param username Nom d'utilisateur
     * @param password Mot de passe en clair
     * @return Token JWT
     */
    String login(String username, String password);
}
