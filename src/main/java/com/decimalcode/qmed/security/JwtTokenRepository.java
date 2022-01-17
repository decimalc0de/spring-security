package com.decimalcode.qmed.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JwtTokenRepository extends JpaRepository<JwtToken, UUID> {
    Optional<JwtToken>  findById(UUID uuid);
}
