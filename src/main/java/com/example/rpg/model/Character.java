package com.example.rpg.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un personnage de jeu
 * Relation OneToOne avec Utilisateur
 */
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CharacterClass characterClass;

    @Column(nullable = false)
    private Integer level = 1;

    // === Stats Final Fantasy Tactics Advance complètes ===
    
    // Points de vie
    @Column(nullable = false)
    private Integer currentHp;

    @Column(nullable = false)
    private Integer maxHp;

    // Points de magie
    @Column(nullable = false)
    private Integer currentMp;

    @Column(nullable = false)
    private Integer maxMp;

    // Attaque
    @Column(nullable = false, name = "physical_attack")
    private Integer pa;  // Physical Attack (Attack Power)

    @Column(nullable = false, name = "magic_attack")
    private Integer ma;  // Magic Attack (Magic Power)

    // Mouvement et mobilité
    @Column(nullable = false)
    private Integer speed;  // Vitesse - Détermine l'ordre d'action

    @Column(nullable = false)
    private Integer move;  // Mouvement - Nombre de cases déplaçables

    // Défense
    @Column(nullable = false, name = "physical_defense")
    private Integer pDef;  // Défense physique

    @Column(nullable = false, name = "magic_defense")
    private Integer mDef;  // Défense magique

    // Précision (Hit/Accuracy)
    @Column(nullable = false)
    private Integer hit;  // Précision physique (pour attaques physiques)

    @Column(nullable = false, name = "magic_hit")
    private Integer magicHit;  // Précision magique (pour sorts)

    // Esquive (Evade)
    @Column(nullable = false)
    private Integer evade;  // Esquive physique

    @Column(nullable = false, name = "magic_evade")
    private Integer magicEvade;  // Esquive magique

    // Critique
    @Column(nullable = false, name = "crit_rate")
    private Integer critRate;  // Taux de critique (en %)

    // Destin - Détermine l'alignement du personnage (peut être négatif)
    @Column(nullable = false)
    private Integer destiny;  // Destin (positif = bon, négatif = mauvais, 0 = neutre)

    @Column(nullable = false)
    private Integer experience = 0;

    // Position dans le monde (pour Phase 1 - WebSocket)
    @Column(nullable = false)
    private Double positionX = 0.0;

    @Column(nullable = false)
    private Double positionY = 0.0;

    // État du personnage (pour machine à états)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlayerState state = PlayerState.HUB;

    // Relation avec l'utilisateur (ManyToOne : plusieurs personnages par utilisateur)
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructeurs
    public Character() {
    }

    /**
     * Constructeur avec initialisation des stats
     * @deprecated Utiliser CharacterFactory.createCharacter() pour respecter les patterns Factory et Strategy
     */
    @Deprecated
    public Character(String name, CharacterClass characterClass, Utilisateur utilisateur) {
        this.name = name;
        this.characterClass = characterClass;
        this.utilisateur = utilisateur;
        initializeStats();
    }

    /**
     * Initialise les statistiques du personnage selon sa classe (système FFT)
     * @deprecated Utiliser StatsInitializer (Pattern Strategy) via CharacterFactory
     */
    @Deprecated
    private void initializeStats() {
        // Stats FFT
        this.maxHp = characterClass.getBaseHp();
        this.currentHp = maxHp;
        this.maxMp = characterClass.getBaseMp();
        this.currentMp = maxMp;
        this.pa = characterClass.getBasePa();
        this.ma = characterClass.getBaseMa();
        this.speed = characterClass.getBaseSpeed();
        this.move = characterClass.getBaseMove();
    }

    /**
     * Vérifie si le personnage est vivant
     * @deprecated Utiliser CombatService.isAlive() pour respecter le principe S (Single Responsibility)
     */
    @Deprecated
    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * Applique des dégâts au personnage
     * @deprecated Utiliser CombatService.takeDamage() pour respecter le principe S (Single Responsibility)
     */
    @Deprecated
    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    /**
     * Soigne le personnage
     * @deprecated Utiliser CombatService.heal() pour respecter le principe S (Single Responsibility)
     */
    @Deprecated
    public void heal(int amount) {
        this.currentHp = Math.min(maxHp, this.currentHp + amount);
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

    // === Nouvelles stats FFT ===
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

    public Integer getPDef() {
        return pDef;
    }

    public void setPDef(Integer pDef) {
        this.pDef = pDef;
    }

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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
