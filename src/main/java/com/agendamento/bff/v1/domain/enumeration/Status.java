package com.agendamento.bff.v1.domain.enumeration;

public enum Status {
    INATIVO(0),
    ATIVO(1);

    private final int codigo;

    Status(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Status valueOf(int codigo) {
        for (Status value : Status.values()) {
            if (value.getCodigo() == codigo) {
                return value;
            }
        }
        throw new IllegalArgumentException("Código de status inválido: " + codigo);
    }
}