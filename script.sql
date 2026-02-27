-- Script consolidado (idempotente)
-- Objetivo: inserir cargas iniciais de cadastros e dados transacionais
-- Regra: todos os inserts verificam existencia antes de inserir (ON CONFLICT / NOT EXISTS)

-- ======================================================================================
-- AJUSTES DE SCHEMA (CARGA)
-- ======================================================================================
ALTER TABLE carga
    ADD COLUMN IF NOT EXISTS usuario_solicitante_id BIGINT,
    ADD COLUMN IF NOT EXISTS usuario_aprovador_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_carga_usuario_solicitante') THEN
        ALTER TABLE carga
            ADD CONSTRAINT fk_carga_usuario_solicitante
            FOREIGN KEY (usuario_solicitante_id) REFERENCES usuario(id);
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_carga_usuario_aprovador') THEN
        ALTER TABLE carga
            ADD CONSTRAINT fk_carga_usuario_aprovador
            FOREIGN KEY (usuario_aprovador_id) REFERENCES usuario(id);
    END IF;
END
$$;

-- ======================================================================================
-- ESTADOS
-- ======================================================================================
INSERT INTO estado (id, descricao, uf) VALUES
    (1, 'Acre', 'AC'),
    (2, 'Alagoas', 'AL'),
    (3, 'Amapa', 'AP'),
    (4, 'Amazonas', 'AM'),
    (5, 'Bahia', 'BA'),
    (6, 'Ceara', 'CE'),
    (7, 'Distrito Federal', 'DF'),
    (8, 'Espirito Santo', 'ES'),
    (9, 'Goias', 'GO'),
    (10, 'Maranhao', 'MA'),
    (11, 'Mato Grosso', 'MT'),
    (12, 'Mato Grosso do Sul', 'MS'),
    (13, 'Minas Gerais', 'MG'),
    (14, 'Para', 'PA'),
    (15, 'Paraiba', 'PB'),
    (16, 'Parana', 'PR'),
    (17, 'Pernambuco', 'PE'),
    (18, 'Piaui', 'PI'),
    (19, 'Rio de Janeiro', 'RJ'),
    (20, 'Rio Grande do Norte', 'RN'),
    (21, 'Rio Grande do Sul', 'RS'),
    (22, 'Rondonia', 'RO'),
    (23, 'Roraima', 'RR'),
    (24, 'Santa Catarina', 'SC'),
    (25, 'Sao Paulo', 'SP'),
    (26, 'Sergipe', 'SE'),
    (27, 'Tocantins', 'TO')
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao,
    uf = EXCLUDED.uf;

SELECT setval(
    pg_get_serial_sequence('estado', 'id'),
    (SELECT COALESCE(MAX(id), 1) FROM estado)
);

-- ======================================================================================
-- FLUXO APROVACAO DE USUARIO
-- ======================================================================================
INSERT INTO status_aprovacao_usuario (id, descricao) VALUES
    (1, 'Solicitado'),
    (2, 'Aprovado'),
    (3, 'Recusado')
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao;

INSERT INTO role_sistema (nome, descricao, data_criacao, data_alteracao)
VALUES ('APROVAR_USUARIO', 'APROVAR USUARIO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao,
    data_alteracao = CURRENT_TIMESTAMP;

-- ======================================================================================
-- STATUS CARGA
-- ======================================================================================
INSERT INTO status_carga (id, descricao) VALUES
    (1, 'EM DIGITACAO'),
    (2, 'SOLICITADO'),
    (3, 'PENDENTE DE APROVACAO'),
    (4, 'APROVADO')
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao;

-- ======================================================================================
-- CADASTRAIS BASE
-- ======================================================================================

-- Grupo empresarial
INSERT INTO grupo_empresarial (descricao, cnpj, status, data_criacao)
VALUES ('GRUPO DEMO', '11111111000199', 1, CURRENT_TIMESTAMP)
ON CONFLICT (cnpj) DO UPDATE
SET descricao = EXCLUDED.descricao,
    status = EXCLUDED.status;

-- Comprador (id manual no modelo)
INSERT INTO comprador (id, grupo_empresarial_id, descricao, status, data_criacao)
SELECT 1001, ge.id, 'COMPRADOR DEMO', 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
ON CONFLICT (id) DO UPDATE
SET grupo_empresarial_id = EXCLUDED.grupo_empresarial_id,
    descricao = EXCLUDED.descricao,
    status = EXCLUDED.status;

-- Fornecedor
INSERT INTO fornecedor (grupo_empresarial_id, cnpj, razao_social, cidade, uf, data_cadastro, status, data_criacao)
SELECT ge.id, '22222222000199', 'FORNECEDOR DEMO LTDA', 'SAO PAULO', 'SP', CURRENT_DATE, 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
  AND NOT EXISTS (
      SELECT 1
      FROM fornecedor f
      WHERE f.grupo_empresarial_id = ge.id
        AND f.cnpj = '22222222000199'
  );

-- Filial (id manual no modelo)
INSERT INTO filial (
    id, grupo_empresarial_id, descricao, cnpj, endereco, bairro, codigo_ibge_cidade,
    uf, cep, cd, wms, flag_regional, descricao_regional, status, data_criacao
)
SELECT
    2001, ge.id, 'FILIAL DEMO', '33333333000199', 'RUA DEMO, 100', 'CENTRO', '3550308',
    'SP', '01000000', 1, 1, 1, 'REGIONAL SUDESTE', 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
ON CONFLICT (id) DO UPDATE
SET grupo_empresarial_id = EXCLUDED.grupo_empresarial_id,
    descricao = EXCLUDED.descricao,
    cnpj = EXCLUDED.cnpj,
    endereco = EXCLUDED.endereco,
    bairro = EXCLUDED.bairro,
    codigo_ibge_cidade = EXCLUDED.codigo_ibge_cidade,
    uf = EXCLUDED.uf,
    cep = EXCLUDED.cep,
    cd = EXCLUDED.cd,
    wms = EXCLUDED.wms,
    flag_regional = EXCLUDED.flag_regional,
    descricao_regional = EXCLUDED.descricao_regional,
    status = EXCLUDED.status;

-- Transportador
INSERT INTO transportador (descricao, cnpj, status, data_criacao, data_alteracao, grupo_empresarial_id)
SELECT 'TRANSPORTADOR DEMO', '44444444000199', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ge.id
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
  AND NOT EXISTS (
      SELECT 1
      FROM transportador t
      WHERE t.grupo_empresarial_id = ge.id
        AND t.cnpj = '44444444000199'
  );

-- Tipo carga
INSERT INTO tipo_carga (id, descricao, min_sku, max_sku) VALUES
    (1, 'SECA', 1, 9999),
    (2, 'REFRIGERADA', 1, 9999)
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao,
    min_sku = EXCLUDED.min_sku,
    max_sku = EXCLUDED.max_sku;

-- Tipo veiculo
INSERT INTO tipo_veiculo (id, nome, quantidade_maxima_paletes, status) VALUES
    (1, 'TRUCK', 28, 1),
    (2, 'CARRETA', 32, 1)
ON CONFLICT (id) DO UPDATE
SET nome = EXCLUDED.nome,
    quantidade_maxima_paletes = EXCLUDED.quantidade_maxima_paletes,
    status = EXCLUDED.status;

-- Especie carga
INSERT INTO especie_carga (id, descricao, ativo, data_criacao, data_alteracao) VALUES
    (1, 'PALETIZADA', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'BATIDA', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO UPDATE
SET descricao = EXCLUDED.descricao,
    ativo = EXCLUDED.ativo,
    data_alteracao = CURRENT_TIMESTAMP;

SELECT setval(pg_get_serial_sequence('tipo_carga', 'id'), (SELECT COALESCE(MAX(id), 1) FROM tipo_carga));
SELECT setval(pg_get_serial_sequence('tipo_veiculo', 'id'), (SELECT COALESCE(MAX(id), 1) FROM tipo_veiculo));
SELECT setval(pg_get_serial_sequence('especie_carga', 'id'), (SELECT COALESCE(MAX(id), 1) FROM especie_carga));

-- Municipio
INSERT INTO municipio (descricao, codigo_ibge, estado_id)
SELECT 'SAO PAULO', '3550308', 25
WHERE NOT EXISTS (
    SELECT 1
    FROM municipio m
    WHERE m.codigo_ibge = '3550308'
);

-- ======================================================================================
-- PRODUTOS E EMBALAGENS
-- ======================================================================================

INSERT INTO produto (
    id, grupo_empresarial_id, comprador_id, descricao, complemento, lastro, altura, peso,
    peso_liquido, composicao, data_cadastro, status, data_criacao
)
SELECT
    3001, ge.id, 1001, 'PRODUTO DEMO 01', 'COMPLEMENTO DEMO', 10, 1.200, '10.0', '9.8', 1,
    CURRENT_DATE, 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
ON CONFLICT (id) DO UPDATE
SET grupo_empresarial_id = EXCLUDED.grupo_empresarial_id,
    comprador_id = EXCLUDED.comprador_id,
    descricao = EXCLUDED.descricao,
    complemento = EXCLUDED.complemento,
    lastro = EXCLUDED.lastro,
    altura = EXCLUDED.altura,
    peso = EXCLUDED.peso,
    peso_liquido = EXCLUDED.peso_liquido,
    composicao = EXCLUDED.composicao,
    data_cadastro = EXCLUDED.data_cadastro,
    status = EXCLUDED.status;

INSERT INTO embalagem (
    id, grupo_empresarial_id, produto_id, digito, codigo_barra, sigla,
    multiplicador_1, multiplicador_2, status, data_criacao
)
SELECT
    4001, ge.id, 3001, 1, '7890000000001', 'CX', 1.000000, 1.000000, 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
WHERE ge.cnpj = '11111111000199'
ON CONFLICT (id) DO UPDATE
SET grupo_empresarial_id = EXCLUDED.grupo_empresarial_id,
    produto_id = EXCLUDED.produto_id,
    digito = EXCLUDED.digito,
    codigo_barra = EXCLUDED.codigo_barra,
    sigla = EXCLUDED.sigla,
    multiplicador_1 = EXCLUDED.multiplicador_1,
    multiplicador_2 = EXCLUDED.multiplicador_2,
    status = EXCLUDED.status;

-- ======================================================================================
-- PEDIDOS E ITENS
-- ======================================================================================

INSERT INTO pedido (
    grupo_empresarial_id, filial_id, fornecedor_id, id_pedido, qtde_itens_pendentes, filial_regional,
    data_elaboracao, tipo_operacao, comprador_id, prioritario, paletizado, tipo_carga, status, data_criacao
)
SELECT
    ge.id,
    2001,
    f.id,
    500001,
    1,
    2001,
    CURRENT_DATE,
    1,
    1001,
    0,
    1,
    1,
    1,
    CURRENT_TIMESTAMP
FROM grupo_empresarial ge
JOIN fornecedor f
  ON f.grupo_empresarial_id = ge.id
 AND f.cnpj = '22222222000199'
WHERE ge.cnpj = '11111111000199'
  AND NOT EXISTS (
      SELECT 1
      FROM pedido p
      WHERE p.grupo_empresarial_id = ge.id
        AND p.id_pedido = 500001
  );

INSERT INTO pedido_item (
    pedido_id, filial_id, sequencia, produto_id, embalagem_id, data_entrega,
    qtd_pedida, qtd_recebida, carga_palet, abc, participacao_item
)
SELECT
    p.id,
    2001,
    1,
    3001,
    4001,
    CURRENT_DATE + INTERVAL '3 day',
    100.000000,
    0.000000,
    1,
    1,
    100.000000
FROM pedido p
JOIN grupo_empresarial ge ON ge.id = p.grupo_empresarial_id
WHERE ge.cnpj = '11111111000199'
  AND p.id_pedido = 500001
  AND NOT EXISTS (
      SELECT 1
      FROM pedido_item pi
      WHERE pi.pedido_id = p.id
        AND pi.sequencia = 1
  );

-- ======================================================================================
-- NOTAS FISCAIS E ITENS
-- ======================================================================================

INSERT INTO nota_fiscal (
    id, grupo_empresarial_id, filial_id, fornecedor_id, nota_fiscal_fornecedor,
    qtde_itens_nota, data_nf, data_recebimento, data_efetiva, estorno,
    nota_entrada_original, nota_fiscal_origem, tipo_operacao, status, data_criacao
)
SELECT
    600001, ge.id, 2001, f.id, 900001,
    1, CURRENT_DATE, CURRENT_DATE, CURRENT_DATE, FALSE,
    NULL, NULL, 1, 1, CURRENT_TIMESTAMP
FROM grupo_empresarial ge
JOIN fornecedor f
  ON f.grupo_empresarial_id = ge.id
 AND f.cnpj = '22222222000199'
WHERE ge.cnpj = '11111111000199'
ON CONFLICT (id) DO UPDATE
SET grupo_empresarial_id = EXCLUDED.grupo_empresarial_id,
    filial_id = EXCLUDED.filial_id,
    fornecedor_id = EXCLUDED.fornecedor_id,
    nota_fiscal_fornecedor = EXCLUDED.nota_fiscal_fornecedor,
    qtde_itens_nota = EXCLUDED.qtde_itens_nota,
    data_nf = EXCLUDED.data_nf,
    data_recebimento = EXCLUDED.data_recebimento,
    data_efetiva = EXCLUDED.data_efetiva,
    estorno = EXCLUDED.estorno,
    nota_entrada_original = EXCLUDED.nota_entrada_original,
    nota_fiscal_origem = EXCLUDED.nota_fiscal_origem,
    tipo_operacao = EXCLUDED.tipo_operacao,
    status = EXCLUDED.status;

INSERT INTO nota_fiscal_item (
    nota_fiscal_id, filial_id, pedido_numero, produto_id, embalagem_id, qtde_receb
)
SELECT
    600001, 2001, 500001, 3001, 4001, 100.000000
WHERE NOT EXISTS (
    SELECT 1
    FROM nota_fiscal_item nfi
    WHERE nfi.nota_fiscal_id = 600001
      AND nfi.produto_id = 3001
      AND nfi.embalagem_id = 4001
);
