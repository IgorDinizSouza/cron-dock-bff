package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoFornecedorFornecedorDetalheResponse(
        Long id,
        LocalDateTime data,
        FornecedorResponse fornecedor
) {
}
