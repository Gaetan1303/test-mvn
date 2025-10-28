package com.example.rpg.exception;

/**
 * Exception levée quand un utilisateur tente de créer un second personnage
 */
public class CharacterLimitException extends RuntimeException {
    public CharacterLimitException(String message) {
        super(message);
    }
}
