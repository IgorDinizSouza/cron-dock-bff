-- Script de carga inicial dos estados do Brasil (26 estados + DF)
-- Tabela esperada: estado(id, descricao, uf)

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
    (27, 'Tocantins', 'TO');

-- Ajusta a sequence do PostgreSQL para novos inserts automáticos após carga com IDs fixos
SELECT setval(
    pg_get_serial_sequence('estado', 'id'),
    (SELECT COALESCE(MAX(id), 1) FROM estado)
);
