package com.example.rpg.dto;

/**
 * DTO pour la synchronisation de position entre joueurs
 * Diffusé via STOMP /topic/game/position
 */
public class PositionUpdate {
    
    private Long characterId;
    private String characterName;
    private Double x;
    private Double y;
    private String mapId;
    private Long timestamp;

    public PositionUpdate() {
        this.timestamp = System.currentTimeMillis();
    }

    public PositionUpdate(Long characterId, String characterName, Double x, Double y, String mapId) {
        this.characterId = characterId;
        this.characterName = characterName;
        this.x = x;
        this.y = y;
        this.mapId = mapId;
        this.timestamp = System.currentTimeMillis();
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
