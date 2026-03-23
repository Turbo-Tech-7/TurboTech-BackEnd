package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.request.UpdateUserRequestDTO;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.ports.input.UserInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HEXAGONAL — input ADAPTER (Controller)
 * Adapter HTTP de entrada para operações CRUD de usuários.
 * Todos os endpoints são PROTEGIDOS — requerem:
 *   Authorization: Bearer <token_obtido_no_login>
 * O JwtAuthFilter (config/) valida o token antes deste controller ser chamado.
 * Este controller delega tudo ao UserInputPort (porta de entrada do domínio).
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInputPort userInputPort;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userInputPort.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userInputPort.findById(id));
    }

    @GetMapping("/findByEmail")
    ResponseEntity<UserResponseDTO> findByEmail(@RequestParam String email){
        return ResponseEntity.ok(userInputPort.findByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(userInputPort.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
