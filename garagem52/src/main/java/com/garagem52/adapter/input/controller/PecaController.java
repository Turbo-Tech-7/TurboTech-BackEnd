package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.PecaControllerSwagger;
import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.ports.input.PecaInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController implements PecaControllerSwagger {

    private final PecaInputPort pecaInputPort;

    @Override
    @GetMapping("/buscar-nome")
    public ResponseEntity<List<PecaResponseDTO>> findByNome(@RequestParam String nomePeca) {
        return ResponseEntity.ok(pecaInputPort.findByNome(nomePeca));
    }

    @Override
    @GetMapping("/buscar-valor")
    public ResponseEntity<List<PecaResponseDTO>> findByValor(@RequestParam Double precoPeca) {
        return ResponseEntity.ok(pecaInputPort.findByValor(precoPeca));
    }
}
