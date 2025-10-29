package com.example.rpg.strategy;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;

/**
 * Pattern Strategy pour l'initialisation des stats de personnage
 * Respecte le principe O (Open/Closed) : extensible sans modification
 * Respecte le principe S (Single Responsibility) : responsable uniquement de l'init des stats
 */
public interface StatsInitializer {

    /**
     * Initialise les statistiques d'un personnage selon sa classe
     * 
     * @param character Le personnage à initialiser
     * @param characterClass La classe du personnage
     */
    void initializeStats(Character character, CharacterClass characterClass);

    /**
     * Vérifie si cette stratégie supporte la classe donnée
     * 
     * @param characterClass La classe à vérifier
     * @return true si cette stratégie peut initialiser cette classe
     */
    boolean supports(CharacterClass characterClass);
}
