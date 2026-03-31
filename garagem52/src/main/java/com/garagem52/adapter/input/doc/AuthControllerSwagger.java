package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
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

    @Operation(
            operationId = "register",
            summary = "Cadastro de usuário",
            description = "Cria um novo usuário no sistema e retorna seus dados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<UserResponseDTO> cadastro(@Valid @RequestBody CreateUserRequestDTO request) throws BusinessException;

    @Operation(
            operationId = "login",
            summary = "Login de usuário",
            description = "Autentica o usuário e retorna o token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) throws BusinessException;
}
