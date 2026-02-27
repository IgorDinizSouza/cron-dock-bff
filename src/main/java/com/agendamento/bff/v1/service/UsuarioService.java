package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.domain.model.Perfil;
import com.agendamento.bff.v1.domain.model.StatusAprovacaoUsuario;
import com.agendamento.bff.v1.domain.model.Usuario;
import com.agendamento.bff.v1.domain.model.UsuarioAprovacao;
import com.agendamento.bff.v1.domain.dto.request.UsuarioAprovacaoRequest;
import com.agendamento.bff.v1.domain.dto.response.UsuarioAprovacaoResponse;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.exception.UnauthorizedException;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import com.agendamento.bff.v1.repository.PerfilRepository;
import com.agendamento.bff.v1.repository.StatusAprovacaoUsuarioRepository;
import com.agendamento.bff.v1.repository.UsuarioAprovacaoRepository;
import com.agendamento.bff.v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Long STATUS_APROVACAO_SOLICITADO = 1L;
    private static final Long STATUS_APROVACAO_APROVADO = 2L;
    private static final Long STATUS_APROVACAO_RECUSADO = 3L;
    private static final String ROLE_APROVAR_USUARIO = "APROVAR_USUARIO";
    private static final String ROLE_ADMINISTRADOR = "ADMINISTRADOR";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAprovacaoRepository usuarioAprovacaoRepository;
    private final StatusAprovacaoUsuarioRepository statusAprovacaoUsuarioRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        List<Usuario> usuarios = usuarioRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
        List<Long> usuariosPendentesIds =
                usuarioAprovacaoRepository.findIdsUsuarioSolicitanteByStatusAprovacaoUsuarioIdAndGrupoEmpresarialId(
                        STATUS_APROVACAO_SOLICITADO,
                        grupoEmpresarialId
                );

        if (usuariosPendentesIds.isEmpty()) {
            return usuarios;
        }

        return usuarios.stream()
                .filter(usuario -> !usuariosPendentesIds.contains(usuario.getId()))
                .toList();
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
    public List<Usuario> listarPendentesAprovacao() {
        return usuarioAprovacaoRepository.findAllByStatusAprovacaoUsuarioId(STATUS_APROVACAO_SOLICITADO)
                .stream()
                .map(UsuarioAprovacao::getUsuarioSolicitante)
                .toList();
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
                .filter(u -> u.getSenha() != null && passwordEncoder.matches(senha, u.getSenha()))
                .toList();

        if (autenticados.isEmpty()) {
            throw new UnauthorizedException("Credenciais invalidas.");
        }

        if (autenticados.size() > 1) {
            throw new UnauthorizedException("Usuario duplicado para o login informado.");
        }

        Usuario autenticado = autenticados.getFirst();

        if (autenticado.getStatus() != Status.ATIVO) {
            throw new UnauthorizedException("Credenciais invalidas ou usuario inativo.");
        }

        UsuarioAprovacao aprovacao = usuarioAprovacaoRepository.findByUsuarioSolicitanteId(autenticado.getId())
                .orElse(null);

        if (aprovacao == null || aprovacao.getStatusAprovacaoUsuario() == null
                || aprovacao.getStatusAprovacaoUsuario().getId() == null
                || STATUS_APROVACAO_SOLICITADO.equals(aprovacao.getStatusAprovacaoUsuario().getId())) {
            throw new UnauthorizedException("Seu usuário foi solicitado e esta aguardando aprovação");
        }

        if (STATUS_APROVACAO_RECUSADO.equals(aprovacao.getStatusAprovacaoUsuario().getId())) {
            throw new UnauthorizedException(
                    "O usuário não foi aprovado, por favor, entre em contato com o administrador do sistema"
            );
        }

        if (!STATUS_APROVACAO_APROVADO.equals(aprovacao.getStatusAprovacaoUsuario().getId())) {
            throw new UnauthorizedException("Usuario sem aprovacao valida para acesso.");
        }

        return autenticado;
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

        Usuario salvo = usuarioRepository.save(novo);
        criarFluxoAprovacaoInicial(salvo);
        return salvo;
    }

    @Transactional
    public UsuarioAprovacaoResponse aprovarUsuario(Long usuarioSolicitanteId, UsuarioAprovacaoRequest req) {
        if (usuarioSolicitanteId == null) {
            throw new IllegalArgumentException("O campo 'usuarioSolicitanteId' e obrigatorio.");
        }
        if (req == null) {
            throw new IllegalArgumentException("O corpo da requisicao e obrigatorio.");
        }
        if (req.idUsuarioAprovador() == null) {
            throw new IllegalArgumentException("O campo 'idUsuarioAprovador' e obrigatorio.");
        }
        if (req.idStatusAprovacaoUsuario() == null) {
            throw new IllegalArgumentException("O campo 'idStatusAprovacaoUsuario' e obrigatorio.");
        }
        if (!STATUS_APROVACAO_APROVADO.equals(req.idStatusAprovacaoUsuario())
                && !STATUS_APROVACAO_RECUSADO.equals(req.idStatusAprovacaoUsuario())) {
            throw new IllegalArgumentException("O status de aprovacao deve ser 2 (aprovado) ou 3 (recusado).");
        }

        if (STATUS_APROVACAO_RECUSADO.equals(req.idStatusAprovacaoUsuario())
                && (req.motivoRecusa() == null || req.motivoRecusa().isBlank())) {
            throw new IllegalArgumentException("O campo 'motivoRecusa' e obrigatorio quando o status for recusado.");
        }

        Usuario usuarioSolicitante = usuarioRepository.findById(usuarioSolicitanteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario solicitante nao encontrado. id=" + usuarioSolicitanteId
                ));

        Usuario usuarioAprovador = usuarioRepository.findById(req.idUsuarioAprovador())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario aprovador nao encontrado. id=" + req.idUsuarioAprovador()
                ));

        validarRoleAprovador(usuarioAprovador);

        StatusAprovacaoUsuario statusAprovacao = statusAprovacaoUsuarioRepository.findById(req.idStatusAprovacaoUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status de aprovacao nao encontrado. id=" + req.idStatusAprovacaoUsuario()
                ));

        UsuarioAprovacao aprovacao = usuarioAprovacaoRepository.findByUsuarioSolicitanteId(usuarioSolicitanteId)
                .orElseGet(() -> UsuarioAprovacao.builder()
                        .usuarioSolicitante(usuarioSolicitante)
                        .dataSolicitacao(LocalDateTime.now())
                        .build());

        aprovacao.setUsuarioSolicitante(usuarioSolicitante);
        aprovacao.setUsuarioAprovador(usuarioAprovador);
        aprovacao.setStatusAprovacaoUsuario(statusAprovacao);
        aprovacao.setMotivoRecusa(STATUS_APROVACAO_RECUSADO.equals(statusAprovacao.getId())
                ? req.motivoRecusa().trim()
                : null);
        aprovacao.setDataAprovacao(LocalDateTime.now());
        if (aprovacao.getDataSolicitacao() == null) {
            aprovacao.setDataSolicitacao(LocalDateTime.now());
        }

        UsuarioAprovacao salvo = usuarioAprovacaoRepository.save(aprovacao);

        String mensagem = STATUS_APROVACAO_APROVADO.equals(statusAprovacao.getId())
                ? "Usuario aprovado com sucesso."
                : "Usuario recusado com sucesso.";

        return new UsuarioAprovacaoResponse(
                salvo.getId(),
                salvo.getUsuarioSolicitante() != null ? salvo.getUsuarioSolicitante().getId() : null,
                salvo.getUsuarioAprovador() != null ? salvo.getUsuarioAprovador().getId() : null,
                salvo.getStatusAprovacaoUsuario() != null ? salvo.getStatusAprovacaoUsuario().getId() : null,
                salvo.getStatusAprovacaoUsuario() != null ? salvo.getStatusAprovacaoUsuario().getDescricao() : null,
                salvo.getMotivoRecusa(),
                salvo.getDataSolicitacao(),
                salvo.getDataAprovacao(),
                mensagem
        );
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

    private void criarFluxoAprovacaoInicial(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario salvo e obrigatorio para iniciar fluxo de aprovacao.");
        }

        if (usuarioAprovacaoRepository.findByUsuarioSolicitanteId(usuario.getId()).isPresent()) {
            return;
        }

        StatusAprovacaoUsuario statusSolicitado = statusAprovacaoUsuarioRepository.findById(STATUS_APROVACAO_SOLICITADO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status de aprovacao 'Solicitado' nao encontrado. id=" + STATUS_APROVACAO_SOLICITADO
                ));

        UsuarioAprovacao aprovacao = UsuarioAprovacao.builder()
                .usuarioSolicitante(usuario)
                .statusAprovacaoUsuario(statusSolicitado)
                .dataSolicitacao(LocalDateTime.now())
                .build();

        usuarioAprovacaoRepository.save(aprovacao);

     }

    private void validarRoleAprovador(Usuario usuarioAprovador) {
        boolean possuiRole = usuarioAprovador.getPerfis() != null && usuarioAprovador.getPerfis().stream()
                .filter(perfil -> perfil.getRoles() != null)
                .flatMap(perfil -> perfil.getRoles().stream())
                .anyMatch(role -> role.getNome() != null
                        && (ROLE_APROVAR_USUARIO.equalsIgnoreCase(role.getNome())
                        || ROLE_ADMINISTRADOR.equalsIgnoreCase(role.getNome())));

        if (!possuiRole) {
            throw new UnauthorizedException(
                    "Usuario aprovador nao possui as roles APROVAR_USUARIO ou ROLE_ADMINISTRADOR."
            );
        }
    }
}
