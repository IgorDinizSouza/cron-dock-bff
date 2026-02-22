package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.TipoVeiculoRequest;
import com.agendamento.bff.v1.domain.dto.response.TipoVeiculoResponse;
import com.agendamento.bff.v1.domain.model.TipoVeiculo;

public class TipoVeiculoMapper {

    private TipoVeiculoMapper() {
    }

    public static TipoVeiculo toEntity(TipoVeiculoRequest req) {
        return TipoVeiculo.builder()
                .nome(req.nome())
                .quantidadeMaximaPaletes(req.quantidadeMaximaPaletes())
                .status(req.status())
                .build();
    }

    public static TipoVeiculoResponse toResponse(TipoVeiculo entity) {
        return new TipoVeiculoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getQuantidadeMaximaPaletes(),
                entity.getStatus()
        );
    }
}
