-- Carga inicial de status de carga
INSERT INTO status_carga (id, descricao) VALUES
    (1, 'EM DIGITACAO'),
    (2, 'SOLICITADO'),
    (3, 'PENDENTE DE APROVACAO'),
    (4, 'APROVADO')
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao;
