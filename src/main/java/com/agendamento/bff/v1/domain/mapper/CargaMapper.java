package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.CargaRequest;
import com.agendamento.bff.v1.domain.dto.response.CargaResponse;
import com.agendamento.bff.v1.domain.model.Carga;

public class CargaMapper {

    private CargaMapper() {
    }

    public static Carga toEntity(CargaRequest req) {
        return Carga.builder()
                .dataAgendamento(req.dataAgendamento())
                .build();
    }

    public static CargaResponse toResponse(Carga entity) {
        return new CargaResponse(
                entity.getId(),
                entity.getStatusCarga() != null ? entity.getStatusCarga().getId() : null,
                entity.getStatusCarga() != null ? entity.getStatusCarga().getDescricao() : null,
                entity.getTipoCarga() != null ? entity.getTipoCarga().getId() : null,
                entity.getTipoCarga() != null ? entity.getTipoCarga().getDescricao() : null,
                entity.getTipoVeiculo() != null ? entity.getTipoVeiculo().getId() : null,
                entity.getTipoVeiculo() != null ? entity.getTipoVeiculo().getNome() : null,
                entity.getTransportador() != null ? entity.getTransportador().getId() : null,
                entity.getTransportador() != null ? entity.getTransportador().getDescricao() : null,
                entity.getEspecieCarga() != null ? entity.getEspecieCarga().getId() : null,
                entity.getEspecieCarga() != null ? entity.getEspecieCarga().getDescricao() : null,
                entity.getUsuarioSolicitante() != null ? entity.getUsuarioSolicitante().getId() : null,
                entity.getUsuarioSolicitante() != null ? entity.getUsuarioSolicitante().getDescricao() : null,
                entity.getUsuarioAprovador() != null ? entity.getUsuarioAprovador().getId() : null,
                entity.getUsuarioAprovador() != null ? entity.getUsuarioAprovador().getDescricao() : null,
                entity.getDataCriacao(),
                entity.getDataAgendamento()
        );
    }
}
