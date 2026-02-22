package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.FornecedorRequest;
import com.agendamento.bff.v1.domain.dto.response.FornecedorResponse;
import com.agendamento.bff.v1.domain.model.Fornecedor;

public class FornecedorMapper {

    private FornecedorMapper() {
    }

    public static Fornecedor toEntity(FornecedorRequest req) {
        return Fornecedor.builder()
                .cnpj(req.cnpj())
                .razaoSocial(req.razaoSocial())
                .cidade(req.cidade())
                .uf(req.uf())
                .dataCadastro(req.dataCadastro())
                .status(req.status())
                .build();
    }

    public static FornecedorResponse toResponse(Fornecedor entity) {
        return new FornecedorResponse(
                entity.getId(),
                entity.getCnpj(),
                entity.getRazaoSocial(),
                entity.getCidade(),
                entity.getUf(),
                entity.getDataCadastro(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null
        );
    }
}
