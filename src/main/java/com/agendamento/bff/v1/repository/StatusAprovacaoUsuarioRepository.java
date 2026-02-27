package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.StatusAprovacaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusAprovacaoUsuarioRepository extends JpaRepository<StatusAprovacaoUsuario, Long> {
}
