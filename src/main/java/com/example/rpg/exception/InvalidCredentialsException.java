package com.example.rpg.exception;

/**
 * Exception levée lors d'une tentative de connexion avec des credentials invalides
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
