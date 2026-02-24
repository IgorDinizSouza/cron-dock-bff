package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.GrupoFornecedorRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorComFornecedoresResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorFornecedorDetalheResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorResponse;
import com.agendamento.bff.v1.domain.model.GrupoFornecedor;
import com.agendamento.bff.v1.domain.model.GrupoFornecedorFornecedor;

import java.util.List;

public class GrupoFornecedorMapper {

    private GrupoFornecedorMapper() {
    }

    public static GrupoFornecedor toEntity(GrupoFornecedorRequest req) {
        return GrupoFornecedor.builder()
                .descricao(req.descricao())
                .status(req.status())
                .build();
    }

    public static GrupoFornecedorResponse toResponse(GrupoFornecedor entity) {
        return new GrupoFornecedorResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataCriacao()
        );
    }

    public static GrupoFornecedorComFornecedoresResponse toComFornecedoresResponse(GrupoFornecedor entity) {
        List<GrupoFornecedorFornecedorDetalheResponse> lista = entity.getFornecedores() == null
                ? List.of()
                : entity.getFornecedores().stream().map(GrupoFornecedorMapper::toDetalheResponse).toList();

        return new GrupoFornecedorComFornecedoresResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataCriacao(),
                lista
        );
    }

    private static GrupoFornecedorFornecedorDetalheResponse toDetalheResponse(GrupoFornecedorFornecedor entity) {
        return new GrupoFornecedorFornecedorDetalheResponse(
                entity.getId(),
                entity.getData(),
                entity.getFornecedor() != null ? FornecedorMapper.toResponse(entity.getFornecedor()) : null
        );
    }
}
