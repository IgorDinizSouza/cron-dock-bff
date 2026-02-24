package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.GrupoTransportadoraRequest;
import com.agendamento.bff.v1.domain.dto.request.GrupoTransportadoraTransportadoraRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraComTransportadorasResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraTransportadoraResponse;
import com.agendamento.bff.v1.domain.mapper.GrupoTransportadoraMapper;
import com.agendamento.bff.v1.domain.mapper.GrupoTransportadoraTransportadoraMapper;
import com.agendamento.bff.v1.domain.model.GrupoTransportadora;
import com.agendamento.bff.v1.domain.model.GrupoTransportadoraTransportadora;
import com.agendamento.bff.v1.service.GrupoTransportadoraService;
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
@RequestMapping("/api/v1/grupo-transportadora")
@RequiredArgsConstructor
public class GrupoTransportadoraController {

    private final GrupoTransportadoraService service;

    @GetMapping
    public ResponseEntity<List<GrupoTransportadoraResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(GrupoTransportadoraMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoTransportadoraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoTransportadoraMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<GrupoTransportadoraResponse> criar(@RequestBody GrupoTransportadoraRequest req) {
        GrupoTransportadora salvo = service.criar(GrupoTransportadoraMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/grupo-transportadora/" + salvo.getId()))
                .body(GrupoTransportadoraMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoTransportadoraResponse> atualizar(@PathVariable Long id, @RequestBody GrupoTransportadoraRequest req) {
        GrupoTransportadora atualizado = service.atualizar(id, GrupoTransportadoraMapper.toEntity(req));
        return ResponseEntity.ok(GrupoTransportadoraMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{grupoTransportadoraId}/transportadora")
    public ResponseEntity<GrupoTransportadoraTransportadoraResponse> criarVinculoTransportadora(
            @PathVariable Long grupoTransportadoraId,
            @RequestBody GrupoTransportadoraTransportadoraRequest req
    ) {
        GrupoTransportadoraTransportadora salvo = service.criarVinculo(grupoTransportadoraId, req.transportadoraId());
        return ResponseEntity
                .created(URI.create("/api/v1/grupo-transportadora/" + grupoTransportadoraId + "/transportadora/" + salvo.getId()))
                .body(GrupoTransportadoraTransportadoraMapper.toResponse(salvo));
    }

    @GetMapping("/com-transportadoras")
    public ResponseEntity<List<GrupoTransportadoraComTransportadorasResponse>> listarComTransportadoras() {
        return ResponseEntity.ok(service.listarComTransportadoras().stream()
                .map(GrupoTransportadoraMapper::toComTransportadorasResponse)
                .toList());
    }

    @GetMapping("/{id}/com-transportadoras")
    public ResponseEntity<GrupoTransportadoraComTransportadorasResponse> buscarPorIdComTransportadoras(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoTransportadoraMapper.toComTransportadorasResponse(service.buscarPorIdComTransportadoras(id)));
    }
}
