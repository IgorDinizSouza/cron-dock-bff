package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.RegionalFilialRequest;
import com.agendamento.bff.v1.domain.dto.request.RegionalRequest;
import com.agendamento.bff.v1.domain.dto.response.RegionalComFiliaisResponse;
import com.agendamento.bff.v1.domain.dto.response.RegionalFilialResponse;
import com.agendamento.bff.v1.domain.dto.response.RegionalResponse;
import com.agendamento.bff.v1.domain.mapper.RegionalFilialMapper;
import com.agendamento.bff.v1.domain.mapper.RegionalMapper;
import com.agendamento.bff.v1.domain.model.Regional;
import com.agendamento.bff.v1.domain.model.RegionalFilial;
import com.agendamento.bff.v1.service.RegionalService;
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
@RequestMapping("/api/v1/regional")
@RequiredArgsConstructor
public class RegionalController {

    private final RegionalService service;

    @GetMapping
    public ResponseEntity<List<RegionalResponse>> listar() {
        List<RegionalResponse> resp = service.listar().stream().map(RegionalMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionalResponse> buscarPorId(@PathVariable Long id) {
        Regional regional = service.buscarPorId(id);
        return ResponseEntity.ok(RegionalMapper.toResponse(regional));
    }

    @PostMapping
    public ResponseEntity<RegionalResponse> criar(@RequestBody RegionalRequest req) {
        Regional salvo = service.criar(RegionalMapper.toEntity(req));
        return ResponseEntity
                .created(URI.create("/api/v1/regional/" + salvo.getId()))
                .body(RegionalMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionalResponse> atualizar(@PathVariable Long id, @RequestBody RegionalRequest req) {
        Regional atualizado = service.atualizar(id, RegionalMapper.toEntity(req));
        return ResponseEntity.ok(RegionalMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{regionalId}/filial")
    public ResponseEntity<RegionalFilialResponse> criarRegionalFilial(
            @PathVariable Long regionalId,
            @RequestBody RegionalFilialRequest req
    ) {
        RegionalFilial salvo = service.criarRegionalFilial(regionalId, req.filialId());
        return ResponseEntity
                .created(URI.create("/api/v1/regional/" + regionalId + "/filial/" + salvo.getId()))
                .body(RegionalFilialMapper.toResponse(salvo));
    }

    @GetMapping("/com-filiais")
    public ResponseEntity<List<RegionalComFiliaisResponse>> listarComFiliais() {
        List<RegionalComFiliaisResponse> resp = service.listarComFiliais().stream()
                .map(RegionalMapper::toComFiliaisResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}/com-filiais")
    public ResponseEntity<RegionalComFiliaisResponse> buscarPorIdComFiliais(@PathVariable Long id) {
        Regional regional = service.buscarPorIdComFiliais(id);
        return ResponseEntity.ok(RegionalMapper.toComFiliaisResponse(regional));
    }
}
