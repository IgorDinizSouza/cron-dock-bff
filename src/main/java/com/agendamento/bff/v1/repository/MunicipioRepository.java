package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Municipio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    @Override
    @EntityGraph(attributePaths = {"estado"})
    List<Municipio> findAll();

    @Override
    @EntityGraph(attributePaths = {"estado"})
    Optional<Municipio> findById(Long id);

    boolean existsByCodigoIbgeIgnoreCase(String codigoIbge);

    boolean existsByCodigoIbgeIgnoreCaseAndIdNot(String codigoIbge, Long id);
}
