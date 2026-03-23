package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.adapter.output.persistence.entity.UserEntity;
import com.garagem52.domain.model.User;
import org.mapstruct.Mapper;

/**
 * ADAPTER/OUTPUT — PERSISTENCE MAPPER
 * Converte entre o modelo de domínio (User) e a entidade JPA (UserEntity).
 * Por que manter dois modelos separados?
 *   UserEntity carrega anotações JPA — é específica de infraestrutura
 *   User (domínio) é pura — sem dependência de nenhum framework
 *   Essa separação permite trocar o banco (MySQL → MongoDB) sem tocar no domínio
 * MapStruct gera a implementação em tempo de COMPILAÇÃO (sem reflection em runtime),
 * mapeando automaticamente campos de mesmo nome entre as classes.
 * O enum Role.USER ↔ RoleEntity.USER é mapeado pelo nome automaticamente.
 */
@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    /** User (domínio) → UserEntity (JPA) — usado antes de salvar no banco */
    UserEntity toEntity(User user);

    /** UserEntity (JPA) → User (domínio) — usado após leitura do banco */
    User toDomain(UserEntity entity);
}
