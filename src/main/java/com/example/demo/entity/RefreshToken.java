package com.example.demo.entity;


import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name="refresh_tokens")
@Data
public class RefreshToken {
@Id
private UUID id;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;

@Column(nullable = false, unique = true, length = 500)
private String token;

@Column(nullable = false)
private LocalDateTime expiry;

private boolean revoked = false;

@PrePersist
void onCreate() {
    this.id = UUID.randomUUID();
}}