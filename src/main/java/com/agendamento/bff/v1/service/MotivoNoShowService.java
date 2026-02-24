package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.MotivoNoShow;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.MotivoNoShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotivoNoShowService {

    private final MotivoNoShowRepository repository;

    @Transactional(readOnly = true)
    public List<MotivoNoShow> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public MotivoNoShow buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MotivoNoShow nao encontrado. id=" + id));
    }

    @Transactional
    public MotivoNoShow criar(MotivoNoShow novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(novo.getDescricao());
        if (repository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe motivo de no show com esta descricao.");
        }
        return repository.save(novo);
    }

    @Transactional
    public MotivoNoShow atualizar(Long id, MotivoNoShow dados) {
        MotivoNoShow atual = buscarPorId(id);
        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (repository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe motivo de no show com esta descricao.");
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
