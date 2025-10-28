package com.example.rpg.service;

import com.example.rpg.model.Utilisateur;
import com.example.rpg.repository.UtilisateurRepository;
import com.example.rpg.security.JwtUtil;
import com.example.rpg.exception.UserAlreadyExistsException;
import com.example.rpg.exception.InvalidCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UtilisateurRepository utilisateurRepository, JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtil = jwtUtil;
    }

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
