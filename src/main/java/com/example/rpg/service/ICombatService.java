package com.example.rpg.service;

import com.example.rpg.model.Character;

/**
 * Interface du service de combat
 * Respecte le principe S (Single Responsibility) : responsable uniquement de la logique de combat
 * Respecte le principe I (Interface Segregation) : contrat minimal et clair
 */
public interface ICombatService {

    /**
     * Applique des dégâts à un personnage
     * 
     * @param character Le personnage qui subit les dégâts
     * @param damage Montant des dégâts
     */
    void takeDamage(Character character, int damage);

    /**
     * Soigne un personnage
     * 
     * @param character Le personnage à soigner
     * @param amount Montant de soin
     */
    void heal(Character character, int amount);

    /**
     * Restaure la mana d'un personnage
     * 
     * @param character Le personnage
     * @param amount Montant de mana à restaurer
     */
    void restoreMana(Character character, int amount);

    /**
     * Vérifie si un personnage est vivant
     * 
     * @param character Le personnage
     * @return true si le personnage a des HP > 0
     */
    boolean isAlive(Character character);
}
