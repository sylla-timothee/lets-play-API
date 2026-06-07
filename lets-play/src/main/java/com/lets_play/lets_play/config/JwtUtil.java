package com.lets_play.lets_play.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // génèrer le token
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // token valide pendans 24h
    private static final long EXPIRATION_TIME = 86400000; 

    // 1. Générer le Token à partir de l'email et du rôle de l'utilisateur
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 2. Extraire l'email (le Subject) depuis le Token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 3. Extraire le rôle depuis le Token
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 4. Vérifier si le token a expiré
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}