package com.example.rpg.exception;

/**
 * Exception levée quand un personnage avec ce nom existe déjà
 */
public class CharacterAlreadyExistsException extends RuntimeException {
    public CharacterAlreadyExistsException(String message) {
        super(message);
    }
}
