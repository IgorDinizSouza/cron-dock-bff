package com.agendamento.bff.v1.service;

import com.agendamento.bff.v1.domain.enumeration.Status;
import com.agendamento.bff.v1.domain.dto.request.EmbalagemRequest;
import com.agendamento.bff.v1.domain.model.Comprador;
import com.agendamento.bff.v1.domain.model.Embalagem;
import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import com.agendamento.bff.v1.domain.model.Produto;
import com.agendamento.bff.v1.exception.ResourceNotFoundException;
import com.agendamento.bff.v1.repository.CompradorRepository;
import com.agendamento.bff.v1.repository.EmbalagemRepository;
import com.agendamento.bff.v1.repository.GrupoEmpresarialRepository;
import com.agendamento.bff.v1.repository.ProdutoRepository;
import com.agendamento.bff.v1.domain.mapper.EmbalagemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoEmpresarialRepository grupoEmpresarialRepository;
    private final CompradorRepository compradorRepository;
    private final EmbalagemRepository embalagemRepository;

    @Transactional(readOnly = true)
    public List<Produto> listarPorGrupoEmpresarial(Long grupoEmpresarialId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return produtoRepository.findAllByGrupoEmpresarialId(grupoEmpresarialId);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorGrupoEmpresarialEId(Long grupoEmpresarialId, Long produtoId) {
        validarGrupoEmpresarialExiste(grupoEmpresarialId);
        return produtoRepository.findByIdAndGrupoEmpresarialId(produtoId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", produtoId=" + produtoId
                ));
    }

    @Transactional
    public Produto criar(Produto novo, Long grupoEmpresarialId, Long compradorId, List<EmbalagemRequest> embalagensReq) {
        if (novo.getId() == null) {
            throw new IllegalArgumentException("Ao criar, o campo 'id' e obrigatorio.");
        }
        if (produtoRepository.existsById(novo.getId())) {
            throw new IllegalArgumentException("Ja existe produto com este id.");
        }

        validarDadosProduto(novo);
        novo.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
        novo.setComprador(buscarCompradorOpcional(compradorId, grupoEmpresarialId));

        if (novo.getStatus() == null) {
            novo.setStatus(Status.ATIVO);
        }

        Produto salvo = produtoRepository.save(novo);
        sincronizarEmbalagens(salvo, grupoEmpresarialId, embalagensReq);
        return salvo;
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados, Long grupoEmpresarialId, Long compradorId, List<EmbalagemRequest> embalagensReq) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }

        Produto atual = produtoRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", produtoId=" + id
                ));

        if (dados.getDescricao() != null && !dados.getDescricao().isBlank()) atual.setDescricao(dados.getDescricao());
        if (dados.getComplemento() != null) atual.setComplemento(dados.getComplemento());
        if (dados.getLastro() != null) atual.setLastro(dados.getLastro());
        if (dados.getAltura() != null) atual.setAltura(dados.getAltura());
        if (dados.getPeso() != null) atual.setPeso(dados.getPeso());
        if (dados.getPesoLiquido() != null) atual.setPesoLiquido(dados.getPesoLiquido());
        if (dados.getComposicao() != null) atual.setComposicao(dados.getComposicao());
        if (dados.getDataCadastro() != null) atual.setDataCadastro(dados.getDataCadastro());
        if (dados.getStatus() != null) atual.setStatus(dados.getStatus());

        atual.setGrupoEmpresarial(buscarGrupoEmpresarial(grupoEmpresarialId));
        if (compradorId != null) {
            atual.setComprador(buscarCompradorOpcional(compradorId, grupoEmpresarialId));
        }

        Produto salvo = produtoRepository.save(atual);
        if (embalagensReq != null) {
            sincronizarEmbalagens(salvo, grupoEmpresarialId, embalagensReq);
        }
        return salvo;
    }

    @Transactional
    public void deletar(Long id, Long grupoEmpresarialId) {
        if (grupoEmpresarialId == null) {
            throw new IllegalArgumentException("O campo 'grupoEmpresarialId' e obrigatorio.");
        }
        Produto produto = produtoRepository.findByIdAndGrupoEmpresarialId(id, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", produtoId=" + id
                ));
        produtoRepository.delete(produto);
    }

    private void validarDadosProduto(Produto produto) {
        if (produto.getDescricao() == null || produto.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' e obrigatorio.");
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

    private Comprador buscarCompradorOpcional(Long compradorId, Long grupoEmpresarialId) {
        if (compradorId == null) {
            return null;
        }
        return compradorRepository.findByIdAndGrupoEmpresarialId(compradorId, grupoEmpresarialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comprador nao encontrado. grupoEmpresarialId=" + grupoEmpresarialId + ", compradorId=" + compradorId
                ));
    }

    private void sincronizarEmbalagens(Produto produto, Long grupoEmpresarialId, List<EmbalagemRequest> embalagensReq) {
        List<Embalagem> existentes = embalagemRepository.findAllByGrupoEmpresarialIdAndProdutoId(grupoEmpresarialId, produto.getId());
        Map<Long, Embalagem> existentesPorId = new HashMap<>();
        for (Embalagem existente : existentes) {
            existentesPorId.put(existente.getId(), existente);
        }

        if (embalagensReq == null) {
            return;
        }

        List<Embalagem> paraSalvar = new java.util.ArrayList<>();
        Set<Long> idsRecebidos = new HashSet<>();

        for (EmbalagemRequest req : embalagensReq) {
            if (req == null) {
                continue;
            }
            if (req.id() == null) {
                throw new IllegalArgumentException("O campo 'id' da embalagem e obrigatorio.");
            }

            if (!idsRecebidos.add(req.id())) {
                throw new IllegalArgumentException("Ha embalagens duplicadas no payload. id=" + req.id());
            }

            Embalagem embalagem = existentesPorId.get(req.id());
            if (embalagem == null) {
                if (embalagemRepository.existsById(req.id())) {
                    throw new IllegalArgumentException("Ja existe embalagem com este id vinculada a outro produto.");
                }
                embalagem = EmbalagemMapper.toEntity(req);
            } else {
                embalagem.setDigito(req.digito());
                embalagem.setCodigoBarra(req.codigoBarra());
                embalagem.setSigla(req.sigla());
                embalagem.setMultiplicador1(req.multiplicador1());
                embalagem.setMultiplicador2(req.multiplicador2());
                if (req.status() != null) {
                    embalagem.setStatus(req.status());
                }
            }

            embalagem.setGrupoEmpresarial(produto.getGrupoEmpresarial());
            embalagem.setProduto(produto);
            if (embalagem.getStatus() == null) {
                embalagem.setStatus(Status.ATIVO);
            }
            paraSalvar.add(embalagem);
        }

        for (Embalagem existente : existentes) {
            if (!idsRecebidos.contains(existente.getId())) {
                embalagemRepository.delete(existente);
            }
        }

        if (!paraSalvar.isEmpty()) {
            embalagemRepository.saveAll(paraSalvar);
        }
    }
}
