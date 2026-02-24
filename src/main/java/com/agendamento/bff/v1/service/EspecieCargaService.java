package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.EspecieCarga;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.EspecieCargaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecieCargaService {

    private final EspecieCargaRepository especieCargaRepository;

    @Transactional(readOnly = true)
    public List<EspecieCarga> listar() {
        return especieCargaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EspecieCarga buscarPorId(Long id) {
        return especieCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EspecieCarga nao encontrada. id=" + id));
    }

    @Transactional
    public EspecieCarga criar(EspecieCarga nova) {
        if (nova.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDescricaoObrigatoria(nova.getDescricao());
        if (especieCargaRepository.existsByDescricaoIgnoreCase(nova.getDescricao())) {
            throw new IllegalArgumentException("Ja existe especie de carga com esta descricao.");
        }

        return especieCargaRepository.save(nova);
    }

    @Transactional
    public EspecieCarga atualizar(Long id, EspecieCarga dados) {
        EspecieCarga atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricaoObrigatoria(dados.getDescricao());
            if (especieCargaRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe especie de carga com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getAtivo() != null) {
            atual.setAtivo(dados.getAtivo());
        }

        return especieCargaRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        EspecieCarga especieCarga = buscarPorId(id);
        especieCargaRepository.delete(especieCarga);
    }

    private void validarDescricaoObrigatoria(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
        if (descricao.length() > 30) {
            throw new IllegalArgumentException("O campo 'descricao' deve ter no maximo 30 caracteres.");
        }
    }
}
