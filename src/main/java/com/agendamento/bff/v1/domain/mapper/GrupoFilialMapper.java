package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.GrupoFilialRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialComFiliaisResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialFilialDetalheResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialResponse;
import com.agendamento.bff.v1.domain.model.GrupoFilial;
import com.agendamento.bff.v1.domain.model.GrupoFilialFilial;

import java.util.List;

public class GrupoFilialMapper {

    private GrupoFilialMapper() {
    }

    public static GrupoFilial toEntity(GrupoFilialRequest req) {
        return GrupoFilial.builder()
                .descricao(req.descricao())
                .status(req.status())
                .build();
    }

    public static GrupoFilialResponse toResponse(GrupoFilial entity) {
        return new GrupoFilialResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus()
        );
    }

    public static GrupoFilialComFiliaisResponse toComFiliaisResponse(GrupoFilial entity) {
        List<GrupoFilialFilialDetalheResponse> lista = entity.getFiliais() == null
                ? List.of()
                : entity.getFiliais().stream().map(GrupoFilialMapper::toDetalheResponse).toList();

        return new GrupoFilialComFiliaisResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                lista
        );
    }

    private static GrupoFilialFilialDetalheResponse toDetalheResponse(GrupoFilialFilial entity) {
        return new GrupoFilialFilialDetalheResponse(
                entity.getId(),
                entity.getData(),
                entity.getFilial() != null ? FilialMapper.toResponse(entity.getFilial()) : null
        );
    }
}
