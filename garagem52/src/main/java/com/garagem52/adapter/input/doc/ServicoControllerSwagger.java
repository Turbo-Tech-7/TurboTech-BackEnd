package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
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

@Tag(name = "Serviços", description = "Gerenciamento de ordens de serviço")
@SecurityRequirement(name = "bearerAuth")
public interface ServicoControllerSwagger {

    @Operation(summary = "Abrir novo serviço")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Serviço criado",
                    content = @Content(schema = @Schema(implementation = ServicoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody CreateServicoRequestDTO request);

    @Operation(summary = "Buscar serviço por ID")
    ResponseEntity<ServicoResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Listar todos os serviços")
    ResponseEntity<List<ServicoResponseDTO>> findAll();

    @Operation(summary = "Buscar serviços por veículo")
    ResponseEntity<List<ServicoResponseDTO>> findByVeiculoId(@RequestParam Long veiculoId);

    @Operation(summary = "Atualizar status do serviço (ex: ABERTO, EM_ANDAMENTO, CONCLUIDO)")
    ResponseEntity<ServicoResponseDTO> updateStatus(@PathVariable Long id, @RequestParam String status);

    @Operation(summary = "Deletar serviço")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
