package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.TransportadorRequest;
import com.agendamento.bff.v1.domain.dto.response.TransportadorResponse;
import com.agendamento.bff.v1.domain.model.Transportador;

public class TransportadorMapper {

    private TransportadorMapper() {
    }

    public static Transportador toEntity(TransportadorRequest req) {
        return Transportador.builder()
                .descricao(req.descricao())
                .cnpj(req.cnpj())
                .status(req.status())
                .build();
    }

    public static TransportadorResponse toResponse(Transportador entity) {
        return new TransportadorResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getCnpj(),
                entity.getStatus(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null
        );
    }
}
