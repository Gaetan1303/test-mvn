package com.example.rpg.dto;

/**
 * DTO pour les messages de chat via Mercure
 */
public class ChatMessage {
    
    private String username;
    private String message;
    private String channel; // Ex: "global", "zone_1", "party"
    private Long timestamp;
    
    public ChatMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ChatMessage(String username, String message, String channel) {
        this.username = username;
        this.message = message;
        this.channel = channel;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
