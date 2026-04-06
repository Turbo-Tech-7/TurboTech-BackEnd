package com.garagem52.ports.output;

import com.garagem52.domain.model.LoginToken;

import java.util.Optional;

public interface LoginTokenOutputPort {
    LoginToken salvar(LoginToken token);
    Optional<LoginToken> buscarPorToken(String token);
    void deletarPorUserId(Long userId);
}
