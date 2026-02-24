package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.response.EstadoResponse;
import com.agendamento.bff.v1.domain.mapper.EstadoMapper;
import com.agendamento.bff.v1.domain.model.Estado;
import com.agendamento.bff.v1.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estado")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService service;

    @GetMapping
    public ResponseEntity<List<EstadoResponse>> listar(
            @RequestParam(required = false) String descricao
    ) {
        if (descricao != null && !descricao.isBlank()) {
            Estado estado = service.buscarPorDescricao(descricao);
            return ResponseEntity.ok(List.of(EstadoMapper.toResponse(estado)));
        }

        List<EstadoResponse> resp = service.listar().stream().map(EstadoMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> buscarPorId(@PathVariable Long id) {
        Estado estado = service.buscarPorId(id);
        return ResponseEntity.ok(EstadoMapper.toResponse(estado));
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<EstadoResponse> buscarPorDescricao(@PathVariable String descricao) {
        Estado estado = service.buscarPorDescricao(descricao);
        return ResponseEntity.ok(EstadoMapper.toResponse(estado));
    }
}
