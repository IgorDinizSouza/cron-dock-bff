package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.domain.model.Regional;
import com.agendamento.bff.v1.domain.model.RegionalFilial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.FilialRepository;
import com.agendamento.bff.v1.repository.RegionalFilialRepository;
import com.agendamento.bff.v1.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionalService {

    private final RegionalRepository regionalRepository;
    private final RegionalFilialRepository regionalFilialRepository;
    private final FilialRepository filialRepository;

    @Transactional(readOnly = true)
    public List<Regional> listar() {
        return regionalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Regional buscarPorId(Long id) {
        return regionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regional nao encontrada. id=" + id));
    }

    @Transactional
    public Regional criar(Regional nova) {
        if (nova.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(nova.getDescricao());
        if (regionalRepository.existsByDescricaoIgnoreCase(nova.getDescricao())) {
            throw new IllegalArgumentException("Ja existe regional com esta descricao.");
        }
        return regionalRepository.save(nova);
    }

    @Transactional
    public Regional atualizar(Long id, Regional dados) {
        Regional atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (regionalRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe regional com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return regionalRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        Regional regional = buscarPorId(id);
        regionalRepository.delete(regional);
    }

    @Transactional
    public RegionalFilial criarRegionalFilial(Long regionalId, Long filialId) {
        if (regionalId == null) {
            throw new IllegalArgumentException("O campo 'regionalId' e obrigatorio.");
        }
        if (filialId == null) {
            throw new IllegalArgumentException("O campo 'filialId' e obrigatorio.");
        }
        if (regionalFilialRepository.existsByRegional_IdAndFilial_Id(regionalId, filialId)) {
            throw new IllegalArgumentException("Esta filial ja esta vinculada a regional informada.");
        }

        Regional regional = buscarPorId(regionalId);
        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial nao encontrada. id=" + filialId));

        RegionalFilial regionalFilial = RegionalFilial.builder()
                .regional(regional)
                .filial(filial)
                .build();

        return regionalFilialRepository.save(regionalFilial);
    }

    @Transactional(readOnly = true)
    public List<Regional> listarComFiliais() {
        return regionalRepository.findAllWithRegionaisFiliais();
    }

    @Transactional(readOnly = true)
    public Regional buscarPorIdComFiliais(Long id) {
        return regionalRepository.findByIdWithRegionaisFiliais(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regional nao encontrada. id=" + id));
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }
}
