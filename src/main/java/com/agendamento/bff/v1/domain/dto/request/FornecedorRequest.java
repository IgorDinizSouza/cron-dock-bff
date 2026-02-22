package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDate;

public record FornecedorRequest(
        String cnpj,
        String razaoSocial,
        String cidade,
        String uf,
        LocalDate dataCadastro,
        Long grupoEmpresarialId,
        Status status
) {
}
