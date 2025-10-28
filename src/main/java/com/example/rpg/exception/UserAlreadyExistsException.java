package com.example.rpg.exception;

/**
 * Exception levée lorsqu'un utilisateur tente de s'inscrire avec un username ou email déjà utilisé
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
