package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.TempoDescarregamentoEspecieCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.TempoDescarregamentoEspecieCargaResponse;
import com.agendamento.bff.v1.domain.mapper.TempoDescarregamentoEspecieCargaMapper;
import com.agendamento.bff.v1.domain.model.TempoDescarregamentoEspecieCarga;
import com.agendamento.bff.v1.service.TempoDescarregamentoEspecieCargaService;
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
@RequestMapping("/api/v1/tempo-descarregamento-especie-carga")
@RequiredArgsConstructor
public class TempoDescarregamentoEspecieCargaController {

    private final TempoDescarregamentoEspecieCargaService service;

    @GetMapping
    public ResponseEntity<List<TempoDescarregamentoEspecieCargaResponse>> listar() {
        List<TempoDescarregamentoEspecieCargaResponse> resp = service.listar().stream()
                .map(TempoDescarregamentoEspecieCargaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TempoDescarregamentoEspecieCargaResponse> buscarPorId(@PathVariable Long id) {
        TempoDescarregamentoEspecieCarga entity = service.buscarPorId(id);
        return ResponseEntity.ok(TempoDescarregamentoEspecieCargaMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TempoDescarregamentoEspecieCargaResponse> criar(
            @RequestBody TempoDescarregamentoEspecieCargaRequest req
    ) {
        TempoDescarregamentoEspecieCarga entity = TempoDescarregamentoEspecieCargaMapper.toEntity(req);
        TempoDescarregamentoEspecieCarga salvo = service.criar(entity, req.especieCargaId());
        return ResponseEntity
                .created(URI.create("/api/v1/tempo-descarregamento-especie-carga/" + salvo.getId()))
                .body(TempoDescarregamentoEspecieCargaMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TempoDescarregamentoEspecieCargaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody TempoDescarregamentoEspecieCargaRequest req
    ) {
        TempoDescarregamentoEspecieCarga dados = TempoDescarregamentoEspecieCargaMapper.toEntity(req);
        TempoDescarregamentoEspecieCarga atualizado = service.atualizar(id, dados, req.especieCargaId());
        return ResponseEntity.ok(TempoDescarregamentoEspecieCargaMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
