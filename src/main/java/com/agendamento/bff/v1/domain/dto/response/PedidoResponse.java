package com.agendamento.bff.v1.domain.dto.response;

public record PedidoResponse(
        Long id,
        String filial,
        String pedido,
        String fornecedor,
        String comprador,
        String dataCriacao,
        String status
) {
}
