package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.CompradorRequest;
import com.agendamento.bff.v1.domain.dto.response.CompradorResponse;
import com.agendamento.bff.v1.domain.model.Comprador;

public class CompradorMapper {

    private CompradorMapper() {
    }

    public static Comprador toEntity(CompradorRequest req) {
        return Comprador.builder()
                .id(req.id())
                .descricao(req.descricao())
                .status(req.status())
                .build();
    }

    public static CompradorResponse toResponse(Comprador entity) {
        return new CompradorResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null
        );
    }
}
