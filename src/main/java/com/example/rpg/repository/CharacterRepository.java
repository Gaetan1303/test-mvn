package com.example.rpg.repository;

import com.example.rpg.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    
    /**
     * Trouve un personnage par son nom
     */
    Optional<Character> findByName(String name);
    
    /**
     * Trouve un personnage par l'ID de son utilisateur
     * @deprecated Utiliser findAllByUtilisateurId() pour supporter plusieurs personnages
     */
    @Deprecated
    Optional<Character> findByUtilisateurId(Long utilisateurId);
    
    /**
     * Trouve tous les personnages d'un utilisateur
     */
    List<Character> findAllByUtilisateurId(Long utilisateurId);
    
    /**
     * Compte le nombre de personnages d'un utilisateur
     */
    long countByUtilisateurId(Long utilisateurId);
    
    /**
     * Vérifie si un nom de personnage existe déjà
     */
    boolean existsByName(String name);
    
    /**
     * Vérifie si un utilisateur a déjà un personnage
     */
    boolean existsByUtilisateurId(Long utilisateurId);
    
    /**
     * Trouve un personnage à une position donnée (pour détection de collisions)
     * Exclut le personnage avec l'ID donné pour permettre au joueur de rester à sa position actuelle
     */
    Optional<Character> findByPositionXAndPositionYAndIdNot(Double positionX, Double positionY, Long excludeCharacterId);
    
    /**
     * Vérifie si une position est occupée par un autre personnage
     */
    boolean existsByPositionXAndPositionYAndIdNot(Double positionX, Double positionY, Long excludeCharacterId);
    
    /**
     * Trouve l'ID d'un personnage à une position donnée (pour collision avec monstres)
     */
    @Query("SELECT c.id FROM Character c WHERE c.positionX = :x AND c.positionY = :y")
    List<Long> findCharacterIdsAtPosition(@Param("x") Double x, @Param("y") Double y);
}
