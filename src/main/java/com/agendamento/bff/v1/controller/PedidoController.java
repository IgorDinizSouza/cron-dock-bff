package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.PedidoBuscaRequest;
import com.agendamento.bff.v1.domain.dto.response.PedidoBuscaResponse;
import com.agendamento.bff.v1.domain.dto.response.PedidoResponse;
import com.agendamento.bff.v1.domain.mapper.PedidoMapper;
import com.agendamento.bff.v1.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<PedidoResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<PedidoResponse> pedidos = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(PedidoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PedidoBuscaResponse> buscarPedido(
            @RequestParam Long grupoEmpresarialId,
            @RequestParam String numeroPedido
    ) {
        PedidoBuscaRequest request = new PedidoBuscaRequest(grupoEmpresarialId, numeroPedido);
        return ResponseEntity.ok(service.buscarPedido(request));
    }
}
