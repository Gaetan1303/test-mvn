package com.example.rpg.model;

/**
 * États possibles du joueur dans le jeu
 * Basé sur le diagramme UML_etat_global.md
 */
public enum PlayerState {
    /**
     * Joueur dans le hub principal (zone sûre)
     */
    HUB,

    /**
     * Joueur en combat
     */
    COMBAT,

    /**
     * Joueur en interaction sociale (dialogue, échange)
     */
    SOCIAL,

    /**
     * Joueur mort (HP <= 0)
     */
    MORT
}
