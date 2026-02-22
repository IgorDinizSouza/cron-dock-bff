package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        Long compradorId,
        String compradorDescricao,
        String descricao,
        String complemento,
        Integer lastro,
        BigDecimal altura,
        String peso,
        String pesoLiquido,
        Long composicao,
        LocalDate dataCadastro,
        Status status,
        LocalDateTime dataCriacao,
        Long grupoEmpresarialId,
        java.util.List<EmbalagemResponse> embalagens
) {
}
