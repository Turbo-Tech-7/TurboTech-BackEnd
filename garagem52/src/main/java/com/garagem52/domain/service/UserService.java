package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.*;
import com.garagem52.adapter.input.dto.response.*;
import com.garagem52.domain.exception.user.*;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService implements UserInputPort {

    private final UserOutputPort userOutputPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginTokenOutputPort loginTokenOutputPort;
    private final JavaMailSender mailSender;

    @Override
    public UserResponseDTO cadastro(CreateUserRequestDTO request) {
        if (userOutputPort.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException(request.getEmail());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .telefone(request.getTelefone())
                .cep(request.getCep())
                .regra(User.Role.USER)
                .build();

        if (user.getCep() == null || user.getCep().isBlank())
            user.setRegra(User.Role.ADMIN);

        return toResponseDTO(userOutputPort.save(user));
    }

    @Override
    public MessageResponse login(LoginRequestDTO request) {
        User user = userOutputPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha()))
            throw new BadCredentialsException("Credenciais inválidas.");

        loginTokenOutputPort.deletarPorUserId(user.getId());

        String codigo = String.format("%06d", new Random().nextInt(1_000_000));
        loginTokenOutputPort.salvar(LoginToken.builder()
                .userId(user.getId())
                .token(codigo)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build());

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

    @Override
    public LoginResponseDTO verificarCodigoLogin(VerifyLoginCodeRequestDTO request) {
        LoginToken loginToken = loginTokenOutputPort.buscarPorToken(request.getCodigo())
                .orElseThrow(() -> new InvalidTokenException("Código inválido"));

        if (loginToken.isExpired() || loginToken.isUsed())
            throw new InvalidTokenException("Código expirado ou já utilizado");

        User user = userOutputPort.findById(loginToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException(loginToken.getUserId()));

        loginToken.setUsed(true);
        loginTokenOutputPort.salvar(loginToken);

        return LoginResponseDTO.builder()
                .token(jwtService.generateToken(user.getEmail(), user.getRegra().name()))
                .type("Bearer")
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Override
    public UserResponseDTO findById(String id) {
        return toResponseDTO(userOutputPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        return toResponseDTO(userOutputPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email)));
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userOutputPort.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO update(String id, UpdateUserRequestDTO request) {
        User user = userOutputPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userOutputPort.existsByEmail(request.getEmail()))
                throw new EmailAlreadyExistsException(request.getEmail());
            user.setEmail(request.getEmail());
        }
        if (request.getSenha() != null) user.setSenha(passwordEncoder.encode(request.getSenha()));

        return toResponseDTO(userOutputPort.save(user));
    }

    @Override
    public void delete(String id) {
        userOutputPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userOutputPort.deleteById(id);
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 2) return email;
        return email.substring(0, 2) + "**" + email.substring(at);
    }

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
