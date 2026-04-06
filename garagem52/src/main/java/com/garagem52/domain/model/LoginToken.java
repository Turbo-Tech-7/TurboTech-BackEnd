package com.garagem52.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Token temporário usado para autenticação em duas etapas no login.
 * Após validar email+senha, o usuário recebe um código de 6 dígitos
 * por e-mail — só então o JWT é emitido.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginToken {

    private Long   id;
    private Long   userId;
    private String token;
    private LocalDateTime expiresAt;
    private boolean used;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
