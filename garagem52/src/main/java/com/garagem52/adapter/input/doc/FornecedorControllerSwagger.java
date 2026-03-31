package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Fornecedores", description = "CRUD de fornecedores de peças")
@SecurityRequirement(name = "bearerAuth")
public interface FornecedorControllerSwagger {

    @Operation(summary = "Criar fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fornecedor criado",
                    content = @Content(schema = @Schema(implementation = FornecedorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<FornecedorResponseDTO> criar(@Valid @RequestBody CreateFornecedorRequestDTO request);

    @Operation(summary = "Buscar fornecedor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado",
                    content = @Content(schema = @Schema(implementation = FornecedorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    ResponseEntity<FornecedorResponseDTO> findById(@Parameter(description = "ID do fornecedor") @PathVariable Long id);

    @Operation(summary = "Listar todos os fornecedores")
    ResponseEntity<List<FornecedorResponseDTO>> findAll();

    @Operation(summary = "Buscar fornecedor por nome")
    ResponseEntity<List<FornecedorResponseDTO>> findByNome(@RequestParam String nome);

    @Operation(summary = "Atualizar fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    ResponseEntity<FornecedorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CreateFornecedorRequestDTO request);

    @Operation(summary = "Deletar fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable Long id);
}
