package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.TipoCarga;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.TipoCargaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCargaService {

    private final TipoCargaRepository tipoCargaRepository;

    @Transactional(readOnly = true)
    public List<TipoCarga> listar() {
        return tipoCargaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoCarga buscarPorId(Long id) {
        return tipoCargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCarga nao encontrado. id=" + id));
    }

    @Transactional
    public TipoCarga criar(TipoCarga novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validar(novo);
        if (tipoCargaRepository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe tipo de carga com esta descricao.");
        }

        return tipoCargaRepository.save(novo);
    }

    @Transactional
    public TipoCarga atualizar(Long id, TipoCarga dados) {
        TipoCarga atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (tipoCargaRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe tipo de carga com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getMinSku() != null) {
            if (dados.getMinSku() < 0) {
                throw new IllegalArgumentException("O campo 'minSku' nao pode ser negativo.");
            }
            atual.setMinSku(dados.getMinSku());
        }

        if (dados.getMaxSku() != null) {
            if (dados.getMaxSku() < 0) {
                throw new IllegalArgumentException("O campo 'maxSku' nao pode ser negativo.");
            }
            atual.setMaxSku(dados.getMaxSku());
        }

        validarFaixaSku(atual.getMinSku(), atual.getMaxSku());
        return tipoCargaRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        TipoCarga tipoCarga = buscarPorId(id);
        tipoCargaRepository.delete(tipoCarga);
    }

    private void validar(TipoCarga tipoCarga) {
        validarDescricao(tipoCarga.getDescricao());

        if (tipoCarga.getMinSku() == null) {
            throw new IllegalArgumentException("O campo 'minSku' e obrigatorio.");
        }
        if (tipoCarga.getMaxSku() == null) {
            throw new IllegalArgumentException("O campo 'maxSku' e obrigatorio.");
        }
        if (tipoCarga.getMinSku() < 0) {
            throw new IllegalArgumentException("O campo 'minSku' nao pode ser negativo.");
        }
        if (tipoCarga.getMaxSku() < 0) {
            throw new IllegalArgumentException("O campo 'maxSku' nao pode ser negativo.");
        }

        validarFaixaSku(tipoCarga.getMinSku(), tipoCarga.getMaxSku());
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }

    private void validarFaixaSku(Integer minSku, Integer maxSku) {
        if (minSku > maxSku) {
            throw new IllegalArgumentException("O campo 'minSku' nao pode ser maior que 'maxSku'.");
        }
    }
}
