package com.example.rpg.model;

/**
 * Patterns d'IA pour le comportement des monstres
 */
public enum AIPattern {
    RANDOM_WALK,    // Déplacement aléatoire
    PATROL,         // Patrouille entre points fixes
    GUARD,          // Garde une zone spécifique
    AGGRESSIVE,     // Attaque les joueurs à vue
    PASSIVE,        // N'attaque que si attaqué
    TERRITORIAL     // Défend son territoire
}