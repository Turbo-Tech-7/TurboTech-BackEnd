package com.garagem52.domain.service;

import com.garagem52.domain.exception.user.InvalidTokenException;
import com.garagem52.domain.model.PasswordResetToken;
import com.garagem52.domain.model.User;
import com.garagem52.ports.output.PasswordResetTokenOutputPort;
import com.garagem52.ports.output.UserOutputPort;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserOutputPort userOutputPort;

    @Mock
    private PasswordResetTokenOutputPort passwordResetTokenOutputPort;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private PasswordResetService service;

    @Test
    void deveSolicitarRecuperacaoComSucesso() {
        User user = User.builder()
                .id("1")
                .name("João")
                .email("joao@email.com")
                .build();

        when(userOutputPort.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() ->
                service.solicitarRecuperacao(user.getEmail()));

        verify(passwordResetTokenOutputPort)
                .salvar(any(PasswordResetToken.class));

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(userOutputPort.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.solicitarRecuperacao("x@email.com"));

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveRedefinirSenhaComSucesso() {
        User user = User.builder()
                .id("1")
                .senha("old")
                .build();

        PasswordResetToken token = new PasswordResetToken();
        token.setToken("123456");
        token.setUserId("1");
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        when(passwordResetTokenOutputPort.buscarPorToken("123456"))
                .thenReturn(Optional.of(token));

        when(userOutputPort.findById("1"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("nova"))
                .thenReturn("encoded");

        service.redefinirSenha("123456", "nova");

        assertEquals("encoded", user.getSenha());

        verify(userOutputPort).save(user);

        verify(passwordResetTokenOutputPort)
                .salvar(any(PasswordResetToken.class));
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        when(passwordResetTokenOutputPort.buscarPorToken("000"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class,
                () -> service.redefinirSenha("000", "nova"));
    }

    @Test
    void deveLancarExcecaoQuandoTokenExpirado() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        token.setUsed(false);

        when(passwordResetTokenOutputPort.buscarPorToken("123"))
                .thenReturn(Optional.of(token));

        assertThrows(InvalidTokenException.class,
                () -> service.redefinirSenha("123", "nova"));
    }

    @Test
    void deveLancarExcecaoQuandoTokenJaUtilizado() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        token.setUsed(true);

        when(passwordResetTokenOutputPort.buscarPorToken("123"))
                .thenReturn(Optional.of(token));

        assertThrows(InvalidTokenException.class,
                () -> service.redefinirSenha("123", "nova"));
    }
}