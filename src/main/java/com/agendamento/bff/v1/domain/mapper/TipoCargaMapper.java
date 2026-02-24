package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.TipoCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.TipoCargaResponse;
import com.agendamento.bff.v1.domain.model.TipoCarga;

public class TipoCargaMapper {

    private TipoCargaMapper() {
    }

    public static TipoCarga toEntity(TipoCargaRequest req) {
        return TipoCarga.builder()
                .descricao(req.descricao())
                .minSku(req.minSku())
                .maxSku(req.maxSku())
                .build();
    }

    public static TipoCargaResponse toResponse(TipoCarga entity) {
        return new TipoCargaResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getMinSku(),
                entity.getMaxSku()
        );
    }
}
