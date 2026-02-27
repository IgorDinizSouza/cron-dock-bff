package com.agendamento.bff.v1.domain.dto.request;

public record UsuarioAprovacaoRequest(
        Long idUsuarioAprovador,
        Long idStatusAprovacaoUsuario,
        String motivoRecusa
) {
}
