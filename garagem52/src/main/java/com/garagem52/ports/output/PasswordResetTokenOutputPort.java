package com.garagem52.ports.output;

import com.garagem52.domain.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenOutputPort {

    void salvar(PasswordResetToken token);

    Optional<PasswordResetToken> buscarPorToken(String token);

    void deletar(PasswordResetToken token);

}
