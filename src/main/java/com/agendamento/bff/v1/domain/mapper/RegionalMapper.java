package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.RegionalRequest;
import com.agendamento.bff.v1.domain.dto.response.RegionalComFiliaisResponse;
import com.agendamento.bff.v1.domain.dto.response.RegionalFilialDetalheResponse;
import com.agendamento.bff.v1.domain.dto.response.RegionalResponse;
import com.agendamento.bff.v1.domain.model.Regional;
import com.agendamento.bff.v1.domain.model.RegionalFilial;

import java.util.List;

public class RegionalMapper {

    private RegionalMapper() {
    }

    public static Regional toEntity(RegionalRequest req) {
        return Regional.builder()
                .descricao(req.descricao())
                .status(req.status())
                .build();
    }

    public static RegionalResponse toResponse(Regional entity) {
        return new RegionalResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataCriacao()
        );
    }

    public static RegionalComFiliaisResponse toComFiliaisResponse(Regional entity) {
        List<RegionalFilialDetalheResponse> filiais = entity.getRegionaisFiliais() == null
                ? List.of()
                : entity.getRegionaisFiliais().stream()
                .map(RegionalMapper::toRegionalFilialDetalheResponse)
                .toList();

        return new RegionalComFiliaisResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataCriacao(),
                filiais
        );
    }

    private static RegionalFilialDetalheResponse toRegionalFilialDetalheResponse(RegionalFilial entity) {
        return new RegionalFilialDetalheResponse(
                entity.getId(),
                entity.getDataCriacao(),
                entity.getFilial() != null ? FilialMapper.toResponse(entity.getFilial()) : null
        );
    }
}
