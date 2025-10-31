package com.example.rpg.repository;

import com.example.rpg.model.Monster;
import com.example.rpg.model.MonsterState;
import com.example.rpg.model.MonsterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, Long> {

    /**
     * Trouve tous les monstres dans une zone donnée
     */
    @Query("SELECT m FROM Monster m WHERE m.positionX BETWEEN :minX AND :maxX AND m.positionY BETWEEN :minY AND :maxY")
    List<Monster> findMonstersInArea(@Param("minX") Double minX, @Param("maxX") Double maxX, 
                                   @Param("minY") Double minY, @Param("maxY") Double maxY);

    /**
     * Trouve tous les monstres par état
     */
    List<Monster> findByState(MonsterState state);

    /**
     * Trouve tous les monstres par type
     */
    List<Monster> findByMonsterType(MonsterType monsterType);

    /**
     * Vérifie s'il y a un monstre à une position donnée (pour éviter les collisions)
     */
    @Query("SELECT m.id FROM Monster m WHERE m.positionX = :x AND m.positionY = :y AND m.id <> :excludeId")
    List<Long> findMonsterAtPosition(@Param("x") Double x, @Param("y") Double y, @Param("excludeId") Long excludeId);

    /**
     * Trouve tous les monstres vivants (non morts)
     */
    @Query("SELECT m FROM Monster m WHERE m.state <> 'DEAD'")
    List<Monster> findAllAlive();

    /**
     * Compte les monstres dans une zone
     */
    @Query("SELECT COUNT(m) FROM Monster m WHERE m.positionX BETWEEN :minX AND :maxX AND m.positionY BETWEEN :minY AND :maxY")
    Long countMonstersInArea(@Param("minX") Double minX, @Param("maxX") Double maxX, 
                           @Param("minY") Double minY, @Param("maxY") Double maxY);
}