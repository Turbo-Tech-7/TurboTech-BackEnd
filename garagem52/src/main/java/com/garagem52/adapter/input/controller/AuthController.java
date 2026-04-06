package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.AuthControllerSwagger;
import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.request.VerifyLoginCodeRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.ports.input.UserInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HEXAGONAL — input ADAPTER (Controller)
 * Adapter HTTP de entrada para autenticação.
 *
 * Endpoints públicos (sem JWT):
 *   POST /auth/register      → cadastro
 *   POST /auth/login         → etapa 1: valida email+senha, envia código por e-mail
 *   POST /auth/login/verify  → etapa 2: valida código e retorna token JWT
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private final UserInputPort userInputPort;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> cadastro(@Valid @RequestBody CreateUserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userInputPort.cadastro(request));
    }

    /**
     * Etapa 1 do login: valida credenciais e dispara o código 2FA por e-mail.
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(userInputPort.login(request));
    }

    /**
     * Etapa 2 do login: valida o código recebido por e-mail e emite o JWT.
     */
    @Override
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponseDTO> verificarCodigo(@Valid @RequestBody VerifyLoginCodeRequestDTO request) {
        return ResponseEntity.ok(userInputPort.verificarCodigoLogin(request));
    }
}
