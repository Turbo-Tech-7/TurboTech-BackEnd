package com.garagem52.config;

import com.garagem52.adapter.output.persistence.mapper.*;
import com.garagem52.domain.service.*;
import com.garagem52.ports.input.OrcamentoInputPort;
import com.garagem52.ports.output.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserService userServicePort(
            UserOutputPort userOutputPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            LoginTokenOutputPort loginTokenOutputPort,
            JavaMailSender mailSender) {
        return new UserService(userOutputPort, passwordEncoder, jwtService,
                               loginTokenOutputPort, mailSender);
    }

    @Bean
    public VeiculoService veiculoServicePort(
            VeiculoOutputPort veiculoOutputPort,
            VeiculoPersistenceMapper mapper,
            WebClient webClient) {
        return new VeiculoService(veiculoOutputPort, mapper, webClient);
    }

    @Bean
    public PasswordResetService passwordResetService(
            UserOutputPort userOutputPort,
            PasswordResetTokenOutputPort passwordResetTokenOutputPort,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        return new PasswordResetService(userOutputPort, passwordResetTokenOutputPort, mailSender, passwordEncoder);
    }

    @Bean
    public PecaService pecaService(
            PecaOutputPort pecaOutputPort,
            PecaPersistenceMapper pecaPersistenceMapper) {
        return new PecaService(pecaOutputPort, pecaPersistenceMapper);
    }

    @Bean
    public FornecedorService fornecedorService(
            FornecedorOutputPort fornecedorOutputPort,
            FornecedorPersistenceMapper mapper) {
        return new FornecedorService(fornecedorOutputPort, mapper);
    }

    @Bean
    public ServicoService servicoService(
            ServicoOutputPort servicoOutputPort,
            ServicoMapper mapper) {
        return new ServicoService(servicoOutputPort, mapper);
    }

    @Bean
    public OrcamentoService orcamentoService(
            OrcamentoOutputPort orcamentoOutputPort,
            OrcamentoMapper mapper) {
        return new OrcamentoService(orcamentoOutputPort, mapper);
    }

    @Bean
    public OrcamentoPdfService orcamentoPdfService() {
        return new OrcamentoPdfService();
    }

    @Bean
    public OrcamentoPdfApplicationService orcamentoPdfApplicationService(
            OrcamentoInputPort orcamentoInputPort,
            OrcamentoPdfService orcamentoPdfService,
            JavaMailSender mailSender) {
        return new OrcamentoPdfApplicationService(orcamentoInputPort, orcamentoPdfService, mailSender);
    }
}
