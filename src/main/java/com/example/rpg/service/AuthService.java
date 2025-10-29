package com.example.rpg.service;

import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.UtilisateurRepository;
import com.example.rpg.security.JwtUtil;
import com.example.rpg.exception.UserAlreadyExistsException;
import com.example.rpg.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service d'authentification
 * Respecte le principe D (Dependency Inversion) : injecte PasswordEncoder au lieu de le créer
 * Respecte le principe I (Interface Segregation) : implémente l'interface IAuthService
 */
@Service
public class AuthService implements IAuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtilisateurRepository utilisateurRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(String username, String email, String password) {
        if (utilisateurRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Nom d'utilisateur déjà utilisé");
        }
        if (utilisateurRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email déjà utilisé");
        }

        String hashedPassword = passwordEncoder.encode(password);
        Utilisateur utilisateur = new Utilisateur(username, email, hashedPassword);
        utilisateurRepository.save(utilisateur);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", utilisateur.getRoles());

        return jwtUtil.generateToken(claims, utilisateur.getUsername());
    }

    @Override
    public String login(String username, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(password, utilisateur.getPassword())) {
            throw new InvalidCredentialsException("Mot de passe invalide");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", utilisateur.getRoles());

        return jwtUtil.generateToken(claims, utilisateur.getUsername());
    }
}
