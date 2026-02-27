package com.agendamento.bff.v1.domain.dto.request;

import java.time.LocalDateTime;

public record CargaRequest(
        Long idStatusCarga,
        Long idTipoCarga,
        Long idTipoVeiculo,
        Long idTransportadora,
        Long idEspecieCarga,
        Long idUsuarioSolicitante,
        Long idUsuarioAprovador,
        LocalDateTime dataAgendamento
) {
}
