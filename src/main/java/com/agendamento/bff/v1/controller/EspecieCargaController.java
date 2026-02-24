package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.EspecieCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.EspecieCargaResponse;
import com.agendamento.bff.v1.domain.mapper.EspecieCargaMapper;
import com.agendamento.bff.v1.domain.model.EspecieCarga;
import com.agendamento.bff.v1.service.EspecieCargaService;
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
@RequestMapping("/api/v1/especie-carga")
@RequiredArgsConstructor
public class EspecieCargaController {

    private final EspecieCargaService service;

    @GetMapping
    public ResponseEntity<List<EspecieCargaResponse>> listar() {
        List<EspecieCargaResponse> resp = service.listar().stream().map(EspecieCargaMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecieCargaResponse> buscarPorId(@PathVariable Long id) {
        EspecieCarga entity = service.buscarPorId(id);
        return ResponseEntity.ok(EspecieCargaMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<EspecieCargaResponse> criar(@RequestBody EspecieCargaRequest req) {
        EspecieCarga entity = EspecieCargaMapper.toEntity(req);
        EspecieCarga salvo = service.criar(entity);
        return ResponseEntity
                .created(URI.create("/api/v1/especie-carga/" + salvo.getId()))
                .body(EspecieCargaMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecieCargaResponse> atualizar(@PathVariable Long id, @RequestBody EspecieCargaRequest req) {
        EspecieCarga dados = EspecieCargaMapper.toEntity(req);
        EspecieCarga atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(EspecieCargaMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
