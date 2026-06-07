package com.lets_play.lets_play.repositories;

import com.lets_play.lets_play.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    
    List<Product> findByUserId(String userId);
}