package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.GrupoEmpresarialRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoEmpresarialResponse;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;

public final class GrupoEmpresarialMapper {

    private GrupoEmpresarialMapper() {}

    public static GrupoEmpresarial toEntity(GrupoEmpresarialRequest req) {
        return GrupoEmpresarial.builder()
                .descricao(req.descricao())
                .cnpj(req.cnpj())
                .status(req.status())
                .build();
    }

    public static GrupoEmpresarialResponse toResponse(GrupoEmpresarial e) {
        return new GrupoEmpresarialResponse(
                e.getId(),
                e.getDescricao(),
                e.getCnpj(),
                e.getStatus(),
                e.getDataCriacao()
        );
    }
}
