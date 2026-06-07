package com.lets_play.lets_play.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lets_play.lets_play.config.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // INJECTION DU FILTRE

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactive la protection CSRF (indispensable pour tester des POST/PUT avec Postman)
            .csrf(csrf -> csrf.disable())
            // Autorise absolument toutes les requêtes sans authentification pour le moment
            .authorizeHttpRequests(auth -> auth
                
                // routes accessibles sans connexion
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers("/error").permitAll()

                // 2. Accès réservé aux connectés pour CRÉER un produit (POST)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products/**").authenticated()
    
                // 3. Accès réservé aux connectés pour MODIFIER/SUPPRIMER (La vraie vérification de l'owner se fera dans le Controller via @PreAuthorize)
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/products/**").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/**").authenticated()
                
                // tout les get user sont accessible uniquement aux admins
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                // les requetes qui demande juste une connexion
                .anyRequest().authenticated()
            );

            // On dit à Spring d'utiliser notre filtre JWT
            http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}