package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.model.Fornecedor;
import com.agendamento.bff.v1.domain.model.GrupoFornecedor;
import com.agendamento.bff.v1.domain.model.GrupoFornecedorFornecedor;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.FornecedorRepository;
import com.agendamento.bff.v1.repository.GrupoFornecedorFornecedorRepository;
import com.agendamento.bff.v1.repository.GrupoFornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoFornecedorService {

    private final GrupoFornecedorRepository grupoFornecedorRepository;
    private final GrupoFornecedorFornecedorRepository grupoFornecedorFornecedorRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<GrupoFornecedor> listar() {
        return grupoFornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GrupoFornecedor buscarPorId(Long id) {
        return grupoFornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoFornecedor nao encontrado. id=" + id));
    }

    @Transactional
    public GrupoFornecedor criar(GrupoFornecedor novo) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }
        validarDescricao(novo.getDescricao());
        if (grupoFornecedorRepository.existsByDescricaoIgnoreCase(novo.getDescricao())) {
            throw new IllegalArgumentException("Ja existe grupo de fornecedor com esta descricao.");
        }
        return grupoFornecedorRepository.save(novo);
    }

    @Transactional
    public GrupoFornecedor atualizar(Long id, GrupoFornecedor dados) {
        GrupoFornecedor atual = buscarPorId(id);

        if (dados.getDescricao() != null) {
            validarDescricao(dados.getDescricao());
            if (grupoFornecedorRepository.existsByDescricaoIgnoreCaseAndIdNot(dados.getDescricao(), id)) {
                throw new IllegalArgumentException("Ja existe grupo de fornecedor com esta descricao.");
            }
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        return grupoFornecedorRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        grupoFornecedorRepository.delete(buscarPorId(id));
    }

    @Transactional
    public GrupoFornecedorFornecedor criarVinculo(Long grupoFornecedorId, Long fornecedorId) {
        if (grupoFornecedorId == null) {
            throw new IllegalArgumentException("O campo 'grupoFornecedorId' e obrigatorio.");
        }
        if (fornecedorId == null) {
            throw new IllegalArgumentException("O campo 'fornecedorId' e obrigatorio.");
        }
        if (grupoFornecedorFornecedorRepository.existsByGrupoFornecedor_IdAndFornecedor_Id(grupoFornecedorId, fornecedorId)) {
            throw new IllegalArgumentException("Este fornecedor ja esta vinculado ao grupo de fornecedor informado.");
        }

        GrupoFornecedor grupoFornecedor = buscarPorId(grupoFornecedorId);
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor nao encontrado. id=" + fornecedorId));

        GrupoFornecedorFornecedor vinculo = GrupoFornecedorFornecedor.builder()
                .grupoFornecedor(grupoFornecedor)
                .fornecedor(fornecedor)
                .build();

        return grupoFornecedorFornecedorRepository.save(vinculo);
    }

    @Transactional(readOnly = true)
    public List<GrupoFornecedor> listarComFornecedores() {
        return grupoFornecedorRepository.findAllWithFornecedores();
    }

    @Transactional(readOnly = true)
    public GrupoFornecedor buscarPorIdComFornecedores(Long id) {
        return grupoFornecedorRepository.findByIdWithFornecedores(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrupoFornecedor nao encontrado. id=" + id));
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
    }
}
