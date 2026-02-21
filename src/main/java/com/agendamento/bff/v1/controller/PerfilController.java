package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.PerfilRequest;
import com.agendamento.bff.v1.domain.dto.request.RoleRequest;
import com.agendamento.bff.v1.domain.dto.response.PerfilResponse;
import com.agendamento.bff.v1.domain.dto.response.RoleResponse;
import com.agendamento.bff.v1.domain.mapper.PerfilMapper;
import com.agendamento.bff.v1.domain.mapper.RoleMapper;
import com.agendamento.bff.v1.domain.model.Perfil;
import com.agendamento.bff.v1.domain.model.Role;
import com.agendamento.bff.v1.service.PerfilService;
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
@RequestMapping("/api/v1/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService service;

    @GetMapping
    public ResponseEntity<List<PerfilResponse>> listar() {
        List<PerfilResponse> resp = service.listar().stream().map(PerfilMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponse> buscarPorId(@PathVariable Long id) {
        Perfil perfil = service.buscarPorId(id);
        return ResponseEntity.ok(PerfilMapper.toResponse(perfil));
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> criar(@RequestBody PerfilRequest req) {
        Perfil entity = PerfilMapper.toEntity(req);
        Perfil salvo = service.criar(entity, req.roleIds());
        return ResponseEntity
                .created(URI.create("/api/v1/perfil/" + salvo.getId()))
                .body(PerfilMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponse> atualizar(@PathVariable Long id, @RequestBody PerfilRequest req) {
        Perfil dados = PerfilMapper.toEntity(req);
        Perfil atualizado = service.atualizar(id, dados, req.roleIds());
        return ResponseEntity.ok(PerfilMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role")
    public ResponseEntity<List<RoleResponse>> listarRoles() {
        List<RoleResponse> resp = service.listarRoles().stream().map(RoleMapper::toResponse).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<RoleResponse> buscarRolePorId(@PathVariable Long id) {
        Role role = service.buscarRolePorId(id);
        return ResponseEntity.ok(RoleMapper.toResponse(role));
    }

    @PostMapping("/role")
    public ResponseEntity<RoleResponse> criarRole(@RequestBody RoleRequest req) {
        Role entity = RoleMapper.toEntity(req);
        Role salvo = service.criarRole(entity);
        return ResponseEntity
                .created(URI.create("/api/v1/perfil/role/" + salvo.getId()))
                .body(RoleMapper.toResponse(salvo));
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<RoleResponse> atualizarRole(@PathVariable Long id, @RequestBody RoleRequest req) {
        Role dados = RoleMapper.toEntity(req);
        Role atualizado = service.atualizarRole(id, dados);
        return ResponseEntity.ok(RoleMapper.toResponse(atualizado));
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> deletarRole(@PathVariable Long id) {
        service.deletarRole(id);
        return ResponseEntity.noContent().build();
    }
}
