package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.output.persistence.entity.PasswordResetTokenEntity;
import com.garagem52.domain.model.PasswordResetToken;
import org.mapstruct.Mapper;

/**
 * Mapeamento direto — userId é String em ambos os lados.
 * Não há mais @ManyToOne UserEntity para mapear.
 */
@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {
    PasswordResetToken toDomain(PasswordResetTokenEntity entity);
    PasswordResetTokenEntity toEntity(PasswordResetToken domain);
}
