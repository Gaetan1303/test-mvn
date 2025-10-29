package com.example.rpg.strategy;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import org.springframework.stereotype.Component;

/**
 * Stratégie d'initialisation pour les classes Support (CHEMIST, BARD, DANCER, MYSTIC, GEOMANCER)
 * Équilibre entre stats pour polyvalence
 */
@Component
public class SupportStatsInitializer implements StatsInitializer {

        @Override
    public void initializeStats(Character character, CharacterClass characterClass) {
        // Stats FFT Advance complètes
        character.setMaxHp(characterClass.getBaseHp());
        character.setCurrentHp(characterClass.getBaseHp());
        character.setMaxMp(characterClass.getBaseMp());
        character.setCurrentMp(characterClass.getBaseMp());
        
        // Attaque physique et magique
        character.setPa(characterClass.getBasePa());
        character.setMa(characterClass.getBaseMa());
        
        // Vitesse et mouvement
        character.setSpeed(characterClass.getBaseSpeed());
        character.setMove(characterClass.getBaseMove());
        
        // Défense
        character.setPDef(characterClass.getBasePDef());
        character.setMDef(characterClass.getBaseMDef());
        
        // Précision
        character.setHit(characterClass.getBaseHit());
        character.setMagicHit(characterClass.getBaseMagicHit());
        
        // Esquive
        character.setEvade(characterClass.getBaseEvade());
        character.setMagicEvade(characterClass.getBaseMagicEvade());
        
        // Critique
        character.setCritRate(characterClass.getBaseCritRate());
        
        // Destin/Alignement
        character.setDestiny(characterClass.getBaseDestiny());
    }

    @Override
    public boolean supports(CharacterClass characterClass) {
        return characterClass == CharacterClass.CHEMIST || 
               characterClass == CharacterClass.BARD ||
               characterClass == CharacterClass.DANCER ||
               characterClass == CharacterClass.MYSTIC ||
               characterClass == CharacterClass.GEOMANCER;
    }
}
