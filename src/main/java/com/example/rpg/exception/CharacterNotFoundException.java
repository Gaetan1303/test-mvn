package com.example.rpg.exception;

/**
 * Exception levée quand un personnage n'est pas trouvé
 */
public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(String message) {
        super(message);
    }
}
