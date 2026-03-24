package com.garagem52.ports.input;

public interface PasswordResetInputPort {

    void solicitarRecuperacao(String email);

    void redefinirSenha(String token, String novaSenha);

}
