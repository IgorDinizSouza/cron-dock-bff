package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
            select p
            from Pedido p
            join fetch p.fornecedor f
            join fetch p.comprador c
            join fetch p.filial fi
            where p.grupoEmpresarial.id = :grupoEmpresarialId
            """)
    List<Pedido> buscarPedidosPorGrupoEmpresarial(@Param("grupoEmpresarialId") Long grupoEmpresarialId);

    @Query(value = """
            select *
            from pedido p
            where p.grupo_empresarial_id = :grupoEmpresarialId
              and cast(p.id_pedido as text) = :numeroPedido
            """, nativeQuery = true)
    java.util.Optional<Pedido> buscarPedidoPorGrupoEmpresarialENumeroPedido(
            @Param("grupoEmpresarialId") Long grupoEmpresarialId,
            @Param("numeroPedido") String numeroPedido
    );
}
