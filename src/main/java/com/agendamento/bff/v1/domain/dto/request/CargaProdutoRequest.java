package com.agendamento.bff.v1.domain.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CargaProdutoRequest(
        Long idProduto,
        Long idPedido,
        BigDecimal quantidadeAlocada,
        LocalDateTime data
) {
}
