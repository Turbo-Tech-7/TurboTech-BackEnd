package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Veículos", description = "Consulta e gerenciamento de veículos via placa (integração API externa)")
@SecurityRequirement(name = "bearerAuth")
public interface VeiculoControllerSwagger {

    @Operation(
            summary = "Cadastrar veículo por placa",
            description = "Consulta a API wdapi2 com a placa informada. Se já existir no banco (cache), retorna direto sem nova requisição externa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado/retornado com sucesso",
                    content = @Content(schema = @Schema(implementation = VeiculoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Formato de placa inválido")
    })
    ResponseEntity<VeiculoResponseDTO> criarVeiculo(@PathVariable String placa);

    @Operation(summary = "Buscar veículo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    ResponseEntity<VeiculoResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Buscar veículo por placa")
    ResponseEntity<VeiculoResponseDTO> findByPlaca(@RequestParam String placa);

    @Operation(summary = "Listar todos os veículos")
    ResponseEntity<List<VeiculoResponseDTO>> findAll();

    @Operation(summary = "Atualizar dados do veículo (marca, modelo, cor)")
    ResponseEntity<VeiculoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateVeiculoRequestDTO requestDTO);

    @Operation(summary = "Deletar veículo")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
