package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.GrupoFornecedorFornecedorRequest;
import com.agendamento.bff.v1.domain.dto.request.GrupoFornecedorRequest;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorComFornecedoresResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorFornecedorResponse;
import com.agendamento.bff.v1.domain.dto.response.GrupoFornecedorResponse;
import com.agendamento.bff.v1.domain.mapper.GrupoFornecedorFornecedorMapper;
import com.agendamento.bff.v1.domain.mapper.GrupoFornecedorMapper;
import com.agendamento.bff.v1.domain.model.GrupoFornecedor;
import com.agendamento.bff.v1.domain.model.GrupoFornecedorFornecedor;
import com.agendamento.bff.v1.service.GrupoFornecedorService;
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
@RequestMapping("/api/v1/grupo-fornecedor")
@RequiredArgsConstructor
public class GrupoFornecedorController {

    private final GrupoFornecedorService service;

    @GetMapping
    public ResponseEntity<List<GrupoFornecedorResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(GrupoFornecedorMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoFornecedorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoFornecedorMapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<GrupoFornecedorResponse> criar(@RequestBody GrupoFornecedorRequest req) {
        GrupoFornecedor salvo = service.criar(GrupoFornecedorMapper.toEntity(req));
        return ResponseEntity.created(URI.create("/api/v1/grupo-fornecedor/" + salvo.getId()))
                .body(GrupoFornecedorMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoFornecedorResponse> atualizar(@PathVariable Long id, @RequestBody GrupoFornecedorRequest req) {
        GrupoFornecedor atualizado = service.atualizar(id, GrupoFornecedorMapper.toEntity(req));
        return ResponseEntity.ok(GrupoFornecedorMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{grupoFornecedorId}/fornecedor")
    public ResponseEntity<GrupoFornecedorFornecedorResponse> criarVinculoFornecedor(
            @PathVariable Long grupoFornecedorId,
            @RequestBody GrupoFornecedorFornecedorRequest req
    ) {
        GrupoFornecedorFornecedor salvo = service.criarVinculo(grupoFornecedorId, req.fornecedorId());
        return ResponseEntity.created(URI.create("/api/v1/grupo-fornecedor/" + grupoFornecedorId + "/fornecedor/" + salvo.getId()))
                .body(GrupoFornecedorFornecedorMapper.toResponse(salvo));
    }

    @GetMapping("/com-fornecedores")
    public ResponseEntity<List<GrupoFornecedorComFornecedoresResponse>> listarComFornecedores() {
        return ResponseEntity.ok(service.listarComFornecedores().stream()
                .map(GrupoFornecedorMapper::toComFornecedoresResponse)
                .toList());
    }

    @GetMapping("/{id}/com-fornecedores")
    public ResponseEntity<GrupoFornecedorComFornecedoresResponse> buscarPorIdComFornecedores(@PathVariable Long id) {
        return ResponseEntity.ok(GrupoFornecedorMapper.toComFornecedoresResponse(service.buscarPorIdComFornecedores(id)));
    }
}
