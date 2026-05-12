package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.request.CreateClienteVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.ports.input.ClienteVeiculoInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint usado pelo mecânico para registrar a entrada de um cliente.
 * Corresponde à tela "Cadastrar Cliente" do wireframe.
 *
 * Fluxo:
 *   1. Mecânico informa: Placa, Nome, Telefone, Modelo, Email
 *   2. Sistema salva em "cliente_veiculo" e tenta vincular ao veículo
 *      cadastrado na collection "veiculo" (pelo campo placa)
 *   3. O id retornado é usado para criar o orçamento (clienteVeiculoId)
 */
@RestController
@RequestMapping("/clientes-veiculos")
@RequiredArgsConstructor
@Tag(name = "Clientes & Veículos", description = "Entrada de clientes na oficina — cadastro feito pelo mecânico")
public class ClienteVeiculoController {

    private final ClienteVeiculoInputPort inputPort;

    /**
     * POST /clientes-veiculos
     * Registra a entrada de um cliente com seu veículo.
     */
    @PostMapping
    @Operation(summary = "Cadastrar cliente com veículo",
               description = "Registra os dados do cliente e do veículo. " +
                             "Se a placa já existir na collection 'veiculo', o vinculo é feito automaticamente.")
    public ResponseEntity<ClienteVeiculoResponseDTO> cadastrar(
            @Valid @RequestBody CreateClienteVeiculoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inputPort.cadastrar(request));
    }

    /** GET /clientes-veiculos/{id} */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<ClienteVeiculoResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(inputPort.findById(id));
    }

    /** GET /clientes-veiculos */
    @GetMapping
    @Operation(summary = "Listar todos")
    public ResponseEntity<List<ClienteVeiculoResponseDTO>> findAll() {
        return ResponseEntity.ok(inputPort.findAll());
    }

    /** GET /clientes-veiculos/buscar-nome?nome=João */
    @GetMapping("/buscar-nome")
    @Operation(summary = "Buscar por nome do cliente")
    public ResponseEntity<List<ClienteVeiculoResponseDTO>> findByNome(@RequestParam String nome) {
        return ResponseEntity.ok(inputPort.findByNome(nome));
    }

    /** GET /clientes-veiculos/buscar-placa?placa=ABC1234 */
    @GetMapping("/buscar-placa")
    @Operation(summary = "Buscar por placa do veículo")
    public ResponseEntity<List<ClienteVeiculoResponseDTO>> findByPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(inputPort.findByPlaca(placa));
    }

    /** DELETE /clientes-veiculos/{id} */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover registro")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        inputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
