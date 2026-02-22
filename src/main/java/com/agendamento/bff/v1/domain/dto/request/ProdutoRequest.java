package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProdutoRequest(
        Long id,
        Long compradorId,
        String descricao,
        String complemento,
        Integer lastro,
        BigDecimal altura,
        String peso,
        String pesoLiquido,
        Long composicao,
        LocalDate dataCadastro,
        Long grupoEmpresarialId,
        Status status,
        List<EmbalagemRequest> embalagens
) {
}
