package com.example.rpg.service;

import com.example.rpg.exception.CharacterAlreadyExistsException;
import com.example.rpg.exception.CharacterLimitException;
import com.example.rpg.exception.CharacterNotFoundException;
import com.example.rpg.factory.CharacterFactory;
import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterClass;
import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des personnages de jeu
 * Support de plusieurs personnages par utilisateur (max 3)
 * Respecte le principe I (Interface Segregation) : implémente l'interface ICharacterService
 * Utilise le Pattern Factory pour créer les personnages
 */
@Service
public class CharacterService implements ICharacterService {

    private static final int MAX_CHARACTERS_PER_USER = 3;

    private final CharacterRepository characterRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CharacterFactory characterFactory;

    public CharacterService(CharacterRepository characterRepository, 
                           UtilisateurRepository utilisateurRepository,
                           CharacterFactory characterFactory) {
        this.characterRepository = characterRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.characterFactory = characterFactory;
    }

    /**
     * Crée un nouveau personnage pour un utilisateur
     * 
     * @param username Nom d'utilisateur (depuis le JWT)
     * @param characterName Nom du personnage
     * @param characterClass Classe du personnage
     * @return Le personnage créé
     * @throws UsernameNotFoundException Si l'utilisateur n'existe pas
     * @throws CharacterLimitException Si l'utilisateur a déjà 3 personnages
     * @throws CharacterAlreadyExistsException Si le nom de personnage existe déjà
     */
    @Override
    @Transactional
    public Character createCharacter(String username, String characterName, CharacterClass characterClass) {
        // Vérifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Vérifier le nombre de personnages (max 3)
        long characterCount = characterRepository.countByUtilisateurId(utilisateur.getId());
        if (characterCount >= MAX_CHARACTERS_PER_USER) {
            throw new CharacterLimitException("Vous avez atteint le maximum de " + MAX_CHARACTERS_PER_USER + " personnages");
        }

        // Vérifier que le nom de personnage n'est pas déjà pris
        if (characterRepository.existsByName(characterName)) {
            throw new CharacterAlreadyExistsException("Ce nom de personnage est déjà utilisé");
        }

        // Créer le personnage via la Factory (Pattern Factory + Strategy)
        Character character = characterFactory.createCharacter(characterName, characterClass, utilisateur);
        return characterRepository.save(character);
    }

    /**
     * Récupère le personnage d'un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Le personnage
     * @throws CharacterNotFoundException Si aucun personnage n'existe
     * @deprecated Utiliser getAllCharactersByUsername() ou getCharacterById() pour multi-personnages
     */
    @Deprecated
    @Transactional(readOnly = true)
    public Character getCharacterByUsername(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return characterRepository.findByUtilisateurId(utilisateur.getId())
            .orElseThrow(() -> new CharacterNotFoundException("Aucun personnage trouvé. Veuillez en créer un."));
    }

    /**
     * Récupère tous les personnages d'un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Liste des personnages (peut être vide)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Character> getAllCharactersByUsername(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return characterRepository.findAllByUtilisateurId(utilisateur.getId());
    }

    /**
     * Récupère un personnage par son ID (vérifie qu'il appartient bien à l'utilisateur)
     * 
     * @param username Nom d'utilisateur
     * @param characterId ID du personnage
     * @return Le personnage
     * @throws CharacterNotFoundException Si le personnage n'existe pas ou n'appartient pas à l'utilisateur
     */
    @Override
    @Transactional(readOnly = true)
    public Character getCharacterById(String username, Long characterId) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new CharacterNotFoundException("Personnage non trouvé"));

        // Vérifier que le personnage appartient bien à cet utilisateur
        if (!character.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new CharacterNotFoundException("Ce personnage ne vous appartient pas");
        }

        return character;
    }

    /**
     * Vérifie si un utilisateur a déjà un personnage
     */
    @Override
    @Transactional(readOnly = true)
    public boolean hasCharacter(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        
        return characterRepository.existsByUtilisateurId(utilisateur.getId());
    }

    /**
     * Met à jour la position du personnage
     */
    @Override
    @Transactional
    public Character updatePosition(String username, Double x, Double y) {
        Character character = getCharacterByUsername(username);
        character.setPositionX(x);
        character.setPositionY(y);
        return characterRepository.save(character);
    }

    /**
     * Supprime le personnage d'un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @throws CharacterNotFoundException Si aucun personnage n'existe
     * @deprecated Utiliser deleteCharacterById() pour multi-personnages
     */
    @Deprecated
    @Transactional
    public void deleteCharacter(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Character character = characterRepository.findByUtilisateurId(utilisateur.getId())
            .orElseThrow(() -> new CharacterNotFoundException("Aucun personnage à supprimer"));

        characterRepository.delete(character);
    }

    /**
     * Supprime un personnage par son ID (vérifie qu'il appartient bien à l'utilisateur)
     * 
     * @param username Nom d'utilisateur
     * @param characterId ID du personnage à supprimer
     * @throws CharacterNotFoundException Si le personnage n'existe pas ou n'appartient pas à l'utilisateur
     */
    @Override
    @Transactional
    public void deleteCharacterById(String username, Long characterId) {
        Character character = getCharacterById(username, characterId);
        characterRepository.delete(character);
    }
}
