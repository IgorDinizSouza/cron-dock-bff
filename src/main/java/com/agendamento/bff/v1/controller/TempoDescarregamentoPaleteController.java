package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.TempoDescarregamentoPaleteRequest;
import com.agendamento.bff.v1.domain.dto.response.TempoDescarregamentoPaleteResponse;
import com.agendamento.bff.v1.domain.mapper.TempoDescarregamentoPaleteMapper;
import com.agendamento.bff.v1.domain.model.TempoDescarregamentoPalete;
import com.agendamento.bff.v1.service.TempoDescarregamentoPaleteService;
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
@RequestMapping("/api/v1/tempo-descarregamento-palete")
@RequiredArgsConstructor
public class TempoDescarregamentoPaleteController {

    private final TempoDescarregamentoPaleteService service;

    @GetMapping
    public ResponseEntity<List<TempoDescarregamentoPaleteResponse>> listar() {
        List<TempoDescarregamentoPaleteResponse> resp = service.listar().stream()
                .map(TempoDescarregamentoPaleteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TempoDescarregamentoPaleteResponse> buscarPorId(@PathVariable Long id) {
        TempoDescarregamentoPalete entity = service.buscarPorId(id);
        return ResponseEntity.ok(TempoDescarregamentoPaleteMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TempoDescarregamentoPaleteResponse> criar(@RequestBody TempoDescarregamentoPaleteRequest req) {
        TempoDescarregamentoPalete entity = TempoDescarregamentoPaleteMapper.toEntity(req);
        TempoDescarregamentoPalete salvo = service.criar(entity, req.tipoVeiculoId(), req.tipoCargaId());
        return ResponseEntity
                .created(URI.create("/api/v1/tempo-descarregamento-palete/" + salvo.getId()))
                .body(TempoDescarregamentoPaleteMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TempoDescarregamentoPaleteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody TempoDescarregamentoPaleteRequest req
    ) {
        TempoDescarregamentoPalete dados = TempoDescarregamentoPaleteMapper.toEntity(req);
        TempoDescarregamentoPalete atualizado = service.atualizar(id, dados, req.tipoVeiculoId(), req.tipoCargaId());
        return ResponseEntity.ok(TempoDescarregamentoPaleteMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
