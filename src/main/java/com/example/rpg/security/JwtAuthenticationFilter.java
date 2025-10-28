package com.example.rpg.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// ATTENTION: Filtre non utilisé. SecurityConfig utilise com.example.rpg.service.JwtFilter.
// Conserver ce fichier uniquement à titre de référence; il n'est pas enregistré en tant que bean.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                   @NonNull HttpServletResponse response, 
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");
        
        // Vérifier si le header existe et commence par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7); // Retirer "Bearer "
            
            try {
                // Extraire le username du token
                String username = jwtUtil.extractUsername(jwt);
                
                // Si le username existe et qu'il n'y a pas déjà d'authentification
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Charger les détails de l'utilisateur
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    // Valider le token
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        // Créer l'authentification
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                            );
                        
                        // Définir l'authentification dans le contexte Spring Security
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Token invalide ou expiré - ne rien faire, la requête sera rejetée
                logger.warn("Erreur lors de la validation du JWT: " + e.getMessage());
            }
        }
        
        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
