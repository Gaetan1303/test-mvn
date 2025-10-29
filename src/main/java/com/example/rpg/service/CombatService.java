package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion de la logique de combat
 * Respecte le principe S (Single Responsibility) : responsable uniquement de la logique de combat
 * Séparé de CharacterService pour éviter de mélanger les responsabilités
 */
@Service
public class CombatService implements ICombatService {

    private final CharacterRepository characterRepository;

    public CombatService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    /**
     * Applique des dégâts à un personnage
     * 
     * @param character Le personnage qui subit les dégâts
     * @param damage Montant des dégâts
     */
    @Override
    @Transactional
    public void takeDamage(Character character, int damage) {
        int newHp = Math.max(0, character.getCurrentHp() - damage);
        character.setCurrentHp(newHp);
        characterRepository.save(character);
    }

    /**
     * Soigne un personnage
     * 
     * @param character Le personnage à soigner
     * @param amount Montant de soin
     */
    @Override
    @Transactional
    public void heal(Character character, int amount) {
        int newHp = Math.min(character.getMaxHp(), character.getCurrentHp() + amount);
        character.setCurrentHp(newHp);
        characterRepository.save(character);
    }

    /**
     * Restaure la mana d'un personnage
     * 
     * @param character Le personnage
     * @param amount Montant de mana à restaurer
     */
    @Override
    @Transactional
    public void restoreMana(Character character, int amount) {
        int newMp = Math.min(character.getMaxMp(), character.getCurrentMp() + amount);
        character.setCurrentMp(newMp);
        characterRepository.save(character);
    }

    /**
     * Vérifie si un personnage est vivant
     * 
     * @param character Le personnage
     * @return true si le personnage a des HP > 0
     */
    @Override
    public boolean isAlive(Character character) {
        return character.getCurrentHp() > 0;
    }
}
