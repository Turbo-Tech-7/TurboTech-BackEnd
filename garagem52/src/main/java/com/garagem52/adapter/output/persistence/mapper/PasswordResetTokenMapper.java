package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.output.persistence.entity.PasswordResetTokenEntity;
import com.garagem52.domain.model.PasswordResetToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {

    @Mapping(source = "user.id", target = "userId")
    PasswordResetToken toDomain(PasswordResetTokenEntity entity);

    @Mapping(source = "userId", target = "user.id")
    PasswordResetTokenEntity toEntity(PasswordResetToken domain);
}
