package com.garagem52.ports.output;

import com.garagem52.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * HEXAGONAL — OUTPUT PORT (Porta de Saída)
 * Contrato que o domínio define para se comunicar com a infraestrutura de dados.
 * O domínio nunca sabe quem implementa essa porta (MySQL, MongoDB, in-memory...).
 * Quem implementa: UserRepositoryAdapter (adapter/output/persistence/database).
 * Princípio da Inversão de Dependência (DIP):
 *   - O domínio define a interface (abstração)
 *   - A infraestrutura (JPA + MySQL) obedece a interface
 *   - A dependência aponta para DENTRO do hexágono, nunca para fora
 */
public interface UserOutputPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);
}
