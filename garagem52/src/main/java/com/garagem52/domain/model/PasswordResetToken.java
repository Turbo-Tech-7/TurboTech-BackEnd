package com.garagem52.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetToken {

    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expiresAt;
    private boolean used;

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
