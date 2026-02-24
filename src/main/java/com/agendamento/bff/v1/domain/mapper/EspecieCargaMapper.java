package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.EspecieCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.EspecieCargaResponse;
import com.agendamento.bff.v1.domain.model.EspecieCarga;

public class EspecieCargaMapper {

    private EspecieCargaMapper() {
    }

    public static EspecieCarga toEntity(EspecieCargaRequest req) {
        return EspecieCarga.builder()
                .descricao(req.descricao())
                .ativo(req.ativo())
                .build();
    }

    public static EspecieCargaResponse toResponse(EspecieCarga entity) {
        return new EspecieCargaResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getAtivo(),
                entity.getDataCriacao(),
                entity.getDataAlteracao()
        );
    }
}
