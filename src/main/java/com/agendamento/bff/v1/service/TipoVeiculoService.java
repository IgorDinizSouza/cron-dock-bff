package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.TipoVeiculo;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.TipoVeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoVeiculoService {

    private final TipoVeiculoRepository tipoVeiculoRepository;

    @Transactional(readOnly = true)
    public List<TipoVeiculo> listar() {
        return tipoVeiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoVeiculo buscarPorId(Long id) {
        return tipoVeiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoVeiculo nao encontrado. id=" + id));
    }

    @Transactional
    public TipoVeiculo criar(TipoVeiculo novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDados(novo);
        if (tipoVeiculoRepository.existsByNomeIgnoreCase(novo.getNome())) {
            throw new IllegalArgumentException("Ja existe tipo de veiculo com este nome.");
        }
        return tipoVeiculoRepository.save(novo);
    }

    @Transactional
    public TipoVeiculo atualizar(Long id, TipoVeiculo dados) {
        TipoVeiculo atual = buscarPorId(id);

        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            if (tipoVeiculoRepository.existsByNomeIgnoreCaseAndIdNot(dados.getNome(), id)) {
                throw new IllegalArgumentException("Ja existe tipo de veiculo com este nome.");
            }
            atual.setNome(dados.getNome());
        }

        if (dados.getQuantidadeMaximaPaletes() != null) {
            atual.setQuantidadeMaximaPaletes(dados.getQuantidadeMaximaPaletes());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return tipoVeiculoRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        TipoVeiculo tipoVeiculo = buscarPorId(id);
        tipoVeiculoRepository.delete(tipoVeiculo);
    }

    private void validarDados(TipoVeiculo tipoVeiculo) {
        if (tipoVeiculo.getNome() == null || tipoVeiculo.getNome().isBlank()) {
            throw new IllegalArgumentException("O campo 'nome' e obrigatorio.");
        }
    }
}
