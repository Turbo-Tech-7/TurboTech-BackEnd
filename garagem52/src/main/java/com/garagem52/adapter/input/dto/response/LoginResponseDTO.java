package com.garagem52.adapter.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO de saída do login.
 * Retorna o token JWT para uso nas próximas requisições:
 * Header: Authorization: Bearer <token>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String type;
    private String email;
    private String name;
}
