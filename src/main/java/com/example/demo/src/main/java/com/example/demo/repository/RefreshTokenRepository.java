package com.example.demo.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;



public interface RefreshTokenRepository extends JpaRepository<RefreshToken,UUID> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}