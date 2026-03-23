package com.garagem52.ports.input;

public interface PasswordResetInputPort {

    void solicitarRecuperação(String email);

    void redefinirSenha(String token, String novaSenha);

}
