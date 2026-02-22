package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDateTime;

public record FilialResponse(
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
        Status status,
        LocalDateTime dataCriacao,
        Long grupoEmpresarialId
) {
}
