package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Estado;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estado buscarPorId(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado nao encontrado. id=" + id));
    }

    @Transactional(readOnly = true)
    public Estado buscarPorDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
        return estadoRepository.findByDescricaoIgnoreCase(descricao)
                .orElseThrow(() -> new ResourceNotFoundException("Estado nao encontrado. descricao=" + descricao));
    }
}
