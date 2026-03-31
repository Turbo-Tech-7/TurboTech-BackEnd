package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.request.CancelarOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.CreateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
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

@Tag(name = "Orçamentos", description = "CRUD completo de orçamentos")
@SecurityRequirement(name = "bearerAuth")
public interface OrcamentoControllerSwagger {

    @Operation(summary = "Criar orçamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrcamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Serviço, veículo ou peça não encontrada")
    })
    ResponseEntity<OrcamentoResponseDTO> criar(@Valid @RequestBody CreateOrcamentoRequestDTO request);

    @Operation(summary = "Buscar orçamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrado",
                    content = @Content(schema = @Schema(implementation = OrcamentoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    ResponseEntity<OrcamentoResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Listar todos os orçamentos")
    ResponseEntity<List<OrcamentoResponseDTO>> findAll();

    @Operation(summary = "Buscar por veículo")
    ResponseEntity<List<OrcamentoResponseDTO>> findByVeiculoId(@RequestParam Long veiculoId);

    @Operation(summary = "Buscar por status (ABERTO, FINALIZADO, CANCELADO)")
    ResponseEntity<List<OrcamentoResponseDTO>> findByStatus(@RequestParam String status);

    @Operation(
            summary = "Editar orçamento",
            description = """
                    Permite editar valores, itens e **status** do orçamento.
                    
                    **Regras de transição de status:**
                    - `ABERTO` → Reabre o orçamento. Não enviar `motivoCancelamento`.
                    - `FINALIZADO` → Conclui o orçamento. Não enviar `motivoCancelamento`.
                    - `CANCELADO` → **`motivoCancelamento` é obrigatório.** Valores aceitos:
                      - `CLIENTE_DESISTIU` → "Cliente desistiu"
                      - `DEMORA_NA_PECA` → "Demora na peça"
                      - `PRECO_ALTO` → "Preços altos"
                      - `OUTROS` → "Outros"
                    
                    Ao reabrir (`ABERTO`) ou concluir (`FINALIZADO`) um orçamento previamente cancelado,
                    o motivo anterior é automaticamente removido.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrcamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Motivo ausente para CANCELADO ou motivo indevido para outros status"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado")
    })
    ResponseEntity<OrcamentoResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody UpdateOrcamentoRequestDTO request);

    @Operation(
            summary = "Cancelar orçamento (atalho semântico)",
            description = """
                    Atalho REST para cancelar sem precisar passar `status` no body.
                    O motivo continua obrigatório.
                    
                    Motivos aceitos:
                    - `CLIENTE_DESISTIU` → "Cliente desistiu"
                    - `DEMORA_NA_PECA` → "Demora na peça"
                    - `PRECO_ALTO` → "Preços altos"
                    - `OUTROS` → "Outros"
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancelado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrcamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Motivo não informado"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado")
    })
    ResponseEntity<OrcamentoResponseDTO> cancelar(@PathVariable Long id,
            @Valid @RequestBody CancelarOrcamentoRequestDTO request);

    @Operation(summary = "Deletar orçamento")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
