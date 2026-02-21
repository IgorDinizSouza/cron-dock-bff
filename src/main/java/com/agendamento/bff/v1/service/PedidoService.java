package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.dto.request.PedidoBuscaRequest;
import com.agendamento.bff.v1.domain.dto.response.PedidoBuscaResponse;
import com.agendamento.bff.v1.domain.mapper.PedidoBuscaMapper;
import com.agendamento.bff.v1.domain.model.Pedido;
import com.agendamento.bff.v1.domain.model.PedidoItem;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.PedidoItemRepository;
import com.agendamento.bff.v1.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final PedidoItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<Pedido> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        return repository.buscarPedidosPorGrupoEmpresarial(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public PedidoBuscaResponse buscarPedido(PedidoBuscaRequest request) {
        if (request == null
                || request.grupoEmpresarialId() == null
                || request.numeroPedido() == null
                || request.numeroPedido().isBlank()) {
            throw new IllegalArgumentException("Grupo empresarial e numero do pedido sao obrigatorios.");
        }

        Pedido pedido = repository.buscarPedidoPorGrupoEmpresarialENumeroPedido(
                        request.grupoEmpresarialId(),
                        request.numeroPedido()
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido nao encontrado. grupoEmpresarialId=" + request.grupoEmpresarialId()
                                + ", numeroPedido=" + request.numeroPedido()
                ));

        List<PedidoItem> itens = itemRepository.buscarItensPorPedidoId(pedido.getId());

        return PedidoBuscaMapper.toResponse(pedido, itens);
    }
}
