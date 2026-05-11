package com.garagem52.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetToken {
    private String id;      // ObjectId do MongoDB
    private String token;
    private String userId;  // referência por ObjectId
    private LocalDateTime expiresAt;
    private boolean used;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
