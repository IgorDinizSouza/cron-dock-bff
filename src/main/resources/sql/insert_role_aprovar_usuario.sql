-- Cria a role necessaria para aprovacao de usuarios
-- Observacao: o codigo valida o NOME como 'APROVAR_USUARIO' (com underscore)

INSERT INTO role_sistema (nome, descricao, data_criacao, data_alteracao)
VALUES ('APROVAR_USUARIO', 'APROVAR USUARIO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao,
    data_alteracao = CURRENT_TIMESTAMP;
