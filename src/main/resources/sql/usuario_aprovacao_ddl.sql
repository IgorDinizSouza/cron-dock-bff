-- DDL das tabelas de aprovacao de usuario
-- PostgreSQL

CREATE TABLE IF NOT EXISTS status_aprovacao_usuario (
    id BIGINT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuario_aprovacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_solicitante_id BIGINT NOT NULL UNIQUE,
    usuario_aprovador_id BIGINT NULL,
    status_aprovacao_usuario_id BIGINT NOT NULL,
    motivo_recusa VARCHAR(500) NULL,
    data_solicitacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP NULL,
    CONSTRAINT fk_usuario_aprovacao_solicitante
        FOREIGN KEY (usuario_solicitante_id) REFERENCES usuario(id),
    CONSTRAINT fk_usuario_aprovacao_aprovador
        FOREIGN KEY (usuario_aprovador_id) REFERENCES usuario(id),
    CONSTRAINT fk_usuario_aprovacao_status
        FOREIGN KEY (status_aprovacao_usuario_id) REFERENCES status_aprovacao_usuario(id),
    CONSTRAINT ck_usuario_aprovacao_motivo_recusa
        CHECK (
            status_aprovacao_usuario_id <> 3
            OR (motivo_recusa IS NOT NULL AND btrim(motivo_recusa) <> '')
        )
);

INSERT INTO status_aprovacao_usuario (id, descricao) VALUES
    (1, 'Solicitado'),
    (2, 'Aprovado'),
    (3, 'Recusado')
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao;
