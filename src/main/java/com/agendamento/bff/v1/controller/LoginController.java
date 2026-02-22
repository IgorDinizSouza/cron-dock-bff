package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.LoginRequest;
import com.agendamento.bff.v1.domain.dto.response.LoginResponse;
import com.agendamento.bff.v1.domain.mapper.PerfilMapper;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Usuario usuario = usuarioService.autenticar(req.usuario(), req.senha());

        LoginResponse response = new LoginResponse(
                usuario.getId(),
                usuario.getDescricao(),
                usuario.getEmail(),
                usuario.getStatus(),
                usuario.getGrupoEmpresarial() != null ? usuario.getGrupoEmpresarial().getId() : null,
                usuario.getGrupoEmpresarial() != null ? usuario.getGrupoEmpresarial().getDescricao() : null,
                usuario.getPerfis().stream().map(PerfilMapper::toResponse).toList()
        );

        return ResponseEntity.ok(response);
    }
}
