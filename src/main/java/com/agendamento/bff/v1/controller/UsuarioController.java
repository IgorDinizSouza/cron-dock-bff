package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.UsuarioAprovacaoRequest;
import com.agendamento.bff.v1.domain.dto.request.UsuarioRequest;
import com.agendamento.bff.v1.domain.dto.response.UsuarioAprovacaoResponse;
import com.agendamento.bff.v1.domain.dto.response.UsuarioResponse;
import com.agendamento.bff.v1.domain.mapper.UsuarioMapper;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.service.UsuarioService;
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
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<UsuarioResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<UsuarioResponse> resp = service.listarPorGrupoEmpresarial(grupoEmpresarialId)
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{usuarioId}")
    public ResponseEntity<UsuarioResponse> buscarPorGrupoEmpresarialEUsuarioId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long usuarioId
    ) {
        Usuario usuario = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, usuarioId);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @GetMapping("/aprovacao/pendentes")
    public ResponseEntity<List<UsuarioResponse>> listarPendentesAprovacao() {
        List<UsuarioResponse> resp = service.listarPendentesAprovacao()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody UsuarioRequest req) {
        Usuario entity = UsuarioMapper.toEntity(req);
        Usuario salvo = service.criar(entity, req.grupoEmpresarialId(), req.perfilIds());

        return ResponseEntity
                .created(URI.create("/api/v1/usuario/" + salvo.getId()))
                .body(UsuarioMapper.toResponse(salvo));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody UsuarioRequest req
    ) {
        Usuario dados = UsuarioMapper.toEntity(req);
        Usuario atualizado = service.atualizar(id, dados, grupoEmpresarialId, req.perfilIds());
        return ResponseEntity.ok(UsuarioMapper.toResponse(atualizado));
    }

    @PutMapping("/{usuarioId}/aprovacao")
    public ResponseEntity<UsuarioAprovacaoResponse> aprovarUsuario(
            @PathVariable Long usuarioId,
            @RequestBody UsuarioAprovacaoRequest req
    ) {
        return ResponseEntity.ok(service.aprovarUsuario(usuarioId, req));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
