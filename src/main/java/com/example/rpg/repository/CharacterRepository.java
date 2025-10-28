package com.example.rpg.repository;

import com.example.rpg.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
