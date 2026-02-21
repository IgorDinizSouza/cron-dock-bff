package com.agendamento.bff.v1.domain.dto.request;

public record PedidoBuscaRequest(
        Long grupoEmpresarialId,
        String numeroPedido
) {
}
