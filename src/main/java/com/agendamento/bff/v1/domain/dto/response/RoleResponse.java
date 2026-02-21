package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record RoleResponse(
        Long id,
        String nome,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
) {
}
