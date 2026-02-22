package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.FilialRequest;
import com.agendamento.bff.v1.domain.dto.response.FilialResponse;
import com.agendamento.bff.v1.domain.model.Filial;

public class FilialMapper {

    private FilialMapper() {
    }

    public static Filial toEntity(FilialRequest req) {
        return Filial.builder()
                .id(req.id())
                .descricao(req.descricao())
                .cnpj(req.cnpj())
                .endereco(req.endereco())
                .bairro(req.bairro())
                .codigoIbgeCidade(req.codigoIbgeCidade())
                .uf(req.uf())
                .cep(req.cep())
                .cd(req.cd())
                .wms(req.wms())
                .flagRegional(req.flagRegional())
                .descricaoRegional(req.descricaoRegional())
                .status(req.status())
                .build();
    }

    public static FilialResponse toResponse(Filial entity) {
        return new FilialResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getCnpj(),
                entity.getEndereco(),
                entity.getBairro(),
                entity.getCodigoIbgeCidade(),
                entity.getUf(),
                entity.getCep(),
                entity.getCd(),
                entity.getWms(),
                entity.getFlagRegional(),
                entity.getDescricaoRegional(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null
        );
    }
}
