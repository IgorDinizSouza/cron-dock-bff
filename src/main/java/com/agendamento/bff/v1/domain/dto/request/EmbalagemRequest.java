package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.math.BigDecimal;

public record EmbalagemRequest(
        Long id,
        Integer digito,
        String codigoBarra,
        String sigla,
        BigDecimal multiplicador1,
        BigDecimal multiplicador2,
        Status status
) {
}
