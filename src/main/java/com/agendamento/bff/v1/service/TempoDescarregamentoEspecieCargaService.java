package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.EspecieCarga;
import com.agendamento.bff.v1.domain.model.TempoDescarregamentoEspecieCarga;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.EspecieCargaRepository;
import com.agendamento.bff.v1.repository.TempoDescarregamentoEspecieCargaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempoDescarregamentoEspecieCargaService {

    private final TempoDescarregamentoEspecieCargaRepository repository;
    private final EspecieCargaRepository especieCargaRepository;

    @Transactional(readOnly = true)
    public List<TempoDescarregamentoEspecieCarga> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public TempoDescarregamentoEspecieCarga buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TempoDescarregamentoEspecieCarga nao encontrado. id=" + id));
    }

    @Transactional
    public TempoDescarregamentoEspecieCarga criar(TempoDescarregamentoEspecieCarga novo, Long especieCargaId) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarMinuto(novo.getMinuto(), true);
        if (especieCargaId == null) {
            throw new IllegalArgumentException("O campo 'especieCargaId' e obrigatorio.");
        }
        if (repository.existsByEspecieCarga_Id(especieCargaId)) {
            throw new IllegalArgumentException("Ja existe tempo de descarregamento para esta especie de carga.");
        }

        novo.setEspecieCarga(buscarEspecieCarga(especieCargaId));
        return repository.save(novo);
    }

    @Transactional
    public TempoDescarregamentoEspecieCarga atualizar(Long id, TempoDescarregamentoEspecieCarga dados, Long especieCargaId) {
        TempoDescarregamentoEspecieCarga atual = buscarPorId(id);

        if (dados.getMinuto() != null) {
            validarMinuto(dados.getMinuto(), false);
            atual.setMinuto(dados.getMinuto());
        }
        if (especieCargaId != null) {
            atual.setEspecieCarga(buscarEspecieCarga(especieCargaId));
        }

        if (atual.getEspecieCarga() == null) {
            throw new IllegalArgumentException("O campo 'especieCargaId' e obrigatorio.");
        }
        if (atual.getMinuto() == null) {
            throw new IllegalArgumentException("O campo 'minuto' e obrigatorio.");
        }

        return repository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        TempoDescarregamentoEspecieCarga entity = buscarPorId(id);
        repository.delete(entity);
    }

    private void validarMinuto(Integer minuto, boolean obrigatorio) {
        if (minuto == null) {
            if (obrigatorio) {
                throw new IllegalArgumentException("O campo 'minuto' e obrigatorio.");
            }
            return;
        }
        if (minuto < 0) {
            throw new IllegalArgumentException("O campo 'minuto' nao pode ser negativo.");
        }
    }

    private EspecieCarga buscarEspecieCarga(Long id) {
        return especieCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EspecieCarga nao encontrada. id=" + id));
    }
}
