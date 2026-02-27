package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.CargaProdutoRequest;
import com.agendamento.bff.v1.domain.dto.response.CargaProdutoResponse;
import com.agendamento.bff.v1.domain.model.CargaProduto;

public class CargaProdutoMapper {

    private CargaProdutoMapper() {
    }

    public static CargaProduto toEntity(CargaProdutoRequest req) {
        return CargaProduto.builder()
                .quantidadeAlocada(req.quantidadeAlocada())
                .data(req.data())
                .build();
    }

    public static CargaProdutoResponse toResponse(CargaProduto entity) {
        return new CargaProdutoResponse(
                entity.getId(),
                entity.getCarga() != null ? entity.getCarga().getId() : null,
                entity.getProduto() != null ? entity.getProduto().getId() : null,
                entity.getProduto() != null ? entity.getProduto().getDescricao() : null,
                entity.getPedido() != null ? entity.getPedido().getId() : null,
                entity.getPedido() != null ? entity.getPedido().getIdPedido() : null,
                entity.getQuantidadeAlocada(),
                entity.getData()
        );
    }
}
