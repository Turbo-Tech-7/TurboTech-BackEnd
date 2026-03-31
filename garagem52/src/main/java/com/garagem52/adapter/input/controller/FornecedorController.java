package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.ports.input.FornecedorInputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor


public class FornecedorController {

    private final FornecedorInputPort fornecedorInputPort;

    @GetMapping("/buscar-nome")
    public ResponseEntity<List<FornecedorResponseDTO>> findByNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(fornecedorInputPort.findByNome(nome));
    }

    @GetMapping("/buscar-cnpj")
    public ResponseEntity<List<FornecedorResponseDTO>> findByCnpj(
            @RequestParam String cnpj) {

        return ResponseEntity.ok(fornecedorInputPort.findByCnpj(cnpj));
    }
}