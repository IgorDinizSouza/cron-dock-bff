package com.agendamento.bff.v1.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CargaProdutoResponse(
        Long id,
        Long idCarga,
        Long idProduto,
        String produtoDescricao,
        Long idPedido,
        Long numeroPedido,
        BigDecimal quantidadeAlocada,
        LocalDateTime data
) {
}
