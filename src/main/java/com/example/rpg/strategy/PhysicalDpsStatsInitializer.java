package com.example.rpg.strategy;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import org.springframework.stereotype.Component;

/**
 * Stratégie d'initialisation pour les classes DPS physiques (ARCHER, NINJA, SAMURAI, LANCER)
 * Privilégie PA et Speed
 */
@Component
public class PhysicalDpsStatsInitializer implements StatsInitializer {

        @Override
    public void initializeStats(Character character, CharacterClass characterClass) {
        // Initialisation des stats de base avec les valeurs de la classe
        character.setMaxHp(characterClass.getBaseHp());
        character.setCurrentHp(characterClass.getBaseHp());
        character.setMaxMp(characterClass.getBaseMp());
        character.setCurrentMp(characterClass.getBaseMp());
        character.setPa(characterClass.getBasePa());
        character.setMa(characterClass.getBaseMa());
        character.setSpeed(characterClass.getBaseSpeed());
        character.setMove(characterClass.getBaseMove());
        character.setPDef(characterClass.getBasePDef());
        character.setMDef(characterClass.getBaseMDef());
        character.setHit(characterClass.getBaseHit());
        character.setMagicHit(characterClass.getBaseMagicHit());
        character.setEvade(characterClass.getBaseEvade());
        character.setMagicEvade(characterClass.getBaseMagicEvade());
        character.setCritRate(characterClass.getBaseCritRate());
        character.setDestiny(characterClass.getBaseDestiny());
    }

    @Override
    public boolean supports(CharacterClass characterClass) {
        return characterClass == CharacterClass.DRAGOON || 
               characterClass == CharacterClass.NINJA ||
               characterClass == CharacterClass.SAMURAI ||
               characterClass == CharacterClass.THIEF;
    }
}
