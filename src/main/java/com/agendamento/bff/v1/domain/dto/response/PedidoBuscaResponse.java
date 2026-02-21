package com.agendamento.bff.v1.domain.dto.response;

import java.util.List;

public record PedidoBuscaResponse(
        Long id,
        String pedido,
        Long filialId,
        String filialDescricao,
        Long fornecedorId,
        String fornecedorDescricao,
        Long compradorId,
        String compradorDescricao,
        String dataCriacao,
        String status,
        List<PedidoItemResponse> itens
) {
}
