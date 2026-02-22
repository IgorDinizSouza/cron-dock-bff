package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.domain.model.Transportador;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import com.agendamento.bff.v1.repository.TransportadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;

    @Transactional(readOnly = true)
    public List<Transportador> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return transportadorRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Transportador buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long transportadorId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return transportadorRepository.findByIdAndGrupoEmpresarialId(transportadorId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transportador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", transportadorId=" + transportadorId
                ));
    }

    @Transactional
    public Transportador criar(Transportador novo, Long grupoEmpresarialId) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDadosTransportador(novo);
        validarCnpjDuplicado(grupoEmpresarialId, novo.getCnpj(), null);

        GrupoEmpresarial grupoEmpresarial = buscarGrupoEmpresarial(grupoEmpresarialId);
        novo.setGrupoEmpresarial(grupoEmpresarial);

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        }

        return transportadorRepository.save(novo);
    }

    @Transactional
    public Transportador atualizar(Long id, Transportador dados, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Transportador atual = transportadorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transportador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", transportadorId=" + id
                ));

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) {
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getCnpj() != null && !dados.getCnpj().isBlank()) {
            validarCnpjDuplicado(grupoEmpresarialId, dados.getCnpj(), id);
            atual.setCnpj(dados.getCnpj());
        }

        if (grupoEmpresarialId != null) {
            atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
            if (dados.getCnpj() == null || dados.getCnpj().isBlank()) {
                validarCnpjDuplicado(grupoEmpresarialId, atual.getCnpj(), id);
            }
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return transportadorRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Transportador transportador = transportadorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transportador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", transportadorId=" + id
                ));
        transportadorRepository.delete(transportador);
    }

    private void validarDadosTransportador(Transportador transportador) {
        if (transportador.getDescricao() == null || transportador.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }

        if (transportador.getCnpj() == null || transportador.getCnpj().isBlank()) {
            throw new IllegalArgumentException("O campo 'cnpj' e obrigatorio.");
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

    private void validarCnpjDuplicado(Long grupoEmpresarialId, String cnpj, Long transportadorIdIgnorado) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        boolean existe = transportadorIdIgnorado == null
                ? transportadorRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCase(grupoEmpresarialId, cnpj)
                : transportadorRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCaseAndIdNot(
                grupoEmpresarialId,
                cnpj,
                transportadorIdIgnorado
        );

        if (existe) {
            throw new IllegalArgumentException("Ja existe transportador com este cnpj no grupo empresarial informado.");
        }
    }
}
