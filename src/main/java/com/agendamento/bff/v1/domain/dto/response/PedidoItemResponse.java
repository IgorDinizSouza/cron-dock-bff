package com.agendamento.bff.v1.domain.dto.response;

import java.math.BigDecimal;

public record PedidoItemResponse(
        Long id,
        Integer sequencia,
        Long produtoId,
        String produtoDescricao,
        BigDecimal qtdPedida,
        BigDecimal qtdRecebida,
        String dataEntrega,
        Integer cargaPalet,
        Integer abc,
        BigDecimal participacaoItem
) {
}
