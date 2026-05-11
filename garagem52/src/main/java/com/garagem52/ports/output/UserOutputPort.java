package com.garagem52.ports.output;

import com.garagem52.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserOutputPort {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(String id);
    boolean existsByEmail(String email);
}
