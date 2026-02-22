package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.FilialRequest;
import com.agendamento.bff.v1.domain.dto.response.FilialResponse;
import com.agendamento.bff.v1.domain.mapper.FilialMapper;
import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.service.FilialService;
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
@RequestMapping("/api/v1/filial")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<FilialResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<FilialResponse> resp = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(FilialMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{filialId}")
    public ResponseEntity<FilialResponse> buscarPorGrupoEmpresarialEFilialId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long filialId
    ) {
        Filial filial = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, filialId);
        return ResponseEntity.ok(FilialMapper.toResponse(filial));
    }

    @PostMapping
    public ResponseEntity<FilialResponse> criar(@RequestBody FilialRequest req) {
        Filial entity = FilialMapper.toEntity(req);
        Filial salvo = service.criar(entity, req.grupoEmpresarialId());

        return ResponseEntity
                .created(URI.create("/api/v1/filial/" + salvo.getId()))
                .body(FilialMapper.toResponse(salvo));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<FilialResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody FilialRequest req
    ) {
        Filial dados = FilialMapper.toEntity(req);
        Filial atualizado = service.atualizar(id, dados, grupoEmpresarialId);
        return ResponseEntity.ok(FilialMapper.toResponse(atualizado));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
