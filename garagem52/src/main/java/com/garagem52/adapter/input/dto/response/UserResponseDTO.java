package com.garagem52.adapter.input.dto.response;

import com.garagem52.domain.model.User;
import lombok.*;

/**
 * DTO de saída para representação de usuário nas respostas da API.
 * O campo "password" é omitido por design — nunca trafega na resposta.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cep;
    private User.Role role;

}
