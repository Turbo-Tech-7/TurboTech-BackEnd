package com.garagem52.adapter.input.dto.response;

import com.garagem52.domain.model.User;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String telefone;
    private String cep;
    private User.Role role;
}
