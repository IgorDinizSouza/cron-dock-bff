package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.CargaProduto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CargaProdutoRepository extends JpaRepository<CargaProduto, Long> {

    @EntityGraph(attributePaths = {"carga", "produto", "pedido"})
    List<CargaProduto> findAllByCargaId(Long cargaId);

    @EntityGraph(attributePaths = {"carga", "produto", "pedido"})
    Optional<CargaProduto> findByIdAndCargaId(Long id, Long cargaId);

    boolean existsByCargaIdAndProdutoIdAndPedidoId(Long cargaId, Long produtoId, Long pedidoId);
}
