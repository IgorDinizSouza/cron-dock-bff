package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.MotivoCancelamentoRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoCancelamentoResponse;
import com.agendamento.bff.v1.domain.mapper.MotivoCancelamentoMapper;
import com.agendamento.bff.v1.domain.model.MotivoCancelamento;
import com.agendamento.bff.v1.service.MotivoCancelamentoService;
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
@RequestMapping("/api/v1/motivo-cancelamento")
@RequiredArgsConstructor
public class MotivoCancelamentoController {

    private final MotivoCancelamentoService service;

    @GetMapping
    public ResponseEntity<List<MotivoCancelamentoResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(MotivoCancelamentoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotivoCancelamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MotivoCancelamentoMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<MotivoCancelamentoResponse> criar(@RequestBody MotivoCancelamentoRequest req) {
        MotivoCancelamento salvo = service.criar(MotivoCancelamentoMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/motivo-cancelamento/" + salvo.getId()))
                .body(MotivoCancelamentoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotivoCancelamentoResponse> atualizar(@PathVariable Long id, @RequestBody MotivoCancelamentoRequest req) {
        MotivoCancelamento atualizado = service.atualizar(id, MotivoCancelamentoMapper.toEntity(req));
        return ResponseEntity.ok(MotivoCancelamentoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
