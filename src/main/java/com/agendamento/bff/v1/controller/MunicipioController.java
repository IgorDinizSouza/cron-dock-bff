package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.MunicipioRequest;
import com.agendamento.bff.v1.domain.dto.response.MunicipioResponse;
import com.agendamento.bff.v1.domain.mapper.MunicipioMapper;
import com.agendamento.bff.v1.domain.model.Municipio;
import com.agendamento.bff.v1.service.MunicipioService;
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
@RequestMapping("/api/v1/municipio")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService service;

    @GetMapping
    public ResponseEntity<List<MunicipioResponse>> listar() {
        List<MunicipioResponse> resp = service.listar().stream().map(MunicipioMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipioResponse> buscarPorId(@PathVariable Long id) {
        Municipio entity = service.buscarPorId(id);
        return ResponseEntity.ok(MunicipioMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MunicipioResponse> criar(@RequestBody MunicipioRequest req) {
        Municipio entity = MunicipioMapper.toEntity(req);
        Municipio salvo = service.criar(entity, req.estadoId());
        return ResponseEntity
                .created(URI.create("/api/v1/municipio/" + salvo.getId()))
                .body(MunicipioMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MunicipioResponse> atualizar(@PathVariable Long id, @RequestBody MunicipioRequest req) {
        Municipio dados = MunicipioMapper.toEntity(req);
        Municipio atualizado = service.atualizar(id, dados, req.estadoId());
        return ResponseEntity.ok(MunicipioMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
