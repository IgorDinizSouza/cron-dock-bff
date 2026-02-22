package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.model.Filial;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.FilialRepository;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilialService {

    private final FilialRepository filialRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;

    @Transactional(readOnly = true)
    public List<Filial> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return filialRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Filial buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long filialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return filialRepository.findByIdAndGrupoEmpresarialId(filialId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Filial nao encontrada. grupoEmpresarialId=" + grupoEmpresarialId + ", filialId=" + filialId
                ));
    }

    @Transactional
    public Filial criar(Filial nova, Long grupoEmpresarialId) {
        if (nova.getId() == null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' e obrigatorio.");
        }
        if (filialRepository.existsById(nova.getId())) {
            throw new IllegalArgumentException("Ja existe filial com este id.");
        }

        validarDadosFilial(nova);
        validarCnpjDuplicado(grupoEmpresarialId, nova.getCnpj(), null);
        nova.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));

        if (nova.getStatus() == null) {
            nova.setStatus(Status.ATIVO);
        }

        return filialRepository.save(nova);
    }

    @Transactional
    public Filial atualizar(Long id, Filial dados, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Filial atual = filialRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Filial nao encontrada. grupoEmpresarialId=" + grupoEmpresarialId + ", filialId=" + id
                ));

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) atual.setDescricao(dados.getDescricao());
        if (dados.getCnpj() != null && !dados.getCnpj().isBlank()) {
            validarCnpjDuplicado(grupoEmpresarialId, dados.getCnpj(), id);
            atual.setCnpj(dados.getCnpj());
        }
        if (dados.getEndereco() != null) atual.setEndereco(dados.getEndereco());
        if (dados.getBairro() != null) atual.setBairro(dados.getBairro());
        if (dados.getCodigoIbgeCidade() != null) atual.setCodigoIbgeCidade(dados.getCodigoIbgeCidade());
        if (dados.getUf() != null) atual.setUf(dados.getUf());
        if (dados.getCep() != null) atual.setCep(dados.getCep());
        if (dados.getCd() != null) atual.setCd(dados.getCd());
        if (dados.getWms() != null) atual.setWms(dados.getWms());
        if (dados.getFlagRegional() != null) atual.setFlagRegional(dados.getFlagRegional());
        if (dados.getDescricaoRegional() != null) atual.setDescricaoRegional(dados.getDescricaoRegional());
        if (dados.getStatus() != null) atual.setStatus(dados.getStatus());

        atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
        if (dados.getCnpj() == null || dados.getCnpj().isBlank()) {
            validarCnpjDuplicado(grupoEmpresarialId, atual.getCnpj(), id);
        }

        return filialRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }
        Filial filial = filialRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Filial nao encontrada. grupoEmpresarialId=" + grupoEmpresarialId + ", filialId=" + id
                ));
        filialRepository.delete(filial);
    }

    private void validarDadosFilial(Filial filial) {
        if (filial.getDescricao() == null || filial.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
        }
        if (filial.getCnpj() == null || filial.getCnpj().isBlank()) {
            throw new IllegalArgumentException("O campo 'cnpj' e obrigatorio.");
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
                .orElseThrow(() -> new ResourceNotFoundException("Grupo empresarial nao encontrado. id=" + grupoEmpresarialId));
    }

    private void validarCnpjDuplicado(Long grupoEmpresarialId, String cnpj, Long filialIdIgnorado) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }
        boolean existe = filialIdIgnorado == null
                ? filialRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCase(grupoEmpresarialId, cnpj)
                : filialRepository.existsByGrupoEmpresarialIdAndCnpjIgnoreCaseAndIdNot(grupoEmpresarialId, cnpj, filialIdIgnorado);
        if (existe) {
            throw new IllegalArgumentException("Ja existe filial com este cnpj no grupo empresarial informado.");
        }
    }
}
