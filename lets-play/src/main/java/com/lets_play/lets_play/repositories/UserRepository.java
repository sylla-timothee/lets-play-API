package com.lets_play.lets_play.repositories;

import com.lets_play.lets_play.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}