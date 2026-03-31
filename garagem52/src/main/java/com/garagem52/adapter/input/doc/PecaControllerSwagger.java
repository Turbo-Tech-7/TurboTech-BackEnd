package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Peças", description = "Consulta de peças disponíveis para composição de orçamentos")
@SecurityRequirement(name = "bearerAuth")
public interface PecaControllerSwagger {

    @Operation(summary = "Buscar peças por nome")
    @ApiResponse(responseCode = "200", description = "Peças encontradas")
    ResponseEntity<List<PecaResponseDTO>> findByNome(@RequestParam String nomePeca);

    @Operation(summary = "Buscar peças por valor máximo")
    @ApiResponse(responseCode = "200", description = "Peças encontradas")
    ResponseEntity<List<PecaResponseDTO>> findByValor(@RequestParam Double precoPeca);
}
