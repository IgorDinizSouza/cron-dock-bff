package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record TipoVeiculoRequest(
        String nome,
        Integer quantidadeMaximaPaletes,
        Status status
) {
}
