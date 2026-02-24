package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.MotivoNoShowRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoNoShowResponse;
import com.agendamento.bff.v1.domain.mapper.MotivoNoShowMapper;
import com.agendamento.bff.v1.domain.model.MotivoNoShow;
import com.agendamento.bff.v1.service.MotivoNoShowService;
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
@RequestMapping("/api/v1/motivo-no-show")
@RequiredArgsConstructor
public class MotivoNoShowController {

    private final MotivoNoShowService service;

    @GetMapping
    public ResponseEntity<List<MotivoNoShowResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(MotivoNoShowMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotivoNoShowResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MotivoNoShowMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<MotivoNoShowResponse> criar(@RequestBody MotivoNoShowRequest req) {
        MotivoNoShow salvo = service.criar(MotivoNoShowMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/motivo-no-show/" + salvo.getId()))
                .body(MotivoNoShowMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotivoNoShowResponse> atualizar(@PathVariable Long id, @RequestBody MotivoNoShowRequest req) {
        MotivoNoShow atualizado = service.atualizar(id, MotivoNoShowMapper.toEntity(req));
        return ResponseEntity.ok(MotivoNoShowMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
