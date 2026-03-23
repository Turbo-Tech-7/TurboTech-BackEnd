package com.garagem52.config;

import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.domain.service.JwtService;
import com.garagem52.domain.service.PasswordResetService;
import com.garagem52.domain.service.VeiculoService;
import com.garagem52.ports.output.PasswordResetTokenOutputPort;
import com.garagem52.ports.output.UserOutputPort;
import com.garagem52.domain.service.UserService;
import com.garagem52.ports.output.VeiculoOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HEXAGONAL — BEAN CONFIGURATION
 * Responsável por instanciar os Services do domínio e injetar suas dependências.
 * Por que existe essa classe?
 *   O UserService (domínio) não tem @Service — ele não deve depender do Spring.
 *   A instância é criada aqui, no limite entre domínio e infraestrutura,
 *   recebendo a implementação concreta do OutputPort (UserRepositoryAdapter).
 * Vantagem: para trocar a implementação do banco (ex: criar MongoUserRepositoryAdapter),
 *   basta alterar este Bean. O domínio e os controllers continuam inalterados!
 */
@Configuration
public class BeanConfiguration {

    /**
     * Cria o bean do UserService.
     * O Spring vai:
     *   1. Criar uma instância de UserService
     *   2. Injetar a implementação de UserOutputPort (o UserRepositoryAdapter JPA)
     *   3. Disponibilizar como UserInputPort para os controllers
     * Isso permite que o controller dependa apenas da interface (Port),
     * não da implementação concreta (Service).
     */
    @Bean
    public UserService userServicePort(
            UserOutputPort userOutputPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        return new UserService(userOutputPort, passwordEncoder, jwtService);
    }

    @Bean
    public VeiculoService veiculoServicePort(
            VeiculoOutputPort veiculoOutputPort,
            VeiculoPersistenceMapper mapper,
            WebClient webClient
    ){
        return new VeiculoService(veiculoOutputPort, mapper, webClient);
    }

    @Bean
    public PasswordResetService passwordResetService(
            UserOutputPort userOutputPort,
            PasswordResetTokenOutputPort passwordResetTokenOutputPort,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder
    ){
        return  new PasswordResetService(userOutputPort, passwordResetTokenOutputPort, mailSender, passwordEncoder);
    }
}
