package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Carga;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CargaRepository extends JpaRepository<Carga, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "statusCarga",
            "tipoCarga",
            "tipoVeiculo",
            "transportador",
            "especieCarga",
            "usuarioSolicitante",
            "usuarioAprovador"
    })
    List<Carga> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "statusCarga",
            "tipoCarga",
            "tipoVeiculo",
            "transportador",
            "especieCarga",
            "usuarioSolicitante",
            "usuarioAprovador"
    })
    Optional<Carga> findById(Long id);
}
