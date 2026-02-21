package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.PedidoResponse;
import com.agendamento.bff.v1.domain.model.Comprador;
import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.domain.model.Fornecedor;
import com.agendamento.bff.v1.domain.model.Pedido;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class PedidoMapper {

    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private PedidoMapper() {}

    public static PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                formatIdDescricao(pedido.getFilial()),
                formatPedido(pedido.getIdPedido()),
                formatIdDescricao(pedido.getFornecedor()),
                formatIdDescricao(pedido.getComprador()),
                formatData(pedido.getDataCriacao()),
                mapStatus(pedido.getStatus())
        );
    }

    private static String formatPedido(Long idPedido) {
        if (idPedido == null) {
            return null;
        }
        return idPedido.toString();
    }

    private static String formatIdDescricao(Filial filial) {
        if (filial == null) {
            return null;
        }
        return filial.getId() + " - " + filial.getDescricao();
    }

    private static String formatIdDescricao(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }
        return fornecedor.getId() + " - " + fornecedor.getRazaoSocial();
    }

    private static String formatIdDescricao(Comprador comprador) {
        if (comprador == null) {
            return null;
        }
        return comprador.getId() + " - " + comprador.getDescricao();
    }

    private static String formatData(LocalDateTime dataCriacao) {
        if (dataCriacao == null) {
            return null;
        }
        return dataCriacao.toLocalDate().format(DATA_FORMAT);
    }

    private static String mapStatus(Integer status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case 0 -> "Pendente";
            case 1 -> "Enviado";
            case 2 -> "Recebido";
            default -> "Desconhecido";
        };
    }
}
