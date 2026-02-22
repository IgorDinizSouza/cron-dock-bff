package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.CompradorRequest;
import com.agendamento.bff.v1.domain.dto.response.CompradorResponse;
import com.agendamento.bff.v1.domain.mapper.CompradorMapper;
import com.agendamento.bff.v1.domain.model.Comprador;
import com.agendamento.bff.v1.service.CompradorService;
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
@RequestMapping("/api/v1/comprador")
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<CompradorResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<CompradorResponse> resp = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(CompradorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{compradorId}")
    public ResponseEntity<CompradorResponse> buscarPorGrupoEmpresarialECompradorId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long compradorId
    ) {
        Comprador comprador = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, compradorId);
        return ResponseEntity.ok(CompradorMapper.toResponse(comprador));
    }

    @PostMapping
    public ResponseEntity<CompradorResponse> criar(@RequestBody CompradorRequest req) {
        Comprador entity = CompradorMapper.toEntity(req);
        Comprador salvo = service.criar(entity, req.grupoEmpresarialId());

        return ResponseEntity
                .created(URI.create("/api/v1/comprador/" + salvo.getId()))
                .body(CompradorMapper.toResponse(salvo));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<CompradorResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody CompradorRequest req
    ) {
        Comprador dados = CompradorMapper.toEntity(req);
        Comprador atualizado = service.atualizar(id, dados, grupoEmpresarialId);
        return ResponseEntity.ok(CompradorMapper.toResponse(atualizado));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
