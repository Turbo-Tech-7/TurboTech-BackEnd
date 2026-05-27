package com.garagem52.adapter.input.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioPecasDTO(
        BigDecimal valorGastoComPecas,
        long totalFornecedores,
        String principalFornecedor,
        List<FornecedorItemDTO> fornecedoresMaisUtilizados,
        List<EvolucaoMensalDTO> evolucaoGastoComPecas
) {
    public record FornecedorItemDTO(String fornecedor, long quantidade) {}
    public record EvolucaoMensalDTO(String mes, BigDecimal total) {}
}
