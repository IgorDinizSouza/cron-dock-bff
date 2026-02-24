package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.GrupoTransportadoraRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraComTransportadorasResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraTransportadoraDetalheResponse;
import com.agendamento.bff.v1.domain.model.GrupoTransportadora;
import com.agendamento.bff.v1.domain.model.GrupoTransportadoraTransportadora;

import java.util.List;

public class GrupoTransportadoraMapper {

    private GrupoTransportadoraMapper() {
    }

    public static GrupoTransportadora toEntity(GrupoTransportadoraRequest req) {
        return GrupoTransportadora.builder()
                .descricao(req.descricao())
                .status(req.status())
                .build();
    }

    public static GrupoTransportadoraResponse toResponse(GrupoTransportadora entity) {
        return new GrupoTransportadoraResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus()
        );
    }

    public static GrupoTransportadoraComTransportadorasResponse toComTransportadorasResponse(GrupoTransportadora entity) {
        List<GrupoTransportadoraTransportadoraDetalheResponse> lista = entity.getTransportadoras() == null
                ? List.of()
                : entity.getTransportadoras().stream().map(GrupoTransportadoraMapper::toDetalheResponse).toList();

        return new GrupoTransportadoraComTransportadorasResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                lista
        );
    }

    private static GrupoTransportadoraTransportadoraDetalheResponse toDetalheResponse(GrupoTransportadoraTransportadora entity) {
        return new GrupoTransportadoraTransportadoraDetalheResponse(
                entity.getId(),
                entity.getDataCriacao(),
                entity.getTransportadora() != null ? TransportadorMapper.toResponse(entity.getTransportadora()) : null
        );
    }
}
