-- =============================================
-- CONFIGURAÇÃO DO BANCO DE DADOS — garagem52
-- =============================================

CREATE DATABASE IF NOT EXISTS garagem52_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE garagem52_db;

-- =============================================
-- A tabela "users" é criada automaticamente
-- pelo Hibernate com ddl-auto=update.
-- Script abaixo para criação manual (produção):
-- =============================================

CREATE TABLE IF NOT EXISTS users (
    user_id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255) NOT NULL,
    cep VARCHAR(255),
    senha VARCHAR(255) NOT NULL,
    regra     ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS veiculo(
    id BIGINT NOT NULL AUTO_INCREMENT,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    placa VARCHAR(255) NOT NULL,
    cor VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE= InnoDB DEFAULT CHARSET=utf8mb4 COLLATE= utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS servico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    servico_Orcado VARCHAR(255),
    veiculo_id BIGINT NOT NULL,
    data_entrada DATETIME,
    descricao_problema VARCHAR(255),
    status VARCHAR(45),

    PRIMARY KEY (id),

    CONSTRAINT fk_servico_veiculo
        FOREIGN KEY (veiculo_id)
        REFERENCES veiculo(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS peca (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    descricao VARCHAR(45),
    valor DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    cep VARCHAR(45),
    telefone VARCHAR(45),

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS fornecedor_has_peca (
    fornecedor_id BIGINT NOT NULL,
    peca_id BIGINT NOT NULL,

    PRIMARY KEY (fornecedor_id, peca_id),

    CONSTRAINT fk_fornecedor_has_peca_fornecedor
        FOREIGN KEY (fornecedor_id)
        REFERENCES fornecedor(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_fornecedor_has_peca_peca
        FOREIGN KEY (peca_id)
        REFERENCES peca(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS orcamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    servico_id BIGINT NOT NULL,
    veiculo_id BIGINT NOT NULL,
    valor_mao_de_obra DECIMAL(10,2),
    valor_total DECIMAL(10,2),
    data_orcamento DATETIME,
    status VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    motivo_cancelamento VARCHAR(30),
    nome_cliente VARCHAR(255),
    telefone_cliente VARCHAR(255),
    descricao_servico VARCHAR(255),

    PRIMARY KEY (id),

    CONSTRAINT fk_orcamento_servico
        FOREIGN KEY (servico_id)
        REFERENCES servico(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_orcamento_veiculo
        FOREIGN KEY (veiculo_id)
        REFERENCES veiculo(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS item_orcado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    orcamento_id BIGINT NOT NULL,
    peca_id BIGINT NOT NULL,
    valor DECIMAL(10,2),
    quantidade INT,
    fornecedor VARCHAR(255),

    PRIMARY KEY (id),

    CONSTRAINT fk_item_orcado_orcamento
        FOREIGN KEY (orcamento_id)
        REFERENCES orcamento(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_orcado_peca
        FOREIGN KEY (peca_id)
        REFERENCES peca(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS password_reset_token (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    token      VARCHAR(255) NOT NULL UNIQUE,
    user_id    BIGINT       NOT NULL,
    expires_at DATETIME     NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,

    PRIMARY KEY (id),

    CONSTRAINT fk_prt_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- USUÁRIO DEDICADO (boa prática — evite root em produção)
-- =============================================
-- CREATE USER IF NOT EXISTS 'garagem52_app'@'localhost' IDENTIFIED BY 'senha_segura';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON garagem52_db.* TO 'garagem52_app'@'localhost';
-- FLUSH PRIVILEGES;

-- =============================================
-- MIGRATION: adiciona colunas que faltavam
-- Execute se o banco já existia antes desta versão
-- =============================================
ALTER TABLE orcamento
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    ADD COLUMN IF NOT EXISTS motivo_cancelamento VARCHAR(30),
    ADD COLUMN IF NOT EXISTS nome_cliente VARCHAR(255),
    ADD COLUMN IF NOT EXISTS telefone_cliente VARCHAR(255),
    ADD COLUMN IF NOT EXISTS descricao_servico VARCHAR(255);

ALTER TABLE item_orcado
    ADD COLUMN IF NOT EXISTS fornecedor VARCHAR(255);
