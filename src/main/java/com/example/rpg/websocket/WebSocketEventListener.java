package com.example.rpg.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Gestionnaire d'événements WebSocket
 * Gère les connexions et déconnexions
 */
@Component
public class WebSocketEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    
    private final GameSessionManager sessionManager;
    private final SimpMessagingTemplate messagingTemplate;
    
    public WebSocketEventListener(GameSessionManager sessionManager,
                                 SimpMessagingTemplate messagingTemplate) {
        this.sessionManager = sessionManager;
        this.messagingTemplate = messagingTemplate;
    }
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        logger.info("Nouvelle connexion WebSocket - SessionId: {}", 
                   event.getMessage().getHeaders().get("simpSessionId"));
        sessionManager.logStats();
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        
        // Récupérer le characterId avant de supprimer la session
        Long characterId = sessionManager.getCharacterId(sessionId);
        
        if (characterId != null) {
            logger.info("Déconnexion WebSocket - SessionId: {}, CharacterId: {}", 
                       sessionId, characterId);
            
            // Notifier les autres joueurs de la déconnexion
            messagingTemplate.convertAndSend("/topic/game/disconnect", characterId);
        }
        
        // Supprimer la session
        sessionManager.removeSession(sessionId);
        sessionManager.logStats();
    }
}
