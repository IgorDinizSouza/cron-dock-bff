package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FornecedorResponse(
        Long id,
        String cnpj,
        String razaoSocial,
        String cidade,
        String uf,
        LocalDate dataCadastro,
        Status status,
        LocalDateTime dataCriacao,
        Long grupoEmpresarialId
) {
}
