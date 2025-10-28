package com.example.rpg.model;

/**
 * Classes de personnage disponibles dans le jeu
 * Système de stats inspiré de Final Fantasy Tactics :
 * - HP (Hit Points) : Points de vie
 * - MP (Magic Points) : Points de magie
 * - PA (Physical Attack) : Attaque physique
 * - MA (Magic Attack) : Attaque magique
 * - Speed : Vitesse (détermine l'ordre des tours)
 * - Move : Mouvement sur la grille
 */
public enum CharacterClass {
    // === Jobs de Combat et de Mêlée ===
    SQUIRE("Écolier", "DPS/Support de base", 100, 30, 50, 40, 100, 3),
    KNIGHT("Chevalier", "Tank/Contrôle Mêlée", 120, 20, 70, 40, 90, 3),
    MONK("Moine", "DPS Mêlée/Soin", 115, 25, 75, 45, 95, 3),
    THIEF("Voleur", "Vitesse/Utility", 90, 35, 55, 45, 120, 4),
    DRAGOON("Chevalier Dragon", "DPS Mêlée/Saut", 110, 25, 65, 40, 85, 3),
    SAMURAI("Samouraï", "DPS Mêlée Épique", 105, 30, 80, 50, 95, 3),
    NINJA("Ninja", "DPS/Speed Ultime", 95, 35, 70, 45, 130, 4),
    
    // === Jobs de Magie et de Support ===
    CHEMIST("Chimiste", "Soin/Support", 95, 40, 45, 50, 100, 3),
    WHITE_MAGE("Mage Blanc", "Soigneur/Buffer", 85, 50, 40, 70, 90, 3),
    BLACK_MAGE("Mage Noir", "DPS Magique", 80, 45, 35, 80, 85, 3),
    TIME_MAGE("Mage Temporel", "Contrôle du Temps", 85, 45, 40, 65, 95, 3),
    SUMMONER("Invocateur", "DPS Magique de Zone", 80, 60, 35, 75, 80, 3),
    MYSTIC("Mystique", "Débuff/Contrôle", 90, 50, 40, 60, 90, 3),
    GEOMANCER("Géomancien", "DPS Polyvalent", 100, 40, 55, 55, 100, 4),
    
    // === Jobs Spéciaux ===
    BARD("Barde", "Support (Buffs)", 85, 30, 45, 50, 75, 3),
    DANCER("Danseuse", "Support (Débuffs)", 85, 30, 50, 55, 75, 3),
    MIME("Mime", "Duplicateur", 100, 40, 60, 60, 110, 3),
    DARK_KNIGHT("Chevalier Noir", "DPS/Soin Sombre", 115, 30, 75, 50, 90, 3);

    private final String displayName;
    private final String role;
    private final int baseHp;    // Hit Points
    private final int baseMp;    // Magic Points
    private final int basePa;    // Physical Attack
    private final int baseMa;    // Magic Attack
    private final int baseSpeed; // Vitesse
    private final int baseMove;  // Mouvement

    CharacterClass(String displayName, String role, int baseHp, int baseMp, int basePa, int baseMa, int baseSpeed, int baseMove) {
        this.displayName = displayName;
        this.role = role;
        this.baseHp = baseHp;
        this.baseMp = baseMp;
        this.basePa = basePa;
        this.baseMa = baseMa;
        this.baseSpeed = baseSpeed;
        this.baseMove = baseMove;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getBaseMp() {
        return baseMp;
    }

    public int getBasePa() {
        return basePa;
    }

    public int getBaseMa() {
        return baseMa;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseMove() {
        return baseMove;
    }
    
    // Méthodes de compatibilité avec l'ancien système (à retirer progressivement)
    @Deprecated
    public int getBaseStrength() {
        return basePa;
    }
    
    @Deprecated
    public int getBaseAgility() {
        return baseSpeed;
    }
    
    @Deprecated
    public int getBaseIntelligence() {
        return baseMa;
    }
}
