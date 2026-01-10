package com.forum.boxchat.repository;

import com.forum.boxchat.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional <VerificationToken> findByToken(UUID token);
}
