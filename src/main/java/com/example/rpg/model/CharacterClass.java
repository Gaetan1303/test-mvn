package com.example.rpg.model;

/**
 * Classes de personnage disponibles dans le jeu
 * Système de stats inspiré de Final Fantasy Tactics Advance :
 * - HP (Hit Points) : Points de vie
 * - MP (Magic Points) : Points de magie
 * - PA (Physical Attack) : Attaque physique
 * - MA (Magic Attack) : Attaque magique
 * - Speed : Vitesse (détermine l'ordre des tours)
 * - Move : Mouvement sur la grille
 * - PDef : Défense physique
 * - MDef : Défense magique
 * - Hit : Précision physique
 * - MagicHit : Précision magique
 * - Evade : Esquive physique
 * - MagicEvade : Esquive magique
 * - CritRate : Taux de critique
 * - Destiny : Destin/Alignement (positif = bon, négatif = mauvais, 0 = neutre)
 */
public enum CharacterClass {
    // === Jobs de Combat et de Mêlée ===
    SQUIRE("Écolier", "DPS/Support de base", 100, 30, 50, 40, 100, 3, 20, 15, 85, 80, 10, 10, 5, 0),
    KNIGHT("Chevalier", "Tank/Contrôle Mêlée", 120, 20, 70, 40, 90, 3, 30, 20, 90, 75, 8, 12, 7, 10),
    MONK("Moine", "DPS Mêlée/Soin", 115, 25, 75, 45, 95, 3, 25, 18, 92, 82, 12, 15, 10, 5),
    THIEF("Voleur", "Vitesse/Utility", 90, 35, 55, 45, 120, 4, 18, 16, 95, 88, 18, 20, 12, -5),
    DRAGOON("Chevalier Dragon", "DPS Mêlée/Saut", 110, 25, 65, 40, 85, 3, 28, 17, 88, 78, 11, 13, 8, 0),
    SAMURAI("Samouraï", "DPS Mêlée Épique", 105, 30, 80, 50, 95, 3, 27, 19, 93, 85, 13, 14, 15, 8),
    NINJA("Ninja", "DPS/Speed Ultime", 95, 35, 70, 45, 130, 4, 22, 18, 97, 90, 20, 22, 18, -3),
    
    // === Jobs de Magie et de Support ===
    CHEMIST("Chimiste", "Soin/Support", 95, 40, 45, 50, 100, 3, 19, 20, 82, 88, 10, 15, 4, 3),
    WHITE_MAGE("Mage Blanc", "Soigneur/Buffer", 85, 50, 40, 70, 90, 3, 16, 25, 78, 95, 9, 18, 5, 15),
    BLACK_MAGE("Mage Noir", "DPS Magique", 80, 45, 35, 80, 85, 3, 14, 22, 75, 98, 8, 16, 6, -8),
    TIME_MAGE("Mage Temporel", "Contrôle du Temps", 85, 45, 40, 65, 95, 3, 17, 23, 80, 92, 11, 17, 7, 0),
    SUMMONER("Invocateur", "DPS Magique de Zone", 80, 60, 35, 75, 80, 3, 15, 24, 76, 96, 8, 19, 8, 5),
    MYSTIC("Mystique", "Débuff/Contrôle", 90, 50, 40, 60, 90, 3, 18, 22, 83, 90, 10, 18, 6, -5),
    GEOMANCER("Géomancien", "DPS Polyvalent", 100, 40, 55, 55, 100, 4, 21, 20, 87, 87, 14, 16, 9, 2),
    
    // === Jobs Spéciaux ===
    BARD("Barde", "Support (Buffs)", 85, 30, 45, 50, 75, 3, 16, 19, 80, 86, 9, 14, 5, 8),
    DANCER("Danseuse", "Support (Débuffs)", 85, 30, 50, 55, 75, 3, 17, 20, 82, 88, 10, 15, 6, 3),
    MIME("Mime", "Duplicateur", 100, 40, 60, 60, 110, 3, 23, 21, 90, 90, 15, 17, 10, 0),
    DARK_KNIGHT("Chevalier Noir", "DPS/Soin Sombre", 115, 30, 75, 50, 90, 3, 29, 18, 91, 83, 12, 13, 12, -15);

    private final String displayName;
    private final String role;
    private final int baseHp;
    private final int baseMp;
    private final int basePa;
    private final int baseMa;
    private final int baseSpeed;
    private final int baseMove;
    private final int basePDef;
    private final int baseMDef;
    private final int baseHit;
    private final int baseMagicHit;
    private final int baseEvade;
    private final int baseMagicEvade;
    private final int baseCritRate;
    private final int baseDestiny;  // Peut être négatif

    CharacterClass(String displayName, String role, int baseHp, int baseMp, int basePa, int baseMa, 
                   int baseSpeed, int baseMove, int basePDef, int baseMDef, 
                   int baseHit, int baseMagicHit, int baseEvade, int baseMagicEvade, int baseCritRate, int baseDestiny) {
        this.displayName = displayName;
        this.role = role;
        this.baseHp = baseHp;
        this.baseMp = baseMp;
        this.basePa = basePa;
        this.baseMa = baseMa;
        this.baseSpeed = baseSpeed;
        this.baseMove = baseMove;
        this.basePDef = basePDef;
        this.baseMDef = baseMDef;
        this.baseHit = baseHit;
        this.baseMagicHit = baseMagicHit;
        this.baseEvade = baseEvade;
        this.baseMagicEvade = baseMagicEvade;
        this.baseCritRate = baseCritRate;
        this.baseDestiny = baseDestiny;
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

    public int getBasePDef() {
        return basePDef;
    }

    public int getBaseMDef() {
        return baseMDef;
    }

    public int getBaseHit() {
        return baseHit;
    }

    public int getBaseMagicHit() {
        return baseMagicHit;
    }

    public int getBaseEvade() {
        return baseEvade;
    }

    public int getBaseMagicEvade() {
        return baseMagicEvade;
    }

    public int getBaseCritRate() {
        return baseCritRate;
    }

    public int getBaseDestiny() {
        return baseDestiny;
    }
}
