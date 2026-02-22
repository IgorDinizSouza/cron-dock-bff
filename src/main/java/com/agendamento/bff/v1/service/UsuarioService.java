package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.domain.model.Perfil;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.exception.UnauthorizedException;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import com.agendamento.bff.v1.repository.PerfilRepository;
import com.agendamento.bff.v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return usuarioRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long usuarioId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return usuarioRepository.findByIdAndGrupoEmpresarialId(usuarioId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", usuarioId=" + usuarioId
                ));
    }

    @Transactional(readOnly = true)
    public Usuario autenticar(String usuario, String senha) {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("O campo 'usuario' e obrigatorio.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("O campo 'senha' e obrigatorio.");
        }

        List<Usuario> candidatos = usuarioRepository.findAllByEmailIgnoreCase(usuario);
        if (candidatos.isEmpty()) {
            throw new UnauthorizedException("Credenciais invalidas.");
        }

        List<Usuario> autenticados = candidatos.stream()
                .filter(u -> u.getStatus() == Status.ATIVO)
                .filter(u -> u.getSenha() != null && passwordEncoder.matches(senha, u.getSenha()))
                .toList();

        if (autenticados.isEmpty()) {
            throw new UnauthorizedException("Credenciais invalidas ou usuario inativo.");
        }

        if (autenticados.size() > 1) {
            throw new UnauthorizedException("Usuario duplicado para o login informado.");
        }

        return autenticados.getFirst();
    }

    @Transactional
    public Usuario criar(Usuario novo, Long grupoEmpresarialId, List<Long> perfilIds) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDadosUsuario(novo);
        validarEmailDuplicado(grupoEmpresarialId, novo.getEmail(), null);
        novo.setSenha(criptografarSenha(novo.getSenha()));

        GrupoEmpresarial grupoEmpresarial = buscarGrupoEmpresarial(grupoEmpresarialId);
        novo.setGrupoEmpresarial(grupoEmpresarial);
        novo.setPerfis(carregarPerfis(perfilIds));

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        }

        return usuarioRepository.save(novo);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dados, Long grupoEmpresarialId, List<Long> perfilIds) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Usuario atual = usuarioRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", usuarioId=" + id
                ));

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) {
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getEmail() != null && !dados.getEmail().isBlank()) {
            Long grupoId = grupoEmpresarialId != null
                    ? grupoEmpresarialId
                    : atual.getGrupoEmpresarial().getId();
            validarEmailDuplicado(grupoId, dados.getEmail(), id);
            atual.setEmail(dados.getEmail());
        }

        if (grupoEmpresarialId != null) {
            atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
            if (dados.getEmail() == null || dados.getEmail().isBlank()) {
                validarEmailDuplicado(grupoEmpresarialId, atual.getEmail(), id);
            }
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        if (dados.getSenha() == null || dados.getSenha().isBlank()) {
            throw new IllegalArgumentException("O campo 'senha' e obrigatorio.");
        }
        atual.setSenha(criptografarSenha(dados.getSenha()));

        if (perfilIds != null) {
            atual.setPerfis(carregarPerfis(perfilIds));
        }

        return usuarioRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Usuario usuario = usuarioRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", usuarioId=" + id
                ));
        usuarioRepository.delete(usuario);
    }

    private void validarDadosUsuario(Usuario usuario) {
        if (usuario.getDescricao() == null || usuario.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O campo 'email' e obrigatorio.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException("O campo 'senha' e obrigatorio.");
        }
    }

    private void validarGrupoEmpresarialExiste(Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        if (!grupoEmpresarialRepository.existsById(grupoEmpresarialId)) {
            throw new ResourceNotFoundException("Grupo empresarial nao encontrado. id=" + grupoEmpresarialId);
        }
    }

    private GrupoEmpresarial buscarGrupoEmpresarial(Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }
        return grupoEmpresarialRepository.findById(grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grupo empresarial nao encontrado. id=" + grupoEmpresarialId
                ));
    }

    private Set<Perfil> carregarPerfis(List<Long> perfilIds) {
        if (perfilIds == null || perfilIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Perfil> perfis = perfilRepository.findAllById(perfilIds);
        if (perfis.size() != perfilIds.size()) {
            throw new IllegalArgumentException("Um ou mais perfis informados nao existem.");
        }
        return new HashSet<>(perfis);
    }

    private void validarEmailDuplicado(Long grupoEmpresarialId, String email, Long usuarioIdIgnorado) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        boolean existe = usuarioIdIgnorado == null
                ? usuarioRepository.existsByGrupoEmpresarialIdAndEmailIgnoreCase(grupoEmpresarialId, email)
                : usuarioRepository.existsByGrupoEmpresarialIdAndEmailIgnoreCaseAndIdNot(
                grupoEmpresarialId,
                email,
                usuarioIdIgnorado
        );

        if (existe) {
            throw new IllegalArgumentException("Ja existe usuario com este email no grupo empresarial informado.");
        }
    }

    private String criptografarSenha(String senhaRecebida) {
        if (senhaRecebida == null || senhaRecebida.isBlank()) {
            throw new IllegalArgumentException("O campo 'senha' e obrigatorio.");
        }
        return passwordEncoder.encode(senhaRecebida);
    }
}
