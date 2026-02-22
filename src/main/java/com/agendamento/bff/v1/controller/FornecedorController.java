package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.FornecedorRequest;
import com.agendamento.bff.v1.domain.dto.response.FornecedorResponse;
import com.agendamento.bff.v1.domain.mapper.FornecedorMapper;
import com.agendamento.bff.v1.domain.model.Fornecedor;
import com.agendamento.bff.v1.service.FornecedorService;
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
@RequestMapping("/api/v1/fornecedor")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<FornecedorResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<FornecedorResponse> resp = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(FornecedorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{fornecedorId}")
    public ResponseEntity<FornecedorResponse> buscarPorGrupoEmpresarialEFornecedorId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long fornecedorId
    ) {
        Fornecedor fornecedor = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, fornecedorId);
        return ResponseEntity.ok(FornecedorMapper.toResponse(fornecedor));
    }

    @PostMapping
    public ResponseEntity<FornecedorResponse> criar(@RequestBody FornecedorRequest req) {
        Fornecedor entity = FornecedorMapper.toEntity(req);
        Fornecedor salvo = service.criar(entity, req.grupoEmpresarialId());

        return ResponseEntity
                .created(URI.create("/api/v1/fornecedor/" + salvo.getId()))
                .body(FornecedorMapper.toResponse(salvo));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<FornecedorResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody FornecedorRequest req
    ) {
        Fornecedor dados = FornecedorMapper.toEntity(req);
        Fornecedor atualizado = service.atualizar(id, dados, grupoEmpresarialId);
        return ResponseEntity.ok(FornecedorMapper.toResponse(atualizado));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
