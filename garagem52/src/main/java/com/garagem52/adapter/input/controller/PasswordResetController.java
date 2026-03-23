package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.ports.input.PasswordResetInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetInputPort passwordResetInputPort;

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        passwordResetInputPort.solicitarRecuperação(email);
        return ResponseEntity.ok(new MessageResponse("Se este e-mail estiver cadastrado, você receberá as instruções."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @RequestParam String token,
            @RequestParam String novaSenha) {
        passwordResetInputPort.redefinirSenha(token, novaSenha);
        return ResponseEntity.ok(new MessageResponse("Senha redefinida com sucesso!"));
    }
}
