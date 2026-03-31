package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateUserRequestDTO;
import com.garagem52.adapter.input.dto.request.LoginRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateUserRequestDTO;
import com.garagem52.adapter.input.dto.response.LoginResponseDTO;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.domain.exception.user.EmailAlreadyExistsException;
import com.garagem52.domain.exception.user.UserNotFoundException;
import com.garagem52.domain.model.User;
import com.garagem52.ports.input.UserInputPort;
import com.garagem52.ports.output.UserOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final UserOutputPort userOutputPort;   // OUTPUT PORT — não sabe quem implementa
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
     * Caso de uso: Autenticação (login).
     * Valida credenciais e gera um token JWT stateless.
     * JWT elimina sessão no servidor: cada requisição subsequente
     * apresenta o token, que é validado pela assinatura criptográfica.
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userOutputPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRegra().name());

        return LoginResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .name(user.getName())
                .build();
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
