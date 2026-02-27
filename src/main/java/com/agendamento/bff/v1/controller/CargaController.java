package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.CargaRequest;
import com.agendamento.bff.v1.domain.dto.request.CargaProdutoRequest;
import com.agendamento.bff.v1.domain.dto.response.CargaResponse;
import com.agendamento.bff.v1.domain.dto.response.CargaProdutoResponse;
import com.agendamento.bff.v1.domain.mapper.CargaMapper;
import com.agendamento.bff.v1.domain.mapper.CargaProdutoMapper;
import com.agendamento.bff.v1.domain.model.Carga;
import com.agendamento.bff.v1.domain.model.CargaProduto;
import com.agendamento.bff.v1.service.CargaService;
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
@RequestMapping("/api/v1/carga")
@RequiredArgsConstructor
public class CargaController {

    private final CargaService service;

    @GetMapping
    public ResponseEntity<List<CargaResponse>> listar() {
        List<CargaResponse> resp = service.listar().stream().map(CargaMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargaResponse> buscarPorId(@PathVariable Long id) {
        Carga entity = service.buscarPorId(id);
        return ResponseEntity.ok(CargaMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CargaResponse> criar(@RequestBody CargaRequest req) {
        Carga entity = CargaMapper.toEntity(req);
        Carga salvo = service.criar(entity, req);
        return ResponseEntity
                .created(URI.create("/api/v1/carga/" + salvo.getId()))
                .body(CargaMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargaResponse> atualizar(@PathVariable Long id, @RequestBody CargaRequest req) {
        Carga dados = CargaMapper.toEntity(req);
        Carga atualizado = service.atualizar(id, dados, req);
        return ResponseEntity.ok(CargaMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cargaId}/produto")
    public ResponseEntity<List<CargaProdutoResponse>> listarProdutosDaCarga(@PathVariable Long cargaId) {
        List<CargaProdutoResponse> resp = service.listarProdutosDaCarga(cargaId)
                .stream()
                .map(CargaProdutoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{cargaId}/produto")
    public ResponseEntity<CargaProdutoResponse> adicionarProdutoNaCarga(
            @PathVariable Long cargaId,
            @RequestBody CargaProdutoRequest req
    ) {
        CargaProduto entity = CargaProdutoMapper.toEntity(req);
        CargaProduto salvo = service.adicionarProdutoNaCarga(cargaId, entity, req);
        return ResponseEntity
                .created(URI.create("/api/v1/carga/" + cargaId + "/produto/" + salvo.getId()))
                .body(CargaProdutoMapper.toResponse(salvo));
    }

    @PutMapping("/{cargaId}/produto/{id}")
    public ResponseEntity<CargaProdutoResponse> editarProdutoNaCarga(
            @PathVariable Long cargaId,
            @PathVariable Long id,
            @RequestBody CargaProdutoRequest req
    ) {
        CargaProduto dados = CargaProdutoMapper.toEntity(req);
        CargaProduto atualizado = service.editarProdutoNaCarga(cargaId, id, dados, req);
        return ResponseEntity.ok(CargaProdutoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{cargaId}/produto/{id}")
    public ResponseEntity<Void> removerProdutoDaCarga(
            @PathVariable Long cargaId,
            @PathVariable Long id
    ) {
        service.removerProdutoDaCarga(cargaId, id);
        return ResponseEntity.noContent().build();
    }
}
