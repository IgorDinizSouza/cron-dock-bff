package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorFornecedorResponse;
import com.agendamento.bff.v1.domain.model.GrupoFornecedorFornecedor;

public class GrupoFornecedorFornecedorMapper {

    private GrupoFornecedorFornecedorMapper() {
    }

    public static GrupoFornecedorFornecedorResponse toResponse(GrupoFornecedorFornecedor entity) {
        return new GrupoFornecedorFornecedorResponse(
                entity.getId(),
                entity.getGrupoFornecedor() != null ? entity.getGrupoFornecedor().getId() : null,
                entity.getFornecedor() != null ? entity.getFornecedor().getId() : null,
                entity.getData()
        );
    }
}
