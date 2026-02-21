package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.GrupoEmpresarialRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoEmpresarialResponse;
import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.mapper.GrupoEmpresarialMapper;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.service.GrupoEmpresarialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/grupo-empresarial")
@RequiredArgsConstructor
public class GrupoEmpresarialController {

    private final GrupoEmpresarialService service;

    @GetMapping
    public ResponseEntity<List<GrupoEmpresarialResponse>> listar() {
        List<GrupoEmpresarialResponse> resp = service.listar().stream()
                .map(GrupoEmpresarialMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoEmpresarialResponse> buscarPorId(@PathVariable Long id) {
        GrupoEmpresarial grupo = service.buscarPorId(id);
        return ResponseEntity.ok(GrupoEmpresarialMapper.toResponse(grupo));
    }

    @PostMapping
    public ResponseEntity<GrupoEmpresarialResponse> criar(@RequestBody GrupoEmpresarialRequest req) {
        GrupoEmpresarial entity = GrupoEmpresarialMapper.toEntity(req);
        GrupoEmpresarial salvo = service.criar(entity);

        return ResponseEntity
                .created(URI.create("/api/v1/grupo-empresarial/" + salvo.getId()))
                .body(GrupoEmpresarialMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoEmpresarialResponse> atualizar(
            @PathVariable Long id,
            @RequestBody GrupoEmpresarialRequest req
    ) {
        GrupoEmpresarial dados = GrupoEmpresarialMapper.toEntity(req);
        GrupoEmpresarial atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(GrupoEmpresarialMapper.toResponse(atualizado));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<GrupoEmpresarialResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam Status status
    ) {
        GrupoEmpresarial atualizado = service.alterarStatus(id, status);
        return ResponseEntity.ok(GrupoEmpresarialMapper.toResponse(atualizado));
    }
}
