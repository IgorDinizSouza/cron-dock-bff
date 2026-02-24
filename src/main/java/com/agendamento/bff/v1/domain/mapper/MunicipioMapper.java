package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.MunicipioRequest;
import com.agendamento.bff.v1.domain.dto.response.MunicipioResponse;
import com.agendamento.bff.v1.domain.model.Municipio;

public class MunicipioMapper {

    private MunicipioMapper() {
    }

    public static Municipio toEntity(MunicipioRequest req) {
        return Municipio.builder()
                .descricao(req.descricao())
                .codigoIbge(req.codigoIbge())
                .build();
    }

    public static MunicipioResponse toResponse(Municipio entity) {
        return new MunicipioResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getCodigoIbge(),
                EstadoMapper.toResponse(entity.getEstado())
        );
    }
}
