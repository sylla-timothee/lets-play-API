package com.lets_play.lets_play.services; // Vérifie que le package correspond bien à ton architecture

import com.lets_play.lets_play.models.Product;
import com.lets_play.lets_play.models.User;
import com.lets_play.lets_play.repositories.ProductRepository;
import com.lets_play.lets_play.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("productService")
public class ProductService {

    @Autowired
    private ProductRepository productRepository; 

    @Autowired
    private UserRepository userRepository; 

    public boolean isOwner(String productId, String userEmail) {
        // 1. On cherche le produit par son ID 
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return false;
        
        // 2. On récupère l'utilisateur par son email
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) return false;
        
        // 3. On compare l'ID du créateur stocké dans le produit avec l'ID de l'utilisateur connecté
        String userIdFromProduct = productOpt.get().getUserId();
        String currentUserId = userOpt.get().getId();

        return userIdFromProduct != null && userIdFromProduct.equals(currentUserId);
    }
}