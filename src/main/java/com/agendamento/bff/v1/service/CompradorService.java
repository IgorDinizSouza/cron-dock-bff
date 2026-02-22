package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.Comprador;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.CompradorRepository;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompradorService {

    private final CompradorRepository compradorRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;

    @Transactional(readOnly = true)
    public List<Comprador> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return compradorRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Comprador buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long compradorId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return compradorRepository.findByIdAndGrupoEmpresarialId(compradorId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comprador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", compradorId=" + compradorId
                ));
    }

    @Transactional
    public Comprador criar(Comprador novo, Long grupoEmpresarialId) {
        if (novo.getId() == null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' e obrigatorio.");
        }
        if (compradorRepository.existsById(novo.getId())) {
            throw new IllegalArgumentException("Ja existe comprador com este id.");
        }

        validarDadosComprador(novo);
        validarDescricaoDuplicada(grupoEmpresarialId, novo.getDescricao(), null);
        novo.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        }

        return compradorRepository.save(novo);
    }

    @Transactional
    public Comprador atualizar(Long id, Comprador dados, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Comprador atual = compradorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comprador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", compradorId=" + id
                ));

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) {
            validarDescricaoDuplicada(grupoEmpresarialId, dados.getDescricao(), id);
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
        if (dados.getDescricao() == null || dados.getDescricao().isBlank()) {
            validarDescricaoDuplicada(grupoEmpresarialId, atual.getDescricao(), id);
        }

        return compradorRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Comprador comprador = compradorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comprador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", compradorId=" + id
                ));
        compradorRepository.delete(comprador);
    }

    private void validarDadosComprador(Comprador comprador) {
        if (comprador.getDescricao() == null || comprador.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
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

    private void validarDescricaoDuplicada(Long grupoEmpresarialId, String descricao, Long compradorIdIgnorado) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        boolean existe = compradorIdIgnorado == null
                ? compradorRepository.existsByGrupoEmpresarialIdAndDescricaoIgnoreCase(grupoEmpresarialId, descricao)
                : compradorRepository.existsByGrupoEmpresarialIdAndDescricaoIgnoreCaseAndIdNot(
                grupoEmpresarialId,
                descricao,
                compradorIdIgnorado
        );

        if (existe) {
            throw new IllegalArgumentException("Ja existe comprador com esta descricao no grupo empresarial informado.");
        }
    }
}
