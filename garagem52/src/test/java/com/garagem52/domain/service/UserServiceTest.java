package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.*;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.domain.exception.user.EmailAlreadyExistsException;
import com.garagem52.domain.exception.user.InvalidTokenException;
import com.garagem52.domain.exception.user.UserNotFoundException;
import com.garagem52.domain.model.LoginToken;
import com.garagem52.domain.model.User;
import com.garagem52.ports.output.LoginTokenOutputPort;
import com.garagem52.ports.output.UserOutputPort;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserOutputPort userOutputPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoginTokenOutputPort loginTokenOutputPort;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id("1")
                .name("João")
                .email("joao@email.com")
                .senha("senha-criptografada")
                .telefone("11999999999")
                .cep("12345-000")
                .regra(User.Role.USER)
                .build();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setName("João");
        request.setEmail("joao@email.com");
        request.setSenha("123");
        request.setTelefone("119999");
        request.setCep("12345");

        when(userOutputPort.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userOutputPort.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = service.cadastro(request);

        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());

        verify(userOutputPort).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setEmail("existente@email.com");

        when(userOutputPort.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.cadastro(request));

        verify(userOutputPort, never()).save(any());
    }

    @Test
    void deveDefinirAdminQuandoCepForNulo() {
        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setName("Admin");
        request.setEmail("admin@email.com");
        request.setSenha("123");
        request.setCep(null);

        User admin = User.builder()
                .id("2")
                .name("Admin")
                .email("admin@email.com")
                .regra(User.Role.ADMIN)
                .build();

        when(userOutputPort.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userOutputPort.save(any(User.class))).thenReturn(admin);

        UserResponseDTO response = service.cadastro(request);

        assertEquals(User.Role.ADMIN, response.getRole());
    }

    @Test
    void deveRealizarLoginComSucesso() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(user.getEmail());
        request.setSenha("123");

        when(userOutputPort.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123", user.getSenha()))
                .thenReturn(true);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MessageResponse response = service.login(request);

        assertTrue(response.getMensagem().contains("Código de verificação enviado"));

        verify(loginTokenOutputPort).salvar(any(LoginToken.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExisteNoLogin() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("naoexiste@email.com");

        when(userOutputPort.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.login(request));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(user.getEmail());
        request.setSenha("errada");

        when(userOutputPort.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> service.login(request));
    }

    @Test
    void deveVerificarCodigoLoginComSucesso() {
        VerifyLoginCodeRequestDTO request = new VerifyLoginCodeRequestDTO();
        request.setCodigo("123456");

        LoginToken token = LoginToken.builder()
                .token("123456")
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        when(loginTokenOutputPort.buscarPorToken("123456"))
                .thenReturn(Optional.of(token));

        when(userOutputPort.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user.getEmail(), user.getRegra().name()))
                .thenReturn("jwt-token");

        LoginResponseDTO response = service.verificarCodigoLogin(request);

        assertEquals("jwt-token", response.getToken());

        verify(loginTokenOutputPort).salvar(token);
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        VerifyLoginCodeRequestDTO request = new VerifyLoginCodeRequestDTO();
        request.setCodigo("000");

        when(loginTokenOutputPort.buscarPorToken("000"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class,
                () -> service.verificarCodigoLogin(request));
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(userOutputPort.findById("1"))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = service.findById("1");

        assertEquals(user.getId(), response.getId());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        when(userOutputPort.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = service.findByEmail(user.getEmail());

        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void deveListarUsuarios() {
        when(userOutputPort.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> response = service.findAll();

        assertEquals(1, response.size());
    }

    @Test
    void deveAtualizarUsuario() {
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        request.setName("Novo Nome");

        when(userOutputPort.findById("1"))
                .thenReturn(Optional.of(user));

        when(userOutputPort.save(any(User.class)))
                .thenReturn(user);

        UserResponseDTO response = service.update("1", request);

        assertNotNull(response);

        verify(userOutputPort).save(any(User.class));
    }

    @Test
    void deveDeletarUsuario() {
        when(userOutputPort.findById("1"))
                .thenReturn(Optional.of(user));

        service.delete("1");

        verify(userOutputPort).deleteById("1");
    }
}