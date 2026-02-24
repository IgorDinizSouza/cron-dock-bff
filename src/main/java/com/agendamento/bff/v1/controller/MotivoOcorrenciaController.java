package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.MotivoOcorrenciaRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoOcorrenciaResponse;
import com.agendamento.bff.v1.domain.mapper.MotivoOcorrenciaMapper;
import com.agendamento.bff.v1.domain.model.MotivoOcorrencia;
import com.agendamento.bff.v1.service.MotivoOcorrenciaService;
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
@RequestMapping("/api/v1/motivo-ocorrencia")
@RequiredArgsConstructor
public class MotivoOcorrenciaController {

    private final MotivoOcorrenciaService service;

    @GetMapping
    public ResponseEntity<List<MotivoOcorrenciaResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(MotivoOcorrenciaMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotivoOcorrenciaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MotivoOcorrenciaMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<MotivoOcorrenciaResponse> criar(@RequestBody MotivoOcorrenciaRequest req) {
        MotivoOcorrencia salvo = service.criar(MotivoOcorrenciaMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/motivo-ocorrencia/" + salvo.getId()))
                .body(MotivoOcorrenciaMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotivoOcorrenciaResponse> atualizar(@PathVariable Long id, @RequestBody MotivoOcorrenciaRequest req) {
        MotivoOcorrencia atualizado = service.atualizar(id, MotivoOcorrenciaMapper.toEntity(req));
        return ResponseEntity.ok(MotivoOcorrenciaMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
