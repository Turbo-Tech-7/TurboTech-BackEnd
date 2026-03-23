package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para atualização parcial de usuário.
 * Campos são opcionais: apenas os informados serão atualizados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {

    private String name;

    @Email(message = "E-mail inválido")
    private String email;

    @Size(min = 11, message = "O telefone deve ter no minimo 11 caracteres")
    private String telefone;

    private String cep;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
}
