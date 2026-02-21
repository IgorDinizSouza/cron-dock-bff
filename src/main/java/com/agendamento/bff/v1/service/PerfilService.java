package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Perfil;
import com.agendamento.bff.v1.domain.model.Role;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.PerfilRepository;
import com.agendamento.bff.v1.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Perfil> listar() {
        List<Perfil> perfis = perfilRepository.findAllWithRoles();
        perfis.forEach(this::hidratarRolesPorFallbackSeNecessario);
        return perfis;
    }

    @Transactional(readOnly = true)
    public Perfil buscarPorId(Long id) {
        Perfil perfil = perfilRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil nao encontrado. id=" + id));
        hidratarRolesPorFallbackSeNecessario(perfil);
        return perfil;
    }

    @Transactional
    public Perfil criar(Perfil novo, List<Long> roleIds) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDescricaoPerfil(novo.getDescricao());
        if (perfilRepository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe perfil com esta descricao.");
        }

        aplicarRoles(novo, carregarRoles(roleIds));
        return perfilRepository.save(novo);
    }

    @Transactional
    public Perfil atualizar(Long id, Perfil dados, List<Long> roleIds) {
        Perfil atual = buscarPorId(id);

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) {
            if (perfilRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe perfil com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (roleIds != null) {
            aplicarRoles(atual, carregarRoles(roleIds));
        }

        return perfilRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        Perfil perfil = perfilRepository.findWithUsuariosById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil nao encontrado. id=" + id));

        for (Usuario usuario : perfil.getUsuarios()) {
            usuario.getPerfis().remove(perfil);
        }
        perfil.getUsuarios().clear();
        perfil.getRoles().clear();

        perfilRepository.delete(perfil);
    }

    @Transactional(readOnly = true)
    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role buscarRolePorId(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role nao encontrada. id=" + id));
    }

    @Transactional
    public Role criarRole(Role role) {
        validarNomeRole(role.getNome());
        if (roleRepository.existsByNomeIgnoreCase(role.getNome())) {
            throw new IllegalArgumentException("Ja existe role com este nome.");
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Role atualizarRole(Long id, Role dados) {
        Role atual = buscarRolePorId(id);

        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            if (roleRepository.existsByNomeIgnoreCaseAndIdNot(dados.getNome(), id)) {
                throw new IllegalArgumentException("Ja existe role com este nome.");
            }
            atual.setNome(dados.getNome());
        }

        if (dados.getDescricao() != null) {
            atual.setDescricao(dados.getDescricao());
        }

        return roleRepository.save(atual);
    }

    @Transactional
    public void deletarRole(Long id) {
        Role role = roleRepository.findWithPerfisById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role nao encontrada. id=" + id));

        role.getPerfis().forEach(perfil -> perfil.getRoles().remove(role));
        role.getPerfis().clear();

        roleRepository.delete(role);
    }

    private void validarDescricaoPerfil(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }

    private void validarNomeRole(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O campo 'nome' da role e obrigatorio.");
        }
    }

    private Set<Role> carregarRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Uma ou mais roles informadas nao existem.");
        }

        return new HashSet<>(roles);
    }

    private void aplicarRoles(Perfil perfil, Set<Role> novasRoles) {
        perfil.getRoles().forEach(role -> role.getPerfis().remove(perfil));
        perfil.getRoles().clear();
        novasRoles.forEach(role -> role.getPerfis().add(perfil));
        perfil.getRoles().addAll(novasRoles);
    }

    private void hidratarRolesPorFallbackSeNecessario(Perfil perfil) {
        if (perfil.getRoles() != null && !perfil.getRoles().isEmpty()) {
            return;
        }

        List<Role> roles = tentarBuscarRolesTabelaAtual(perfil.getId());
        if (roles.isEmpty()) {
            roles = tentarBuscarRolesTabelaLegada(perfil.getId());
        }

        if (!roles.isEmpty()) {
            perfil.getRoles().clear();
            roles.forEach(role -> role.getPerfis().add(perfil));
            perfil.getRoles().addAll(roles);
        }
    }

    private List<Role> tentarBuscarRolesTabelaAtual(Long perfilId) {
        try {
            return roleRepository.buscarPorPerfilIdTabelaAtual(perfilId);
        } catch (RuntimeException ex) {
            return List.of();
        }
    }

    private List<Role> tentarBuscarRolesTabelaLegada(Long perfilId) {
        try {
            return roleRepository.buscarPorPerfilIdTabelaLegada(perfilId);
        } catch (RuntimeException ex) {
            return List.of();
        }
    }
}
