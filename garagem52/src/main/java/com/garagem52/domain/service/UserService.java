package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.VerifyLoginCodeRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.domain.exception.user.EmailAlreadyExistsException;
import com.garagem52.domain.exception.user.InvalidTokenException;
import com.garagem52.domain.exception.user.UserNotFoundException;
import com.garagem52.domain.model.LoginToken;
import com.garagem52.domain.model.User;
import com.garagem52.ports.input.UserInputPort;
import com.garagem52.ports.output.LoginTokenOutputPort;
import com.garagem52.ports.output.UserOutputPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * HEXAGONAL — DOMAIN SERVICE
 * Implementa a porta de entrada (UserInputPort) — é o coração da aplicação.
 * Orquestra os casos de uso usando o modelo de domínio e delega persistência
 * ao UserOutputPort, sem jamais saber se é MySQL, MongoDB ou outro banco.
 * Instanciado pelo BeanConfiguration (config/) via @Bean, com injeção do
 * UserOutputPort (que é implementado pelo UserRepositoryAdapter).
 * Depende de:
 *   UserOutputPort  → saída para o banco (inversão de dependência)
 *   JwtService      → geração de token (infraestrutura injetada como dependência)
 *   PasswordEncoder → hash bcrypt (Spring Security)
 */
@RequiredArgsConstructor
public class UserService implements UserInputPort {

    private final UserOutputPort userOutputPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginTokenOutputPort loginTokenOutputPort;
    private final JavaMailSender mailSender;

    /**
     * Caso de uso: Cadastrar novo usuário.
     * Garante unicidade do e-mail e faz hash da senha antes de persistir.
     */
    @Override
    @Transactional
    public UserResponseDTO cadastro(CreateUserRequestDTO request) {
        if (userOutputPort.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // bcrypt hash
                .telefone(request.getTelefone())
                .cep(request.getCep())
                .regra(User.Role.USER)
                .build();

        if (user.getCep() == null || user.getCep().isBlank()) {
            user.setRegra(User.Role.ADMIN);
        }

        User saved = userOutputPort.save(user);
        return toResponseDTO(saved);
    }

    /**
     * Caso de uso: Autenticação — Etapa 1.
     * Valida email e senha. Se corretos, envia código de 6 dígitos por e-mail.
     * O JWT só é emitido após verificarCodigoLogin().
     */
    @Override
    public MessageResponse login(LoginRequestDTO request) {
        User user = userOutputPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        loginTokenOutputPort.deletarPorUserId(user.getId());

        String codigo = String.format("%06d", new Random().nextInt(1_000_000));
        LoginToken loginToken = LoginToken.builder()
                .userId(user.getId())
                .token(codigo)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();
        loginTokenOutputPort.salvar(loginToken);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setFrom("garagem.g.52@gmail.com", "Garagem52");
            helper.setSubject("Garagem52 — Código de Acesso");
            helper.setText(EmailTemplateService.loginCode(user.getName(), codigo), true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail de autenticação: " + e.getMessage(), e);
        }

        return new MessageResponse("Código de verificação enviado para " + maskEmail(user.getEmail()));
    }

    /**
     * Caso de uso: Autenticação — Etapa 2.
     * Recebe apenas o código de 6 dígitos. O usuário é identificado pelo próprio
     * token armazenado (userId), eliminando a necessidade de enviar o e-mail novamente.
     */
    @Override
    public LoginResponseDTO verificarCodigoLogin(VerifyLoginCodeRequestDTO request) {
        LoginToken loginToken = loginTokenOutputPort.buscarPorToken(request.getCodigo())
                .orElseThrow(() -> new InvalidTokenException("Código inválido"));

        if (loginToken.isExpired() || loginToken.isUsed()) {
            throw new InvalidTokenException("Código expirado ou já utilizado");
        }

        User user = userOutputPort.findById(loginToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException(loginToken.getUserId()));

        loginToken.setUsed(true);
        loginTokenOutputPort.salvar(loginToken);

        String token = jwtService.generateToken(user.getEmail(), user.getRegra().name());

        return LoginResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /** Mascara o e-mail para exibição: ex**@gmail.com */
    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 2) return email;
        return email.substring(0, 2) + "**" + email.substring(at);
    }

    /**
     * Caso de uso: Buscar usuário por ID.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userOutputPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toResponseDTO(user);
    }

    /**
     * Caso de uso: Buscar usuário por email
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userOutputPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return toResponseDTO(user);
    }

    /**
     * Caso de uso: Listar todos os usuários.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userOutputPort.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Caso de uso: Atualizar usuário (patch semântico — só altera campos enviados).
     */
    @Override
    @Transactional
    public UserResponseDTO update(Long id, UpdateUserRequestDTO request) {
        User user = userOutputPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userOutputPort.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException(request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getSenha() != null) {
            user.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return toResponseDTO(userOutputPort.save(user));
    }

    /**
     * Caso de uso: Remover usuário.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        userOutputPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userOutputPort.deleteById(id);
    }

    /** Conversão manual User (domínio) → UserResponseDTO — sem MapStruct no service */
    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .telefone(user.getTelefone())
                .cep(user.getCep())
                .role(user.getRegra())
                .build();
    }
}