package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PerfilResponse(
        Long id,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao,
        List<RoleResponse> roles
) {
}
