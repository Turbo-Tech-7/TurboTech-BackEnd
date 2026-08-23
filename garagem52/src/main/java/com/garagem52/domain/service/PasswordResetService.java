package com.garagem52.domain.service;

import com.garagem52.domain.exception.user.InvalidTokenException;
import com.garagem52.domain.exception.user.TooManyRequestsException;
import com.garagem52.domain.exception.user.UserNotFoundException;
import com.garagem52.domain.model.PasswordResetToken;
import com.garagem52.domain.model.User;
import com.garagem52.ports.input.PasswordResetInputPort;
import com.garagem52.ports.output.PasswordResetTokenOutputPort;
import com.garagem52.ports.output.UserOutputPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PasswordResetService implements PasswordResetInputPort {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserOutputPort userOutputPort;
    private final PasswordResetTokenOutputPort passwordResetTokenOutputPort;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final RateLimiterService rateLimiterService;

    @Override
    public void solicitarRecuperacao(String email) {
        String chaveRateLimit = "reset-solicitar:" + email.trim().toLowerCase();
        if (!rateLimiterService.podeTentar(chaveRateLimit)) {
            throw new TooManyRequestsException(
                    "Muitas solicitações de redefinição de senha. Tente novamente em alguns minutos.");
        }

        User user = userOutputPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PasswordResetToken resetToken = new PasswordResetToken();
        String codigo = String.format("%06d", SECURE_RANDOM.nextInt(1000000));
        resetToken.setToken(codigo);
        resetToken.setUserId(user.getId());
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        passwordResetTokenOutputPort.salvar(resetToken);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setFrom("garagem.g.52@gmail.com", "Garagem52");
            helper.setSubject("Garagem52 — Redefinição de Senha");
            helper.setText(EmailTemplateService.passwordReset(user.getName(), resetToken.getToken()), true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail de recuperação: " + e.getMessage(), e);
        }
    }

    @Override
    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = passwordResetTokenOutputPort.buscarPorToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));

        String chaveRateLimit = "reset-validar:" + resetToken.getUserId();
        if (!rateLimiterService.podeTentar(chaveRateLimit)) {
            throw new TooManyRequestsException(
                    "Muitas tentativas de redefinição de senha. Tente novamente em alguns minutos.");
        }

        if (resetToken.isExpired() || resetToken.isUsed())
            throw new InvalidTokenException("Token expirado ou já utilizado");

        User user = userOutputPort.findById(resetToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setSenha(passwordEncoder.encode(novaSenha));
        userOutputPort.save(user);

        resetToken.setUsed(true);
        passwordResetTokenOutputPort.salvar(resetToken);
    }
}