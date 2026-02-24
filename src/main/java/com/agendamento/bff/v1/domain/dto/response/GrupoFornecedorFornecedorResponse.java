package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoFornecedorFornecedorResponse(
        Long id,
        Long grupoFornecedorId,
        Long fornecedorId,
        LocalDateTime data
) {
}
