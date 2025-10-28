package com.example.rpg.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration; // en millisecondes

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parser()
               .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
               .build()
               .parseSignedClaims(token)
               .getPayload();
}

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

        /**
         * Valide un token JWT pour un UserDetails
         */
        public boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

   public String generateToken(Map<String, Object> extraClaims, String username) {
    return Jwts.builder()
               .claims(extraClaims)          // .setClaims → .claims
               .subject(username)            // .setSubject → .subject
               .issuedAt(new Date())         // .setIssuedAt → .issuedAt
               .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
               .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())) // plus besoin de préciser HS256
               .compact();
}
}
