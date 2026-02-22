package com.agendamento.bff.v1.controller;

import com.agendamento.bff.v1.domain.dto.request.ProdutoRequest;
import com.agendamento.bff.v1.domain.dto.response.EmbalagemResponse;
import com.agendamento.bff.v1.domain.dto.response.ProdutoResponse;
import com.agendamento.bff.v1.domain.mapper.EmbalagemMapper;
import com.agendamento.bff.v1.domain.mapper.ProdutoMapper;
import com.agendamento.bff.v1.domain.model.Produto;
import com.agendamento.bff.v1.repository.EmbalagemRepository;
import com.agendamento.bff.v1.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final EmbalagemRepository embalagemRepository;

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorGrupoEmpresarial(@PathVariable Long grupoEmpresarialId) {
        List<Produto> produtos = service.listarPorGrupoEmpresarial(grupoEmpresarialId);
        List<Long> produtoIds = produtos.stream().map(Produto::getId).toList();

        Map<Long, List<EmbalagemResponse>> embalagensPorProduto = produtoIds.isEmpty()
                ? Collections.emptyMap()
                : embalagemRepository.findAllByGrupoEmpresarialIdAndProdutoIdIn(grupoEmpresarialId, produtoIds)
                .stream()
                .collect(Collectors.groupingBy(
                        emb -> emb.getProduto().getId(),
                        Collectors.mapping(EmbalagemMapper::toResponse, Collectors.toList())
                ));

        List<ProdutoResponse> resp = produtos.stream()
                .map(produto -> ProdutoMapper.toResponse(
                        produto,
                        embalagensPorProduto.getOrDefault(produto.getId(), List.of())
                ))
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/grupo-empresarial/{grupoEmpresarialId}/{produtoId}")
    public ResponseEntity<ProdutoResponse> buscarPorGrupoEmpresarialEProdutoId(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long produtoId
    ) {
        Produto produto = service.buscarPorGrupoEmpresarialEId(grupoEmpresarialId, produtoId);
        List<EmbalagemResponse> embalagens = embalagemRepository
                .findAllByGrupoEmpresarialIdAndProdutoId(grupoEmpresarialId, produtoId)
                .stream()
                .map(EmbalagemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ProdutoMapper.toResponse(produto, embalagens));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest req) {
        Produto entity = ProdutoMapper.toEntity(req);
        Produto salvo = service.criar(entity, req.grupoEmpresarialId(), req.compradorId(), req.embalagens());
        List<EmbalagemResponse> embalagens = embalagemRepository
                .findAllByGrupoEmpresarialIdAndProdutoId(req.grupoEmpresarialId(), salvo.getId())
                .stream()
                .map(EmbalagemMapper::toResponse)
                .toList();

        return ResponseEntity
                .created(URI.create("/api/v1/produto/" + salvo.getId()))
                .body(ProdutoMapper.toResponse(salvo, embalagens));
    }

    @PutMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long grupoEmpresarialId,
            @PathVariable Long id,
            @RequestBody ProdutoRequest req
    ) {
        Produto dados = ProdutoMapper.toEntity(req);
        Produto atualizado = service.atualizar(id, dados, grupoEmpresarialId, req.compradorId(), req.embalagens());
        List<EmbalagemResponse> embalagens = embalagemRepository
                .findAllByGrupoEmpresarialIdAndProdutoId(grupoEmpresarialId, atualizado.getId())
                .stream()
                .map(EmbalagemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ProdutoMapper.toResponse(atualizado, embalagens));
    }

    @DeleteMapping("/grupo-empresarial/{grupoEmpresarialId}/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long grupoEmpresarialId, @PathVariable Long id) {
        service.deletar(id, grupoEmpresarialId);
        return ResponseEntity.noContent().build();
    }
}
