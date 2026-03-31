package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.ServicoControllerSwagger;
import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.ports.input.ServicoInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController implements ServicoControllerSwagger {

    private final ServicoInputPort servicoInputPort;

    @Override
    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody CreateServicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoInputPort.criar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicoInputPort.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> findAll() {
        return ResponseEntity.ok(servicoInputPort.findAll());
    }

    @Override
    @GetMapping("/por-veiculo")
    public ResponseEntity<List<ServicoResponseDTO>> findByVeiculoId(@RequestParam Long veiculoId) {
        return ResponseEntity.ok(servicoInputPort.findByVeiculoId(veiculoId));
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<ServicoResponseDTO> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(servicoInputPort.updateStatus(id, status));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
