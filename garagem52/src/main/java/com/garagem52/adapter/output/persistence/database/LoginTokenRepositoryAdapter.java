package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.LoginTokenEntity;
import com.garagem52.adapter.output.persistence.repository.MongoLoginTokenRepository;
import com.garagem52.domain.model.LoginToken;
import com.garagem52.ports.output.LoginTokenOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginTokenRepositoryAdapter implements LoginTokenOutputPort {

    private final MongoLoginTokenRepository repository;

    @Override
    public LoginToken salvar(LoginToken token) {
        return toDomain(repository.save(toEntity(token)));
    }

    @Override
    public Optional<LoginToken> buscarPorToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    @Override
    public void deletarPorUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    private LoginTokenEntity toEntity(LoginToken t) {
        return LoginTokenEntity.builder()
                .id(t.getId())
                .userId(t.getUserId())
                .token(t.getToken())
                .expiresAt(t.getExpiresAt())
                .used(t.isUsed())
                .build();
    }

    private LoginToken toDomain(LoginTokenEntity e) {
        return LoginToken.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .token(e.getToken())
                .expiresAt(e.getExpiresAt())
                .used(e.isUsed())
                .build();
    }
}
