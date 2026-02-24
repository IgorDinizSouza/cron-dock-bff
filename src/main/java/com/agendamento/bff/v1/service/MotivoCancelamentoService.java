package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.MotivoCancelamento;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.MotivoCancelamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotivoCancelamentoService {

    private final MotivoCancelamentoRepository repository;

    @Transactional(readOnly = true)
    public List<MotivoCancelamento> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public MotivoCancelamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MotivoCancelamento nao encontrado. id=" + id));
    }

    @Transactional
    public MotivoCancelamento criar(MotivoCancelamento novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(novo.getDescricao());
        if (repository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe motivo de cancelamento com esta descricao.");
        }
        return repository.save(novo);
    }

    @Transactional
    public MotivoCancelamento atualizar(Long id, MotivoCancelamento dados) {
        MotivoCancelamento atual = buscarPorId(id);
        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (repository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe motivo de cancelamento com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }
        return repository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscarPorId(id));
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }
}
