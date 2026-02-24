package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.TempoDescarregamentoPalete;
import com.agendamento.bff.v1.domain.model.TipoCarga;
import com.agendamento.bff.v1.domain.model.TipoVeiculo;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.TempoDescarregamentoPaleteRepository;
import com.agendamento.bff.v1.repository.TipoCargaRepository;
import com.agendamento.bff.v1.repository.TipoVeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempoDescarregamentoPaleteService {

    private final TempoDescarregamentoPaleteRepository repository;
    private final TipoVeiculoRepository tipoVeiculoRepository;
    private final TipoCargaRepository tipoCargaRepository;

    @Transactional(readOnly = true)
    public List<TempoDescarregamentoPalete> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public TempoDescarregamentoPalete buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TempoDescarregamentoPalete nao encontrado. id=" + id));
    }

    @Transactional
    public TempoDescarregamentoPalete criar(TempoDescarregamentoPalete novo, Long tipoVeiculoId, Long tipoCargaId) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarMinuto(novo.getMinuto(), true);
        if (tipoVeiculoId == null) {
            throw new IllegalArgumentException("O campo 'tipoVeiculoId' e obrigatorio.");
        }
        if (tipoCargaId == null) {
            throw new IllegalArgumentException("O campo 'tipoCargaId' e obrigatorio.");
        }
        if (repository.existsByTipoVeiculo_IdAndTipoCarga_Id(tipoVeiculoId, tipoCargaId)) {
            throw new IllegalArgumentException("Ja existe tempo de descarregamento para este tipo de veiculo e tipo de carga.");
        }

        novo.setTipoVeiculo(buscarTipoVeiculo(tipoVeiculoId));
        novo.setTipoCarga(buscarTipoCarga(tipoCargaId));
        return repository.save(novo);
    }

    @Transactional
    public TempoDescarregamentoPalete atualizar(Long id, TempoDescarregamentoPalete dados, Long tipoVeiculoId, Long tipoCargaId) {
        TempoDescarregamentoPalete atual = buscarPorId(id);

        if (dados.getMinuto() != null) {
            validarMinuto(dados.getMinuto(), false);
            atual.setMinuto(dados.getMinuto());
        }

        if (tipoVeiculoId != null) {
            atual.setTipoVeiculo(buscarTipoVeiculo(tipoVeiculoId));
        }
        if (tipoCargaId != null) {
            atual.setTipoCarga(buscarTipoCarga(tipoCargaId));
        }

        if (atual.getTipoVeiculo() == null) {
            throw new IllegalArgumentException("O campo 'tipoVeiculoId' e obrigatorio.");
        }
        if (atual.getTipoCarga() == null) {
            throw new IllegalArgumentException("O campo 'tipoCargaId' e obrigatorio.");
        }
        if (atual.getMinuto() == null) {
            throw new IllegalArgumentException("O campo 'minuto' e obrigatorio.");
        }

        return repository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        TempoDescarregamentoPalete entity = buscarPorId(id);
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

    private TipoVeiculo buscarTipoVeiculo(Long id) {
        return tipoVeiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoVeiculo nao encontrado. id=" + id));
    }

    private TipoCarga buscarTipoCarga(Long id) {
        return tipoCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCarga nao encontrado. id=" + id));
    }
}
