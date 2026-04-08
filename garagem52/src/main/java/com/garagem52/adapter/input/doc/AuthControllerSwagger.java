package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.request.VerifyLoginCodeRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.domain.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Endpoints para cadastro e autenticação de usuários")
public interface AuthControllerSwagger {

    @Operation(operationId = "register", summary = "Cadastro de usuário",
            description = "Cria um novo usuário no sistema e retorna seus dados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    ResponseEntity<UserResponseDTO> cadastro(@Valid @RequestBody CreateUserRequestDTO request) throws BusinessException;

    @Operation(operationId = "login", summary = "Login — Etapa 1: validar credenciais",
            description = "Valida e-mail e senha. Se corretos, envia um código de 6 dígitos para o e-mail do usuário. Use POST /auth/login/verify com apenas o código para obter o JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código enviado para o e-mail",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequestDTO request) throws BusinessException;

    @Operation(operationId = "verifyLoginCode", summary = "Login — Etapa 2: verificar código e obter JWT",
            description = "Recebe apenas o código de 6 dígitos enviado por e-mail. O usuário é identificado automaticamente pelo código. Se válido, retorna o token JWT. O código expira em 10 minutos e é de uso único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação concluída — JWT retornado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Código inválido, expirado ou já utilizado")
    })
    ResponseEntity<LoginResponseDTO> verificarCodigo(@Valid @RequestBody VerifyLoginCodeRequestDTO request) throws BusinessException;
}