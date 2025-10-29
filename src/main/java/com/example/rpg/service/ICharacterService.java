package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;

import java.util.List;

/**
 * Interface du service de gestion des personnages
 * Respecte le principe I (Interface Segregation) : contrat minimal et clair
 * Respecte le principe D (Dependency Inversion) : dépendre d'abstraction, pas d'implémentation
 */
public interface ICharacterService {

    /**
     * Crée un nouveau personnage
     * 
     * @param username Nom d'utilisateur
     * @param characterName Nom du personnage
     * @param characterClass Classe du personnage
     * @return Le personnage créé
     */
    Character createCharacter(String username, String characterName, CharacterClass characterClass);

    /**
     * Récupère tous les personnages d'un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Liste des personnages
     */
    List<Character> getAllCharactersByUsername(String username);

    /**
     * Récupère un personnage par son ID
     * 
     * @param username Nom d'utilisateur
     * @param characterId ID du personnage
     * @return Le personnage
     */
    Character getCharacterById(String username, Long characterId);

    /**
     * Vérifie si un utilisateur a au moins un personnage
     * 
     * @param username Nom d'utilisateur
     * @return true si l'utilisateur a au moins un personnage
     */
    boolean hasCharacter(String username);

    /**
     * Met à jour la position d'un personnage
     * 
     * @param username Nom d'utilisateur
     * @param x Position X
     * @param y Position Y
     * @return Le personnage mis à jour
     */
    Character updatePosition(String username, Double x, Double y);

    /**
     * Supprime un personnage par son ID
     * 
     * @param username Nom d'utilisateur
     * @param characterId ID du personnage
     */
    void deleteCharacterById(String username, Long characterId);
}
