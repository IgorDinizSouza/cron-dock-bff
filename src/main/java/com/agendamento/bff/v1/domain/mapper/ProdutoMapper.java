package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.ProdutoRequest;
import com.agendamento.bff.v1.domain.dto.response.EmbalagemResponse;
import com.agendamento.bff.v1.domain.dto.response.ProdutoResponse;
import com.agendamento.bff.v1.domain.model.Produto;

import java.util.List;

public class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static Produto toEntity(ProdutoRequest req) {
        return Produto.builder()
                .id(req.id())
                .descricao(req.descricao())
                .complemento(req.complemento())
                .lastro(req.lastro())
                .altura(req.altura())
                .peso(req.peso())
                .pesoLiquido(req.pesoLiquido())
                .composicao(req.composicao())
                .dataCadastro(req.dataCadastro())
                .status(req.status())
                .build();
    }

    public static ProdutoResponse toResponse(Produto entity) {
        return toResponse(entity, List.of());
    }

    public static ProdutoResponse toResponse(Produto entity, List<EmbalagemResponse> embalagens) {
        return new ProdutoResponse(
                entity.getId(),
                entity.getComprador() != null ? entity.getComprador().getId() : null,
                entity.getComprador() != null ? entity.getComprador().getDescricao() : null,
                entity.getDescricao(),
                entity.getComplemento(),
                entity.getLastro(),
                entity.getAltura(),
                entity.getPeso(),
                entity.getPesoLiquido(),
                entity.getComposicao(),
                entity.getDataCadastro(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null,
                embalagens
        );
    }
}
