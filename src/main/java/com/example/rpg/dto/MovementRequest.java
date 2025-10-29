package com.example.rpg.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO pour les requêtes de mouvement du joueur
 * Envoyé via STOMP /app/game/move
 */
public class MovementRequest {
    
    @NotNull(message = "La nouvelle position X est obligatoire")
    private Double newX;
    
    @NotNull(message = "La nouvelle position Y est obligatoire")
    private Double newY;
    
    private String mapId = "world_1"; // Zone de jeu par défaut

    public MovementRequest() {
    }

    public MovementRequest(Double newX, Double newY, String mapId) {
        this.newX = newX;
        this.newY = newY;
        this.mapId = mapId;
    }

    public Double getNewX() {
        return newX;
    }

    public void setNewX(Double newX) {
        this.newX = newX;
    }

    public Double getNewY() {
        return newY;
    }

    public void setNewY(Double newY) {
        this.newY = newY;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
}
