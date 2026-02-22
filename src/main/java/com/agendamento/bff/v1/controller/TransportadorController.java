package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.TransportadorRequest;
import com.agendamento.bff.v1.domain.dto.response.TransportadorResponse;
import com.agendamento.bff.v1.domain.mapper.TransportadorMapper;
import com.agendamento.bff.v1.domain.model.Transportador;
import com.agendamento.bff.v1.service.TransportadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transportador")
@RequiredArgsConstructor
public class TransportadorController {

    private final TransportadorService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<TransportadorResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<TransportadorResponse> resp = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(TransportadorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{transportadorId}")
    public ResponseEntity<TransportadorResponse> buscarPorGrupoEmpresarialETransportadorId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long transportadorId
    ) {
        Transportador transportador = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, transportadorId);
        return ResponseEntity.ok(TransportadorMapper.toResponse(transportador));
    }

    @PostMapping
    public ResponseEntity<TransportadorResponse> criar(@RequestBody TransportadorRequest req) {
        Transportador entity = TransportadorMapper.toEntity(req);
        Transportador salvo = service.criar(entity, req.grupoEmpresarialId());

        return ResponseEntity
                .created(URI.create("/api/v1/transportador/" + salvo.getId()))
                .body(TransportadorMapper.toResponse(salvo));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<TransportadorResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody TransportadorRequest req
    ) {
        Transportador dados = TransportadorMapper.toEntity(req);
        Transportador atualizado = service.atualizar(id, dados, grupoEmpresarialId);
        return ResponseEntity.ok(TransportadorMapper.toResponse(atualizado));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
