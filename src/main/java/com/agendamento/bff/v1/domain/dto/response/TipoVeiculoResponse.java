package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record TipoVeiculoResponse(
        Long id,
        String nome,
        Integer quantidadeMaximaPaletes,
        Status status
) {
}
