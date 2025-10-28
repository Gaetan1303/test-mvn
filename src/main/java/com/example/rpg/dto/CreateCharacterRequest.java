package com.example.rpg.dto;

import com.example.rpg.model.CharacterClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'un personnage
 */
public class CreateCharacterRequest {

    @NotBlank(message = "Le nom du personnage est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    @NotNull(message = "La classe du personnage est obligatoire")
    private CharacterClass characterClass;

    public CreateCharacterRequest() {
    }

    public CreateCharacterRequest(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }
}
