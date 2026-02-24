package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDateTime;
import java.util.List;

public record GrupoFornecedorComFornecedoresResponse(
        Long id,
        String descricao,
        Status status,
        LocalDateTime dataCriacao,
        List<GrupoFornecedorFornecedorDetalheResponse> fornecedores
) {
}
