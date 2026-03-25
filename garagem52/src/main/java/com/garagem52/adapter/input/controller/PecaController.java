package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.ports.input.PecaInputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final PecaInputPort pecaInputPort;

    @GetMapping("/buscar-nome")
    public ResponseEntity<List<PecaResponseDTO>> findByNome(
            @RequestParam String nomePeca) {

        return ResponseEntity.ok(pecaInputPort.findByNome(nomePeca));
    }

    @GetMapping("/buscar-valor")
    public ResponseEntity<List<PecaResponseDTO>> findByValor(
            @RequestParam Double precoPeca) {

        return ResponseEntity.ok(pecaInputPort.findByValor(precoPeca));
    }
}