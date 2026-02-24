package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.MotivoNoShowRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoNoShowResponse;
import com.agendamento.bff.v1.domain.model.MotivoNoShow;

public class MotivoNoShowMapper {

    private MotivoNoShowMapper() {
    }

    public static MotivoNoShow toEntity(MotivoNoShowRequest req) {
        return MotivoNoShow.builder()
                .descricao(req.descricao())
                .build();
    }

    public static MotivoNoShowResponse toResponse(MotivoNoShow entity) {
        return new MotivoNoShowResponse(entity.getId(), entity.getDescricao());
    }
}
