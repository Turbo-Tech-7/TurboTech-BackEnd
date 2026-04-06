package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.VerifyLoginCodeRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;

import java.util.List;

/**
 * HEXAGONAL — INPUT PORT (Porta de Entrada)
 * Interface que implementa um contrato com as camadas de persistencia do adapter
 * Os controllers (adapter/input) dependem APENAS desta interface —
 * nunca da implementação concreta (UserService).
 *
 * Assim, trocar o canal de entrada (REST → gRPC, CLI, mensageria) não
 * afeta nada no domínio ou na aplicação.
 */
public interface UserInputPort {

    UserResponseDTO cadastro(CreateUserRequestDTO request);

    /** Etapa 1: valida email+senha e envia código 2FA por e-mail */
    MessageResponse login(LoginRequestDTO request);

    /** Etapa 2: valida o código recebido e retorna o JWT */
    LoginResponseDTO verificarCodigoLogin(VerifyLoginCodeRequestDTO request);

    UserResponseDTO findById(Long id);

    UserResponseDTO findByEmail(String email);

    List<UserResponseDTO> findAll();

    UserResponseDTO update(Long id, UpdateUserRequestDTO request);

    void delete(Long id);
}