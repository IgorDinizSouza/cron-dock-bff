package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.TipoCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.TipoCargaResponse;
import com.agendamento.bff.v1.domain.mapper.TipoCargaMapper;
import com.agendamento.bff.v1.domain.model.TipoCarga;
import com.agendamento.bff.v1.service.TipoCargaService;
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
@RequestMapping("/api/v1/tipo-carga")
@RequiredArgsConstructor
public class TipoCargaController {

    private final TipoCargaService service;

    @GetMapping
    public ResponseEntity<List<TipoCargaResponse>> listar() {
        List<TipoCargaResponse> resp = service.listar().stream().map(TipoCargaMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCargaResponse> buscarPorId(@PathVariable Long id) {
        TipoCarga entity = service.buscarPorId(id);
        return ResponseEntity.ok(TipoCargaMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TipoCargaResponse> criar(@RequestBody TipoCargaRequest req) {
        TipoCarga entity = TipoCargaMapper.toEntity(req);
        TipoCarga salvo = service.criar(entity);
        return ResponseEntity
                .created(URI.create("/api/v1/tipo-carga/" + salvo.getId()))
                .body(TipoCargaMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCargaResponse> atualizar(@PathVariable Long id, @RequestBody TipoCargaRequest req) {
        TipoCarga dados = TipoCargaMapper.toEntity(req);
        TipoCarga atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(TipoCargaMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
