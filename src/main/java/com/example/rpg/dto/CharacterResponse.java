package com.example.rpg.dto;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.PlayerState;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de réponse contenant toutes les informations du personnage
 * Stats Final Fantasy Tactics Advance complètes (14 stats + données de jeu)
 */
public class CharacterResponse {

    private Long id;
    private String name;
    private CharacterClass characterClass;
    private String classDisplayName;
    private Integer level;
    
    // Stats FFT Advance complètes
    private Integer currentHp;
    private Integer maxHp;
    private Integer currentMp;
    private Integer maxMp;
    private Integer pa;           // Physical Attack
    private Integer ma;           // Magic Attack
    private Integer speed;
    private Integer move;
    private Integer pDef;         // Physical Defense
    private Integer mDef;         // Magic Defense
    private Integer hit;          // Précision physique
    private Integer magicHit;     // Précision magique
    private Integer evade;        // Esquive physique
    private Integer magicEvade;   // Esquive magique
    private Integer critRate;     // Taux de critique
    private Integer destiny;      // Destin/Alignement
    
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
        
        // Stats FFT Advance complètes
        this.currentHp = character.getCurrentHp();
        this.maxHp = character.getMaxHp();
        this.currentMp = character.getCurrentMp();
        this.maxMp = character.getMaxMp();
        this.pa = character.getPa();
        this.ma = character.getMa();
        this.speed = character.getSpeed();
        this.move = character.getMove();
        this.pDef = character.getPDef();
        this.mDef = character.getMDef();
        this.hit = character.getHit();
        this.magicHit = character.getMagicHit();
        this.evade = character.getEvade();
        this.magicEvade = character.getMagicEvade();
        this.critRate = character.getCritRate();
        this.destiny = character.getDestiny();
        
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

    public Integer getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(Integer currentMp) {
        this.currentMp = currentMp;
    }

    public Integer getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(Integer maxMp) {
        this.maxMp = maxMp;
    }

    public Integer getPa() {
        return pa;
    }

    public void setPa(Integer pa) {
        this.pa = pa;
    }

    public Integer getMa() {
        return ma;
    }

    public void setMa(Integer ma) {
        this.ma = ma;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getMove() {
        return move;
    }

    public void setMove(Integer move) {
        this.move = move;
    }

    @JsonProperty("pDef")
    public Integer getPDef() {
        return pDef;
    }

    public void setPDef(Integer pDef) {
        this.pDef = pDef;
    }

    @JsonProperty("mDef")
    public Integer getMDef() {
        return mDef;
    }

    public void setMDef(Integer mDef) {
        this.mDef = mDef;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Integer getMagicHit() {
        return magicHit;
    }

    public void setMagicHit(Integer magicHit) {
        this.magicHit = magicHit;
    }

    public Integer getEvade() {
        return evade;
    }

    public void setEvade(Integer evade) {
        this.evade = evade;
    }

    public Integer getMagicEvade() {
        return magicEvade;
    }

    public void setMagicEvade(Integer magicEvade) {
        this.magicEvade = magicEvade;
    }

    public Integer getCritRate() {
        return critRate;
    }

    public void setCritRate(Integer critRate) {
        this.critRate = critRate;
    }

    public Integer getDestiny() {
        return destiny;
    }

    public void setDestiny(Integer destiny) {
        this.destiny = destiny;
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
