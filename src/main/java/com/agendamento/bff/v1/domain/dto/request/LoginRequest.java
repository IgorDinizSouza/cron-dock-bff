package com.agendamento.bff.v1.domain.dto.request;

public record LoginRequest(
        String usuario,
        String senha
) {
}
