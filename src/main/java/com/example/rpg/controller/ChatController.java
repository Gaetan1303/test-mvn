package com.example.rpg.controller;

import com.example.rpg.dto.ChatMessage;
import com.example.rpg.service.MercureService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Contrôleur pour le chat via Mercure
 * Messages non-critiques diffusés via Server-Sent Events (SSE)
 */
@Controller
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    private final MercureService mercureService;
    
    public ChatController(MercureService mercureService) {
        this.mercureService = mercureService;
    }
    
    /**
     * Envoi d'un message de chat
     * Endpoint: /app/chat/send
     */
    @MessageMapping("/chat/send")
    public void handleChatMessage(@Valid @Payload ChatMessage message, Principal principal) {
        // Définir l'username depuis l'authentification
        message.setUsername(principal.getName());
        
        // Publier via Mercure
        mercureService.publishChatMessage(message);
        
        logger.info("Message chat envoyé - User: {}, Channel: {}, Message: {}", 
                   principal.getName(), message.getChannel(), message.getMessage());
    }
}
