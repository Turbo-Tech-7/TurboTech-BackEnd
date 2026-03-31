package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.OrcamentoControllerSwagger;
import com.garagem52.adapter.input.dto.request.CancelarOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.CreateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.ports.input.OrcamentoInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController implements OrcamentoControllerSwagger {

    private final OrcamentoInputPort orcamentoInputPort;

    @Override
    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criar(@Valid @RequestBody CreateOrcamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoInputPort.criar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orcamentoInputPort.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> findAll() {
        return ResponseEntity.ok(orcamentoInputPort.findAll());
    }

    @Override
    @GetMapping("/por-veiculo")
    public ResponseEntity<List<OrcamentoResponseDTO>> findByVeiculoId(@RequestParam Long veiculoId) {
        return ResponseEntity.ok(orcamentoInputPort.findByVeiculoId(veiculoId));
    }

    @Override
    @GetMapping("/por-status")
    public ResponseEntity<List<OrcamentoResponseDTO>> findByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orcamentoInputPort.findByStatus(status));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody UpdateOrcamentoRequestDTO request) {
        return ResponseEntity.ok(orcamentoInputPort.update(id, request));
    }

    @Override
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<OrcamentoResponseDTO> cancelar(
            @PathVariable Long id, @Valid @RequestBody CancelarOrcamentoRequestDTO request) {
        return ResponseEntity.ok(orcamentoInputPort.cancelar(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orcamentoInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
