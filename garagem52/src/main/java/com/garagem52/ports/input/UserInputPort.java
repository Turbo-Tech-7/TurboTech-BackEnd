package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.*;
import com.garagem52.adapter.input.dto.response.*;

import java.util.List;

public interface UserInputPort {
    UserResponseDTO cadastro(CreateUserRequestDTO request);
    MessageResponse login(LoginRequestDTO request);
    LoginResponseDTO verificarCodigoLogin(VerifyLoginCodeRequestDTO request);
    UserResponseDTO findById(String id);
    UserResponseDTO findByEmail(String email);
    List<UserResponseDTO> findAll();
    UserResponseDTO update(String id, UpdateUserRequestDTO request);
    void delete(String id);
}
