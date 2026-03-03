package com.efatura.authservice.repository;

import com.efatura.authservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
    Boolean existsByEmail(String email);
}
