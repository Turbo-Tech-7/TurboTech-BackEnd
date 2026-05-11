package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.output.persistence.entity.UserEntity;
import com.garagem52.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
