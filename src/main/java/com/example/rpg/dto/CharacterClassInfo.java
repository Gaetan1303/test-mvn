package com.example.rpg.dto;

/**
 * DTO pour exposer les informations d'une classe de personnage
 * Système de stats Final Fantasy Tactics :
 * - HP : Hit Points
 * - MP : Magic Points
 * - PA : Physical Attack
 * - MA : Magic Attack
 * - Speed : Vitesse
 * - Move : Mouvement
 */
public class CharacterClassInfo {
    private String name;              // SQUIRE, KNIGHT, etc.
    private String displayName;       // Écolier, Chevalier, etc.
    private String role;              // DPS/Support, Tank, etc.
    private int baseHp;
    private int baseMp;
    private int basePa;               // Physical Attack
    private int baseMa;               // Magic Attack
    private int baseSpeed;
    private int baseMove;
    private String emoji;             // Emoji pour l'affichage

    public CharacterClassInfo(String name, String displayName, String role,
                             int baseHp, int baseMp, int basePa, int baseMa, 
                             int baseSpeed, int baseMove, String emoji) {
        this.name = name;
        this.displayName = displayName;
        this.role = role;
        this.baseHp = baseHp;
        this.baseMp = baseMp;
        this.basePa = basePa;
        this.baseMa = baseMa;
        this.baseSpeed = baseSpeed;
        this.baseMove = baseMove;
        this.emoji = emoji;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getBaseMp() {
        return baseMp;
    }

    public void setBaseMp(int baseMp) {
        this.baseMp = baseMp;
    }

    public int getBasePa() {
        return basePa;
    }

    public void setBasePa(int basePa) {
        this.basePa = basePa;
    }

    public int getBaseMa() {
        return baseMa;
    }

    public void setBaseMa(int baseMa) {
        this.baseMa = baseMa;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public int getBaseMove() {
        return baseMove;
    }

    public void setBaseMove(int baseMove) {
        this.baseMove = baseMove;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
