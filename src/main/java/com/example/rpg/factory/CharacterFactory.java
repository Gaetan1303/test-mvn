package com.example.rpg.factory;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.Utilisateur;

/**
 * Pattern Factory pour la création de personnages
 * Respecte le principe O (Open/Closed) : extensible sans modification
 * Respecte le principe S (Single Responsibility) : responsable uniquement de la création
 */
public interface CharacterFactory {

    /**
     * Crée un nouveau personnage avec les stats initialisées
     * 
     * @param name Nom du personnage
     * @param characterClass Classe du personnage
     * @param utilisateur Utilisateur propriétaire
     * @return Le personnage créé et initialisé
     */
    Character createCharacter(String name, CharacterClass characterClass, Utilisateur utilisateur);
}
