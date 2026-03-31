package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.AuthControllerSwagger;
import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
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
 * Depende APENAS de UserInputPort (interface) — nunca do UserService diretamente.
 * Isso é a inversão de dependência em ação: o adapter não acopla com a implementação.
 * Endpoints públicos (sem JWT):
 *   POST /auth/register → cadastro
 *   POST /auth/login    → retorna token JWT
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private final UserInputPort userInputPort;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> cadastro(@Valid @RequestBody CreateUserRequestDTO request) {
        UserResponseDTO response = userInputPort.cadastro(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(userInputPort.login(request));
    }
}
