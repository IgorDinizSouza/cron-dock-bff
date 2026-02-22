package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmbalagemResponse(
        Long id,
        Long produtoId,
        Integer digito,
        String codigoBarra,
        String sigla,
        BigDecimal multiplicador1,
        BigDecimal multiplicador2,
        Status status,
        LocalDateTime dataCriacao,
        Long grupoEmpresarialId
) {
}
