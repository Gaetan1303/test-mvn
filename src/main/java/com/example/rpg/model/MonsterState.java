package com.example.rpg.model;

/**
 * États possibles pour un monstre
 */
public enum MonsterState {
    IDLE,      // Au repos
    PATROLLING, // En patrouille
    CHASING,   // Poursuit un joueur
    FIGHTING,  // En combat
    FLEEING,   // En fuite
    DEAD       // Mort
}