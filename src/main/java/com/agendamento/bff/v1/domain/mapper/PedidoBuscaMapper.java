package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.PedidoBuscaResponse;
import com.agendamento.bff.v1.domain.dto.response.PedidoItemResponse;
import com.agendamento.bff.v1.domain.model.Comprador;
import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.domain.model.Fornecedor;
import com.agendamento.bff.v1.domain.model.Pedido;
import com.agendamento.bff.v1.domain.model.PedidoItem;
import com.agendamento.bff.v1.domain.model.Produto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class PedidoBuscaMapper {

    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private PedidoBuscaMapper() {}

    public static PedidoBuscaResponse toResponse(Pedido pedido, List<PedidoItem> itens) {
        return new PedidoBuscaResponse(
                pedido.getId(),
                formatPedido(pedido.getIdPedido()),
                getId(pedido.getFilial()),
                getDescricao(pedido.getFilial()),
                getId(pedido.getFornecedor()),
                getDescricao(pedido.getFornecedor()),
                getId(pedido.getComprador()),
                getDescricao(pedido.getComprador()),
                formatData(pedido.getDataCriacao()),
                mapStatus(pedido.getStatus()),
                itens.stream().map(PedidoBuscaMapper::toItemResponse).toList()
        );
    }

    private static PedidoItemResponse toItemResponse(PedidoItem item) {
        return new PedidoItemResponse(
                item.getId(),
                item.getSequencia(),
                getId(item.getProduto()),
                getDescricao(item.getProduto()),
                item.getQtdPedida(),
                item.getQtdRecebida(),
                formatData(item.getDataEntrega()),
                item.getCargaPalet(),
                item.getAbc(),
                item.getParticipacaoItem()
        );
    }

    private static String formatPedido(Long idPedido) {
        if (idPedido == null) {
            return null;
        }
        return idPedido.toString();
    }

    private static String formatData(LocalDateTime dataCriacao) {
        if (dataCriacao == null) {
            return null;
        }
        return dataCriacao.toLocalDate().format(DATA_FORMAT);
    }

    private static String formatData(LocalDate dataEntrega) {
        if (dataEntrega == null) {
            return null;
        }
        return dataEntrega.format(DATA_FORMAT);
    }

    private static Long getId(Filial filial) {
        return filial != null ? filial.getId() : null;
    }

    private static String getDescricao(Filial filial) {
        return filial != null ? filial.getDescricao() : null;
    }

    private static Long getId(Fornecedor fornecedor) {
        return fornecedor != null ? fornecedor.getId() : null;
    }

    private static String getDescricao(Fornecedor fornecedor) {
        return fornecedor != null ? fornecedor.getRazaoSocial() : null;
    }

    private static Long getId(Comprador comprador) {
        return comprador != null ? comprador.getId() : null;
    }

    private static String getDescricao(Comprador comprador) {
        return comprador != null ? comprador.getDescricao() : null;
    }

    private static Long getId(Produto produto) {
        return produto != null ? produto.getId() : null;
    }

    private static String getDescricao(Produto produto) {
        return produto != null ? produto.getDescricao() : null;
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
