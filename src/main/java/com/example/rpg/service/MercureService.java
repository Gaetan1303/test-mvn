package com.example.rpg.service;

import com.example.rpg.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Service pour publier des messages via Mercure Hub
 * Utilisé pour le chat et les notifications non-critiques
 */
@Service
public class MercureService {
    
    private static final Logger logger = LoggerFactory.getLogger(MercureService.class);
    
    @Value("${mercure.hub.url:http://mercure:80/.well-known/mercure}")
    private String mercureHubUrl;
    
    @Value("${mercure.jwt.secret:!ChangeThisMercureSecretKey!}")
    private String mercureJwtSecret;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public MercureService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Publie un message de chat via Mercure
     */
    public void publishChatMessage(ChatMessage message) {
        try {
            String topic = "chat/" + message.getChannel();
            String data = objectMapper.writeValueAsString(message);
            
            publishToMercure(topic, data);
            
            logger.debug("Message chat publié - Channel: {}, User: {}", 
                        message.getChannel(), message.getUsername());
        } catch (Exception e) {
            logger.error("Erreur lors de la publication du message chat", e);
        }
    }
    
    /**
     * Publie un événement de notification via Mercure
     */
    public void publishNotification(String topic, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            publishToMercure(topic, jsonData);
            
            logger.debug("Notification publiée - Topic: {}", topic);
        } catch (Exception e) {
            logger.error("Erreur lors de la publication de notification", e);
        }
    }
    
    /**
     * Méthode privée pour publier sur Mercure Hub
     */
    private void publishToMercure(String topic, String data) {
        try {
            // Générer un JWT pour l'autorisation de publication
            String publisherJwt = generatePublisherJwt();
            
            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(publisherJwt);
            
            // Préparer le body (form-urlencoded)
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("topic", topic);
            body.add("data", data);
            
            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            
            // Envoyer la requête POST
            ResponseEntity<String> response = restTemplate.postForEntity(
                mercureHubUrl, 
                request, 
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.trace("Publié sur Mercure - Topic: {}", topic);
            } else {
                logger.warn("Réponse inattendue de Mercure: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("Erreur lors de la publication sur Mercure Hub: {}", e.getMessage());
            // Ne pas lancer d'exception pour éviter d'interrompre le flux principal
        }
    }
    
    /**
     * Génère un JWT pour autoriser la publication sur Mercure
     */
    private String generatePublisherJwt() {
        return Jwts.builder()
            .claim("mercure", new MercureClaim())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
            .signWith(Keys.hmacShaKeyFor(mercureJwtSecret.getBytes()))
            .compact();
    }
    
    /**
     * Claim Mercure pour autoriser la publication
     */
    private static class MercureClaim {
        public String[] publish = {"*"}; // Peut publier sur tous les topics
        
        public String[] getPublish() {
            return publish;
        }
    }
}
