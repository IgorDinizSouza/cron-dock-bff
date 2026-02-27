package com.agendamento.bff.v1.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PerfilRequest(
        String descricao,
        @JsonAlias({"roles", "roleIds"})
        List<Long> roleIds
) {
}
