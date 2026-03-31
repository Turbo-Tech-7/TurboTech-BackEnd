package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.VeiculoControllerSwagger;
import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.ports.input.VeiculoInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController implements VeiculoControllerSwagger {

    private final VeiculoInputPort veiculoInputPort;

    @Override
    @PostMapping("/criar/{placa}")
    public ResponseEntity<VeiculoResponseDTO> criarVeiculo(@Valid @PathVariable String placa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoInputPort.criarVeiculo(placa));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(veiculoInputPort.findById(id));
    }

    @Override
    @GetMapping("/buscar-placa")
    public ResponseEntity<VeiculoResponseDTO> findByPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(veiculoInputPort.findByPlaca(placa));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> findAll() {
        return ResponseEntity.ok(veiculoInputPort.findAll());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody UpdateVeiculoRequestDTO requestDTO) {
        return ResponseEntity.ok(veiculoInputPort.updateVeiculo(id, requestDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veiculoInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
