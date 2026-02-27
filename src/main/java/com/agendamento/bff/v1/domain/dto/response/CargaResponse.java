package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record CargaResponse(
        Long id,
        Long idStatusCarga,
        String statusCargaDescricao,
        Long idTipoCarga,
        String tipoCargaDescricao,
        Long idTipoVeiculo,
        String tipoVeiculoNome,
        Long idTransportadora,
        String transportadoraDescricao,
        Long idEspecieCarga,
        String especieCargaDescricao,
        Long idUsuarioSolicitante,
        String usuarioSolicitanteDescricao,
        Long idUsuarioAprovador,
        String usuarioAprovadorDescricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAgendamento
) {
}
