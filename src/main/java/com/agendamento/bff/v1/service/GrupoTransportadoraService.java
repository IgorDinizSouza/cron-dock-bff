package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.GrupoTransportadora;
import com.agendamento.bff.v1.domain.model.GrupoTransportadoraTransportadora;
import com.agendamento.bff.v1.domain.model.Transportador;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.GrupoTransportadoraRepository;
import com.agendamento.bff.v1.repository.GrupoTransportadoraTransportadoraRepository;
import com.agendamento.bff.v1.repository.TransportadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoTransportadoraService {

    private final GrupoTransportadoraRepository grupoTransportadoraRepository;
    private final GrupoTransportadoraTransportadoraRepository vinculoRepository;
    private final TransportadorRepository transportadorRepository;

    @Transactional(readOnly = true)
    public List<GrupoTransportadora> listar() {
        return grupoTransportadoraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GrupoTransportadora buscarPorId(Long id) {
        return grupoTransportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoTransportadora nao encontrado. id=" + id));
    }

    @Transactional
    public GrupoTransportadora criar(GrupoTransportadora novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(novo.getDescricao());
        if (grupoTransportadoraRepository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe grupo de transportadora com esta descricao.");
        }
        return grupoTransportadoraRepository.save(novo);
    }

    @Transactional
    public GrupoTransportadora atualizar(Long id, GrupoTransportadora dados) {
        GrupoTransportadora atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (grupoTransportadoraRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe grupo de transportadora com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return grupoTransportadoraRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        grupoTransportadoraRepository.delete(buscarPorId(id));
    }

    @Transactional
    public GrupoTransportadoraTransportadora criarVinculo(Long grupoTransportadoraId, Long transportadoraId) {
        if (grupoTransportadoraId == null) {
            throw new IllegalArgumentException("O campo 'grupoTransportadoraId' e obrigatorio.");
        }
        if (transportadoraId == null) {
            throw new IllegalArgumentException("O campo 'transportadoraId' e obrigatorio.");
        }
        if (vinculoRepository.existsByGrupoTransportadora_IdAndTransportadora_Id(grupoTransportadoraId, transportadoraId)) {
            throw new IllegalArgumentException("Esta transportadora ja esta vinculada ao grupo informado.");
        }

        GrupoTransportadora grupo = buscarPorId(grupoTransportadoraId);
        Transportador transportadora = transportadorRepository.findById(transportadoraId)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora nao encontrada. id=" + transportadoraId));

        GrupoTransportadoraTransportadora vinculo = GrupoTransportadoraTransportadora.builder()
                .grupoTransportadora(grupo)
                .transportadora(transportadora)
                .build();

        return vinculoRepository.save(vinculo);
    }

    @Transactional(readOnly = true)
    public List<GrupoTransportadora> listarComTransportadoras() {
        return grupoTransportadoraRepository.findAllWithTransportadoras();
    }

    @Transactional(readOnly = true)
    public GrupoTransportadora buscarPorIdComTransportadoras(Long id) {
        return grupoTransportadoraRepository.findByIdWithTransportadoras(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoTransportadora nao encontrado. id=" + id));
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }
}
