package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.GrupoFilialFilialRequest;
import com.agendamento.bff.v1.domain.dto.request.GrupoFilialRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialComFiliaisResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialFilialResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFilialResponse;
import com.agendamento.bff.v1.domain.mapper.GrupoFilialFilialMapper;
import com.agendamento.bff.v1.domain.mapper.GrupoFilialMapper;
import com.agendamento.bff.v1.domain.model.GrupoFilial;
import com.agendamento.bff.v1.domain.model.GrupoFilialFilial;
import com.agendamento.bff.v1.service.GrupoFilialService;
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
@RequestMapping("/api/v1/grupo-filial")
@RequiredArgsConstructor
public class GrupoFilialController {

    private final GrupoFilialService service;

    @GetMapping
    public ResponseEntity<List<GrupoFilialResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(GrupoFilialMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoFilialResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoFilialMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<GrupoFilialResponse> criar(@RequestBody GrupoFilialRequest req) {
        GrupoFilial salvo = service.criar(GrupoFilialMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/grupo-filial/" + salvo.getId()))
                .body(GrupoFilialMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoFilialResponse> atualizar(@PathVariable Long id, @RequestBody GrupoFilialRequest req) {
        GrupoFilial atualizado = service.atualizar(id, GrupoFilialMapper.toEntity(req));
        return ResponseEntity.ok(GrupoFilialMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{grupoFilialId}/filial")
    public ResponseEntity<GrupoFilialFilialResponse> criarVinculoFilial(
            @PathVariable Long grupoFilialId,
            @RequestBody GrupoFilialFilialRequest req
    ) {
        GrupoFilialFilial salvo = service.criarVinculo(grupoFilialId, req.filialId());
        return ResponseEntity
                .created(URI.create("/api/v1/grupo-filial/" + grupoFilialId + "/filial/" + salvo.getId()))
                .body(GrupoFilialFilialMapper.toResponse(salvo));
    }

    @DeleteMapping("/{grupoFilialId}/filial/{vinculoId}")
    public ResponseEntity<Void> deletarVinculoFilial(
            @PathVariable Long grupoFilialId,
            @PathVariable Long vinculoId
    ) {
        service.deletarVinculo(grupoFilialId, vinculoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/com-filiais")
    public ResponseEntity<List<GrupoFilialComFiliaisResponse>> listarComFiliais() {
        return ResponseEntity.ok(service.listarComFiliais().stream()
                .map(GrupoFilialMapper::toComFiliaisResponse)
                .toList());
    }

    @GetMapping("/{id}/com-filiais")
    public ResponseEntity<GrupoFilialComFiliaisResponse> buscarPorIdComFiliais(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoFilialMapper.toComFiliaisResponse(service.buscarPorIdComFiliais(id)));
    }
}
