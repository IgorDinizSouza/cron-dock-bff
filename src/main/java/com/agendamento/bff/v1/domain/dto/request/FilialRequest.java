package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record FilialRequest(
        Long id,
        String descricao,
        String cnpj,
        String endereco,
        String bairro,
        String codigoIbgeCidade,
        String uf,
        String cep,
        Integer cd,
        Integer wms,
        Integer flagRegional,
        String descricaoRegional,
        Long grupoEmpresarialId,
        Status status
) {
}
