package com.garagem52.adapter.input.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Orçamentos — PDF", description = "Geração de PDF do orçamento no padrão Garagem52")
@SecurityRequirement(name = "bearerAuth")
public interface OrcamentoPdfControllerSwagger {

    @Operation(
            summary = "Gerar PDF do orçamento",
            description = """
                    Gera e retorna o PDF completo do orçamento em bytes
                    
                    O documento contém:
                    - Número e data do orçamento
                    - Status atual
                    - Informações do cliente
                    - Detalhes do serviço
                    - Tabela de peças/materiais
                    - Resumo financeiro
                    
                    Resposta: bytes do PDF com header Content-Disposition para download direto.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao gerar o PDF")
    })
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> gerarPdf(
            @Parameter(description = "ID do orçamento", required = true) @PathVariable Long id);
}
