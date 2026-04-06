package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Enviado na segunda etapa do login: e-mail + código recebido por e-mail.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VerifyLoginCodeRequestDTO {

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Código é obrigatório")
    private String codigo;
}
