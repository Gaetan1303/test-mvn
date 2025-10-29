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

    // === Stats Final Fantasy Tactics ===
    @Column(nullable = false)
    private Integer currentHp;

    @Column(nullable = false)
    private Integer maxHp;

    @Column(nullable = false)
    private Integer currentMp;

    @Column(nullable = false)
    private Integer maxMp;

    @Column(nullable = false, name = "physical_attack")
    private Integer pa;  // Physical Attack

    @Column(nullable = false, name = "magic_attack")
    private Integer ma;  // Magic Attack

    @Column(nullable = false)
    private Integer speed;

    @Column(nullable = false)
    private Integer move;

    // === Anciennes stats (compatibilité) - À RETIRER ===
    @Deprecated
    @Column(nullable = true)
    private Integer strength;

    @Deprecated
    @Column(nullable = true)
    private Integer agility;

    @Deprecated
    @Column(nullable = true)
    private Integer intelligence;

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

    public Character(String name, CharacterClass characterClass, Utilisateur utilisateur) {
        this.name = name;
        this.characterClass = characterClass;
        this.utilisateur = utilisateur;
        initializeStats();
    }

    /**
     * Initialise les statistiques du personnage selon sa classe (système FFT)
     */
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
        
        // Compatibilité anciennes stats (deprecated)
        this.strength = characterClass.getBasePa();
        this.agility = characterClass.getBaseSpeed();
        this.intelligence = characterClass.getBaseMa();
    }

    /**
     * Vérifie si le personnage est vivant
     */
    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * Applique des dégâts au personnage
     */
    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    /**
     * Soigne le personnage
     */
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

    // === Anciennes stats (deprecated) ===
    
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
