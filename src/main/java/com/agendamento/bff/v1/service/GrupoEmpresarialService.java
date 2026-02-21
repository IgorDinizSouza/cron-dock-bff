package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoEmpresarialService {

    private final GrupoEmpresarialRepository repository;

    @Transactional(readOnly = true)
    public List<GrupoEmpresarial> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public GrupoEmpresarial buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Grupo empresarial não encontrado. id=" + id));
    }

    @Transactional
    public GrupoEmpresarial criar(GrupoEmpresarial novo) {

        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        if (novo.getCnpj() == null || novo.getCnpj().isBlank()) {
            throw new IllegalArgumentException("O campo 'cnpj' é obrigatório.");
        }

        repository.findByCnpj(novo.getCnpj())
                .ifPresent(g -> {
                    throw new IllegalArgumentException("Já existe grupo empresarial com este CNPJ.");
                });

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        } else if (novo.getStatus() != Status.INATIVO && novo.getStatus() != Status.ATIVO) {
            throw new IllegalArgumentException("Campo 'ativo' deve ser 0 ou 1.");
        }

        if (novo.getDescricao() == null || novo.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' é obrigatório.");
        }

        return repository.save(novo);
    }

    @Transactional
    public GrupoEmpresarial atualizar(Long id, GrupoEmpresarial dados) {

        GrupoEmpresarial atual = buscarPorId(id);

        if (dados.getCnpj() != null && !dados.getCnpj().isBlank()) {

            String cnpjNovo = dados.getCnpj();
            String cnpjAtual = atual.getCnpj();

            if (!cnpjNovo.equals(cnpjAtual)) {

                repository.findByCnpj(cnpjNovo)
                        .ifPresent(g -> {
                            throw new IllegalArgumentException("Já existe grupo empresarial com este CNPJ.");
                        });
            }

            atual.setCnpj(cnpjNovo);
        }

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) {
            atual.setDescricao(dados.getDescricao());
        }

        if (dados.getStatus() != null) {
            if (dados.getStatus() != Status.INATIVO && dados.getStatus() != Status.ATIVO) {
                throw new IllegalArgumentException("Campo 'ativo' deve ser 0 ou 1.");
            }
            atual.setStatus(dados.getStatus());
        }

        return repository.save(atual);
    }

    @Transactional
    public GrupoEmpresarial alterarStatus(Long id, Status status) {

        if (status == null || (status != Status.INATIVO && status != Status.ATIVO)) {
            throw new IllegalArgumentException("Campo 'ativo' deve ser 0 ou 1.");
        }

        GrupoEmpresarial atual = buscarPorId(id);
        atual.setStatus(status);

        return repository.save(atual);
    }
}
