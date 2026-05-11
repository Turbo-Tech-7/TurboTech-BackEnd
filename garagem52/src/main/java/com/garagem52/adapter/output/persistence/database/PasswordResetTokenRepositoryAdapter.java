package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.PasswordResetTokenMapper;
import com.garagem52.adapter.output.persistence.repository.MongoPasswordResetTokenRepository;
import com.garagem52.domain.model.PasswordResetToken;
import com.garagem52.ports.output.PasswordResetTokenOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenOutputPort {

    private final MongoPasswordResetTokenRepository repository;
    private final PasswordResetTokenMapper mapper;

    @Override
    public void salvar(PasswordResetToken token) {
        repository.save(mapper.toEntity(token));
    }

    @Override
    public Optional<PasswordResetToken> buscarPorToken(String token) {
        return repository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public void deletar(PasswordResetToken token) {
        if (token.getId() != null) {
            repository.deleteById(token.getId());
        }
    }
}
