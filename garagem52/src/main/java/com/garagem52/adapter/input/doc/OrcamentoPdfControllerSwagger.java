package com.garagem52.adapter.input.doc;

import com.garagem52.adapter.input.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Orçamentos — PDF", description = "Geração e envio por e-mail do PDF do orçamento no padrão Garagem52")
@SecurityRequirement(name = "bearerAuth")
public interface OrcamentoPdfControllerSwagger {

    @Operation(
            summary = "Gerar PDF do orçamento",
            description = """
                    Gera e retorna o PDF completo do orçamento em bytes.
                    
                    O documento contém número, data, status, informações do cliente,
                    detalhes do serviço, tabela de peças/materiais e resumo financeiro.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar o PDF")
    })
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> gerarPdf(
            @Parameter(description = "ID do orçamento", required = true) @PathVariable Long id);

    @Operation(
            summary = "Enviar PDF do orçamento por e-mail",
            description = """
                    Gera o PDF do orçamento e o envia como anexo para o e-mail do cliente.
                    
                    **Requisito:** o campo `telefoneCliente` do orçamento deve conter um endereço
                    de e-mail válido (com '@') para que o envio seja realizado.
                    
                    O e-mail enviado contém:
                    - Layout com identidade visual Garagem52
                    - Resumo do orçamento (número, data, serviço, veículo, total)
                    - PDF completo em anexo
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "E-mail enviado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar ou enviar o e-mail")
    })
    @PostMapping("/{id}/pdf/email")
    ResponseEntity<MessageResponse> enviarPdfPorEmail(
            @Parameter(description = "ID do orçamento", required = true) @PathVariable Long id);
}
