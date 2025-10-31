package com.example.rpg.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un monstre/NPC qui peut se déplacer dans le jeu
 */
@Entity
@Table(name = "monsters")
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MonsterType monsterType;

    @Column(nullable = false)
    private Integer level = 1;

    // === Stats simplifiées pour les monstres ===
    
    @Column(nullable = false)
    private Integer currentHp;

    @Column(nullable = false)
    private Integer maxHp;

    @Column(nullable = false)
    private Integer attackPower;

    @Column(nullable = false)
    private Integer defense;

    @Column(nullable = false)
    private Integer speed;

    @Column(nullable = false)
    private Integer move; // Distance de déplacement par tour

    // === Position dans le monde ===
    @Column(nullable = false)
    private Double positionX = 0.0;

    @Column(nullable = false)
    private Double positionY = 0.0;

    // === État du monstre ===
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonsterState state = MonsterState.IDLE;

    // === AI Pattern ===
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AIPattern aiPattern = AIPattern.RANDOM_WALK;

    // === Métadonnées ===
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // === Constructeurs ===
    public Monster() {}

    public Monster(String name, MonsterType monsterType) {
        this.name = name;
        this.monsterType = monsterType;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // === Callbacks JPA ===
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // === Getters et Setters ===
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public MonsterType getMonsterType() { return monsterType; }
    public void setMonsterType(MonsterType monsterType) { this.monsterType = monsterType; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getCurrentHp() { return currentHp; }
    public void setCurrentHp(Integer currentHp) { this.currentHp = currentHp; }

    public Integer getMaxHp() { return maxHp; }
    public void setMaxHp(Integer maxHp) { this.maxHp = maxHp; }

    public Integer getAttackPower() { return attackPower; }
    public void setAttackPower(Integer attackPower) { this.attackPower = attackPower; }

    public Integer getDefense() { return defense; }
    public void setDefense(Integer defense) { this.defense = defense; }

    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }

    public Integer getMove() { return move; }
    public void setMove(Integer move) { this.move = move; }

    public Double getPositionX() { return positionX; }
    public void setPositionX(Double positionX) { this.positionX = positionX; }

    public Double getPositionY() { return positionY; }
    public void setPositionY(Double positionY) { this.positionY = positionY; }

    public MonsterState getState() { return state; }
    public void setState(MonsterState state) { this.state = state; }

    public AIPattern getAiPattern() { return aiPattern; }
    public void setAiPattern(AIPattern aiPattern) { this.aiPattern = aiPattern; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", monsterType=" + monsterType +
                ", level=" + level +
                ", position=(" + positionX + ", " + positionY + ")" +
                ", state=" + state +
                '}';
    }
}