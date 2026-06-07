package com.lets_play.lets_play.controllers;

import com.lets_play.lets_play.config.JwtUtil;
import com.lets_play.lets_play.models.User;
import com.lets_play.lets_play.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Erreur : Cet email est déjà utilisé !");
        }
        
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        
        return ResponseEntity.ok(savedUser);
    }

@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
    java.util.Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
    
    if (userOpt.isEmpty()) {
        return ResponseEntity.status(401).body("Erreur : Email ou mot de passe incorrect !");
    }

    User user = userOpt.get();

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        return ResponseEntity.status(401).body("Erreur : Email ou mot de passe incorrect !");
    }

    // Génération du token JWT en y incluant l'email et le rôle stocké
    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

    // On renvoie une réponse propre sous forme de Map JSON contenant le Token
    java.util.Map<String, String> response = new java.util.HashMap<>();
    response.put("token", token);
    response.put("email", user.getEmail());
    response.put("role", user.getRole());
    return ResponseEntity.ok(response);
}
}