package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.EmbalagemRequest;
import com.agendamento.bff.v1.domain.dto.response.EmbalagemResponse;
import com.agendamento.bff.v1.domain.model.Embalagem;

public class EmbalagemMapper {

    private EmbalagemMapper() {
    }

    public static Embalagem toEntity(EmbalagemRequest req) {
        return Embalagem.builder()
                .id(req.id())
                .digito(req.digito())
                .codigoBarra(req.codigoBarra())
                .sigla(req.sigla())
                .multiplicador1(req.multiplicador1())
                .multiplicador2(req.multiplicador2())
                .status(req.status())
                .build();
    }

    public static EmbalagemResponse toResponse(Embalagem entity) {
        return new EmbalagemResponse(
                entity.getId(),
                entity.getProduto() != null ? entity.getProduto().getId() : null,
                entity.getDigito(),
                entity.getCodigoBarra(),
                entity.getSigla(),
                entity.getMultiplicador1(),
                entity.getMultiplicador2(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null
        );
    }
}
