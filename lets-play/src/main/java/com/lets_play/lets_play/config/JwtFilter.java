package com.lets_play.lets_play.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupérer le header "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // 2. Le token doit commencer par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                logger.error("Impossible d'extraire l'email du token : " + e.getMessage());
            }
        }

        // 3. Si on a l'email et que l'utilisateur n'est pas déjà authentifié dans Spring
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (!jwtUtil.isTokenExpired(token)) {
                String role = jwtUtil.extractRole(token);
                
                // AJOUT SÉCURITÉ : On vérifie que le rôle n'est pas null avant de le manipuler
                if (role != null && !role.isEmpty()) {
                    
                    
                    String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                    
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(formattedRole);
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.singletonList(authority)
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // On injecte l'utilisateur authentifié avec son rôle formaté
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}