package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Estado;
import com.agendamento.bff.v1.domain.model.Municipio;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.EstadoRepository;
import com.agendamento.bff.v1.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipioService {

    private final MunicipioRepository municipioRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<Municipio> listar() {
        return municipioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Municipio buscarPorId(Long id) {
        return municipioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Municipio nao encontrado. id=" + id));
    }

    @Transactional
    public Municipio criar(Municipio novo, Long estadoId) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDados(novo, estadoId);
        if (municipioRepository.existsByCodigoIbgeIgnoreCase(novo.getCodigoIbge())) {
            throw new IllegalArgumentException("Ja existe municipio com este codigo IBGE.");
        }

        novo.setEstado(buscarEstado(estadoId));
        return municipioRepository.save(novo);
    }

    @Transactional
    public Municipio atualizar(Long id, Municipio dados, Long estadoId) {
        Municipio atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getCodigoIbge() != null) {
            validarCodigoIbge(dados.getCodigoIbge());
            if (municipioRepository.existsByCodigoIbgeIgnoreCaseAndIdNot(dados.getCodigoIbge(), id)) {
                throw new IllegalArgumentException("Ja existe municipio com este codigo IBGE.");
            }
            atual.setCodigoIbge(dados.getCodigoIbge());
        }

        if (estadoId != null) {
            atual.setEstado(buscarEstado(estadoId));
        }

        if (atual.getEstado() == null) {
            throw new IllegalArgumentException("O campo 'estadoId' e obrigatorio.");
        }

        return municipioRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        Municipio municipio = buscarPorId(id);
        municipioRepository.delete(municipio);
    }

    private void validarDados(Municipio municipio, Long estadoId) {
        validarDescricao(municipio.getDescricao());
        validarCodigoIbge(municipio.getCodigoIbge());
        if (estadoId == null) {
            throw new IllegalArgumentException("O campo 'estadoId' e obrigatorio.");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }

    private void validarCodigoIbge(String codigoIbge) {
        if (codigoIbge == null || codigoIbge.isBlank()) {
            throw new IllegalArgumentException("O campo 'codigoIbge' e obrigatorio.");
        }
    }

    private Estado buscarEstado(Long estadoId) {
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado nao encontrado. id=" + estadoId));
    }
}
