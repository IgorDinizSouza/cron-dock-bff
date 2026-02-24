package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.MotivoPriorizacaoRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoPriorizacaoResponse;
import com.agendamento.bff.v1.domain.mapper.MotivoPriorizacaoMapper;
import com.agendamento.bff.v1.domain.model.MotivoPriorizacao;
import com.agendamento.bff.v1.service.MotivoPriorizacaoService;
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
@RequestMapping("/api/v1/motivo-priorizacao")
@RequiredArgsConstructor
public class MotivoPriorizacaoController {

    private final MotivoPriorizacaoService service;

    @GetMapping
    public ResponseEntity<List<MotivoPriorizacaoResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(MotivoPriorizacaoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotivoPriorizacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MotivoPriorizacaoMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<MotivoPriorizacaoResponse> criar(@RequestBody MotivoPriorizacaoRequest req) {
        MotivoPriorizacao salvo = service.criar(MotivoPriorizacaoMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/motivo-priorizacao/" + salvo.getId()))
                .body(MotivoPriorizacaoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotivoPriorizacaoResponse> atualizar(@PathVariable Long id, @RequestBody MotivoPriorizacaoRequest req) {
        MotivoPriorizacao atualizado = service.atualizar(id, MotivoPriorizacaoMapper.toEntity(req));
        return ResponseEntity.ok(MotivoPriorizacaoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
