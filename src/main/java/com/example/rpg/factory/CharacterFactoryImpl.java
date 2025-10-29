package com.example.rpg.factory;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.strategy.StatsInitializer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implémentation de la factory de personnages utilisant le Pattern Strategy
 * Respecte le principe O (Open/Closed) : extensible en ajoutant de nouvelles stratégies
 * Respecte le principe D (Dependency Inversion) : dépend de l'abstraction StatsInitializer
 */
@Component
public class CharacterFactoryImpl implements CharacterFactory {

    private final List<StatsInitializer> statsInitializers;

    public CharacterFactoryImpl(List<StatsInitializer> statsInitializers) {
        this.statsInitializers = statsInitializers;
    }

    @Override
    public Character createCharacter(String name, CharacterClass characterClass, Utilisateur utilisateur) {
        // Créer le personnage avec données de base
        Character character = new Character();
        character.setName(name);
        character.setCharacterClass(characterClass);
        character.setUtilisateur(utilisateur);
        character.setLevel(1);
        character.setExperience(0);
        character.setPositionX(0.0);
        character.setPositionY(0.0);

        // Trouver la stratégie appropriée et initialiser les stats
        StatsInitializer initializer = findInitializer(characterClass);
        initializer.initializeStats(character, characterClass);

        return character;
    }

    /**
     * Trouve la stratégie d'initialisation appropriée pour une classe
     * Utilise la première stratégie qui supporte la classe
     * 
     * @param characterClass La classe du personnage
     * @return La stratégie d'initialisation
     */
    private StatsInitializer findInitializer(CharacterClass characterClass) {
        return statsInitializers.stream()
                .filter(initializer -> initializer.supports(characterClass))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                    "Aucune stratégie d'initialisation trouvée pour la classe : " + characterClass
                ));
    }
}
