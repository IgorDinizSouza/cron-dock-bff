package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record UsuarioAprovacaoResponse(
        Long id,
        Long usuarioSolicitanteId,
        Long usuarioAprovadorId,
        Long statusAprovacaoUsuarioId,
        String statusAprovacaoDescricao,
        String motivoRecusa,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataAprovacao,
        String mensagem
) {
}
