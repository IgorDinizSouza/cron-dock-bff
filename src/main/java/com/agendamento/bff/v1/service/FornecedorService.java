package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.Fornecedor;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.FornecedorRepository;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;

    @Transactional(readOnly = true)
    public List<Fornecedor> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return fornecedorRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long fornecedorId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return fornecedorRepository.findByIdAndGrupoEmpresarialId(fornecedorId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", fornecedorId=" + fornecedorId
                ));
    }

    @Transactional
    public Fornecedor criar(Fornecedor novo, Long grupoEmpresarialId) {
        if (novo.getId() != null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' deve ser nulo.");
        }

        validarDadosFornecedor(novo);
        validarCnpjDuplicado(grupoEmpresarialId, novo.getCnpj(), null);

        GrupoEmpresarial grupoEmpresarial = buscarGrupoEmpresarial(grupoEmpresarialId);
        novo.setGrupoEmpresarial(grupoEmpresarial);

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        }

        // dataCriacao e controlada pela entidade (@PrePersist), nao deve vir do request.
        return fornecedorRepository.save(novo);
    }

    @Transactional
    public Fornecedor atualizar(Long id, Fornecedor dados, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Fornecedor atual = fornecedorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", fornecedorId=" + id
                ));

        if (dados.getCnpj() != null && !dados.getCnpj().isBlank()) {
            validarCnpjDuplicado(grupoEmpresarialId, dados.getCnpj(), id);
            atual.setCnpj(dados.getCnpj());
        }

        if (dados.getRazaoSocial() != null && !dados.getRazaoSocial().isBlank()) {
            atual.setRazaoSocial(dados.getRazaoSocial());
        }

        if (dados.getCidade() != null) {
            atual.setCidade(dados.getCidade());
        }

        if (dados.getUf() != null) {
            atual.setUf(dados.getUf());
        }

        if (dados.getDataCadastro() != null) {
            atual.setDataCadastro(dados.getDataCadastro());
        }

        if (dados.getStatus() != null) {
            atual.setStatus(dados.getStatus());
        }

        if (grupoEmpresarialId != null) {
            atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
            if (dados.getCnpj() == null || dados.getCnpj().isBlank()) {
                validarCnpjDuplicado(grupoEmpresarialId, atual.getCnpj(), id);
            }
        }

        // Nunca altera dataCriacao no update.
        return fornecedorRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Fornecedor fornecedor = fornecedorRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId
                                + ", fornecedorId=" + id
                ));
        fornecedorRepository.delete(fornecedor);
    }

    private void validarDadosFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().isBlank()) {
            throw new IllegalArgumentException("O campo 'cnpj' e obrigatorio.");
        }
        if (fornecedor.getRazaoSocial() == null || fornecedor.getRazaoSocial().isBlank()) {
            throw new IllegalArgumentException("O campo 'razaoSocial' e obrigatorio.");
        }
    }

    private void validarGrupoEmpresarialExiste(Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        if (!grupoEmpresarialRepository.existsById(grupoEmpresarialId)) {
            throw new ResourceNotFoundException("Grupo empresarial nao encontrado. id=" + grupoEmpresarialId);
        }
    }

    private GrupoEmpresarial buscarGrupoEmpresarial(Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }
        return grupoEmpresarialRepository.findById(grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grupo empresarial nao encontrado. id=" + grupoEmpresarialId
                ));
    }

    private void validarCnpjDuplicado(Long grupoEmpresarialId, String cnpj, Long fornecedorIdIgnorado) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        boolean existe = fornecedorIdIgnorado == null
                ? fornecedorRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCase(grupoEmpresarialId, cnpj)
                : fornecedorRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCaseAndIdNot(
                grupoEmpresarialId,
                cnpj,
                fornecedorIdIgnorado
        );

        if (existe) {
            throw new IllegalArgumentException("Ja existe fornecedor com este cnpj no grupo empresarial informado.");
        }
    }
}
