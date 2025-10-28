package com.example.rpg.dto;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.PlayerState;

/**
 * DTO de réponse contenant toutes les informations du personnage
 */
public class CharacterResponse {

    private Long id;
    private String name;
    private CharacterClass characterClass;
    private String classDisplayName;
    private Integer level;
    private Integer currentHp;
    private Integer maxHp;
    private Integer strength;
    private Integer agility;
    private Integer intelligence;
    private Integer experience;
    private Double positionX;
    private Double positionY;
    private PlayerState state;
    private String message;

    public CharacterResponse() {
    }

    /**
     * Constructeur à partir d'une entité Character
     */
    public CharacterResponse(Character character, String message) {
        this.id = character.getId();
        this.name = character.getName();
        this.characterClass = character.getCharacterClass();
        this.classDisplayName = character.getCharacterClass().getDisplayName();
        this.level = character.getLevel();
        this.currentHp = character.getCurrentHp();
        this.maxHp = character.getMaxHp();
        this.strength = character.getStrength();
        this.agility = character.getAgility();
        this.intelligence = character.getIntelligence();
        this.experience = character.getExperience();
        this.positionX = character.getPositionX();
        this.positionY = character.getPositionY();
        this.state = character.getState();
        this.message = message;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getClassDisplayName() {
        return classDisplayName;
    }

    public void setClassDisplayName(String classDisplayName) {
        this.classDisplayName = classDisplayName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(Integer currentHp) {
        this.currentHp = currentHp;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
