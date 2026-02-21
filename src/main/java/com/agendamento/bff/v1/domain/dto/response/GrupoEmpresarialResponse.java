package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDateTime;

public record GrupoEmpresarialResponse(
        Long id,
        String descricao,
        String cnpj,
        Status status,
        LocalDateTime dataCriacao
) {}