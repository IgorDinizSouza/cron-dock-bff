package com.agendamento.bff.v1.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record PerfilRequest(
        String descricao,
        @JsonAlias({"roles", "roleIds"})
        List<Long> roleIds
) {
}
