package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.domain.model.GrupoFilial;
import com.agendamento.bff.v1.domain.model.GrupoFilialFilial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.FilialRepository;
import com.agendamento.bff.v1.repository.GrupoFilialFilialRepository;
import com.agendamento.bff.v1.repository.GrupoFilialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoFilialService {

    private final GrupoFilialRepository grupoFilialRepository;
    private final GrupoFilialFilialRepository vinculoRepository;
    private final FilialRepository filialRepository;

    @Transactional(readOnly = true)
    public List<GrupoFilial> listar() {
        return grupoFilialRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GrupoFilial buscarPorId(Long id) {
        return grupoFilialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoFilial nao encontrado. id=" + id));
    }

    @Transactional
    public GrupoFilial criar(GrupoFilial novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(novo.getDescricao());
        if (grupoFilialRepository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe grupo de filial com esta descricao.");
        }
        return grupoFilialRepository.save(novo);
    }

    @Transactional
    public GrupoFilial atualizar(Long id, GrupoFilial dados) {
        GrupoFilial atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (grupoFilialRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe grupo de filial com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return grupoFilialRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        grupoFilialRepository.delete(buscarPorId(id));
    }

    @Transactional
    public GrupoFilialFilial criarVinculo(Long grupoFilialId, Long filialId) {
        if (grupoFilialId == null) {
            throw new IllegalArgumentException("O campo 'grupoFilialId' e obrigatorio.");
        }
        if (filialId == null) {
            throw new IllegalArgumentException("O campo 'filialId' e obrigatorio.");
        }
        if (vinculoRepository.existsByGrupoFilial_IdAndFilial_Id(grupoFilialId, filialId)) {
            throw new IllegalArgumentException("Esta filial ja esta vinculada ao grupo informado.");
        }

        GrupoFilial grupo = buscarPorId(grupoFilialId);
        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial nao encontrada. id=" + filialId));

        GrupoFilialFilial vinculo = GrupoFilialFilial.builder()
                .grupoFilial(grupo)
                .filial(filial)
                .build();

        return vinculoRepository.save(vinculo);
    }

    @Transactional
    public void deletarVinculo(Long grupoFilialId, Long vinculoId) {
        GrupoFilialFilial vinculo = vinculoRepository.findByIdAndGrupoFilial_Id(vinculoId, grupoFilialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vinculo GrupoFilialFilial nao encontrado. grupoFilialId=" + grupoFilialId + ", id=" + vinculoId
                ));
        vinculoRepository.delete(vinculo);
    }

    @Transactional(readOnly = true)
    public List<GrupoFilial> listarComFiliais() {
        return grupoFilialRepository.findAllWithFiliais();
    }

    @Transactional(readOnly = true)
    public GrupoFilial buscarPorIdComFiliais(Long id) {
        return grupoFilialRepository.findByIdWithFiliais(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoFilial nao encontrado. id=" + id));
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }
}
