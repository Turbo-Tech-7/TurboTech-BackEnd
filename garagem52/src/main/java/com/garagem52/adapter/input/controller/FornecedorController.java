package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.FornecedorControllerSwagger;
import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.ports.input.FornecedorInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController implements FornecedorControllerSwagger {

    private final FornecedorInputPort fornecedorInputPort;

    @Override
    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criar(@Valid @RequestBody CreateFornecedorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorInputPort.criar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorInputPort.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> findAll() {
        return ResponseEntity.ok(fornecedorInputPort.findAll());
    }

    @Override
    @GetMapping("/buscar-nome")
    public ResponseEntity<List<FornecedorResponseDTO>> findByNome(@RequestParam String nome) {
        return ResponseEntity.ok(fornecedorInputPort.findByNome(nome));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody CreateFornecedorRequestDTO request) {
        return ResponseEntity.ok(fornecedorInputPort.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fornecedorInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
