package com.example.rpg.websocket;

import com.example.rpg.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration WebSocket avec STOMP
 * Authentification JWT pour les connexions WebSocket
 */
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public WebSocketConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        // Active un broker simple en mémoire
        // Destinations commençant par /topic ou /queue
        config.enableSimpleBroker("/topic", "/queue");
        
        // Préfixe pour les messages envoyés depuis le client vers le serveur
        config.setApplicationDestinationPrefixes("/app");
        
        // Préfixe pour les messages utilisateur (privés)
        config.setUserDestinationPrefix("/user");
        
        logger.info("Message broker configuré - /topic, /queue, /app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // Endpoint WebSocket natif (pour clients WebSocket standards)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // À restreindre en production
        
        // Endpoint WebSocket avec SockJS (pour navigateurs sans support WebSocket)
        registry.addEndpoint("/ws-sockjs")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        
        logger.info("Endpoints STOMP enregistrés - /ws (natif) et /ws-sockjs (avec SockJS)");
    }

    @Override
    public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Récupérer le token JWT depuis les headers STOMP
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        
                        try {
                            // Extraire le username depuis le JWT
                            String username = jwtUtil.extractUsername(token);
                            
                            // Charger les détails de l'utilisateur
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            
                            // Valider le token
                            if (jwtUtil.validateToken(token, userDetails)) {
                                // Créer l'authentification
                                UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(
                                        userDetails, 
                                        null, 
                                        userDetails.getAuthorities()
                                    );
                                
                                // Stocker l'authentification dans le contexte WebSocket
                                accessor.setUser(authentication);
                                
                                logger.info("WebSocket authentifié - User: {}, SessionId: {}", 
                                           username, accessor.getSessionId());
                            } else {
                                logger.warn("Token JWT invalide pour WebSocket");
                                throw new IllegalArgumentException("Token JWT invalide");
                            }
                        } catch (Exception e) {
                            logger.error("Erreur d'authentification WebSocket: {}", e.getMessage());
                            throw new IllegalArgumentException("Authentification échouée");
                        }
                    } else {
                        logger.warn("Connexion WebSocket sans token JWT");
                        throw new IllegalArgumentException("Token JWT requis");
                    }
                }
                
                return message;
            }
        });
    }
}
