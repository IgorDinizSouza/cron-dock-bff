package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.TipoVeiculoRequest;
import com.agendamento.bff.v1.domain.dto.response.TipoVeiculoResponse;
import com.agendamento.bff.v1.domain.mapper.TipoVeiculoMapper;
import com.agendamento.bff.v1.domain.model.TipoVeiculo;
import com.agendamento.bff.v1.service.TipoVeiculoService;
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
@RequestMapping("/api/v1/tipo-veiculo")
@RequiredArgsConstructor
public class TipoVeiculoController {

    private final TipoVeiculoService service;

    @GetMapping
    public ResponseEntity<List<TipoVeiculoResponse>> listar() {
        List<TipoVeiculoResponse> resp = service.listar().stream().map(TipoVeiculoMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoVeiculoResponse> buscarPorId(@PathVariable Long id) {
        TipoVeiculo entity = service.buscarPorId(id);
        return ResponseEntity.ok(TipoVeiculoMapper.toResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TipoVeiculoResponse> criar(@RequestBody TipoVeiculoRequest req) {
        TipoVeiculo entity = TipoVeiculoMapper.toEntity(req);
        TipoVeiculo salvo = service.criar(entity);
        return ResponseEntity
                .created(URI.create("/api/v1/tipo-veiculo/" + salvo.getId()))
                .body(TipoVeiculoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoVeiculoResponse> atualizar(@PathVariable Long id, @RequestBody TipoVeiculoRequest req) {
        TipoVeiculo dados = TipoVeiculoMapper.toEntity(req);
        TipoVeiculo atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(TipoVeiculoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
