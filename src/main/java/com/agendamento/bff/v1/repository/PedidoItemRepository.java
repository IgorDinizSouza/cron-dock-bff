package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

    @Query(value = """
            select *
            from pedido_item pi
            where pi.pedido_id = :pedidoId
            """, nativeQuery = true)
    List<PedidoItem> buscarItensPorPedidoId(@Param("pedidoId") Long pedidoId);
}
