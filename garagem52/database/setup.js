// =============================================================
// SETUP — banco garagem_52 no MongoDB
// Execute: mongosh < setup.js
// =============================================================
//
// IMPORTANTE: todas as collections usam _id do tipo ObjectId (string hex).
// Os validators NÃO incluem _id — o MongoDB gerencia esse campo sozinho.
// Campos *_id dentro dos documentos (ex: veiculo_id, user_id) são
// referências por ObjectId armazenadas como string.
// =============================================================

use("garagem_52");

// ── USERS ──────────────────────────────────────────────────────────────────
// Mecânicos e administradores do sistema.
// Clientes NÃO ficam aqui — ficam em cliente_veiculo.

db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "email", "telefone", "senha", "regra"],
      properties: {
        name:     { bsonType: "string" },
        email:    { bsonType: "string" },
        telefone: { bsonType: "string" },
        cep:      { bsonType: ["string", "null"] },
        senha:    { bsonType: "string" },
        regra:    { enum: ["USER", "ADMIN"] }
      }
    }
  }
});

db.users.createIndex({ email: 1 }, { unique: true, name: "idx_users_email" });


// ── VEICULO ────────────────────────────────────────────────────────────────
// Cadastrado via API externa (wdapi2.com.br) na primeira consulta pela placa.

db.createCollection("veiculo", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["marca", "modelo", "ano", "placa", "cor"],
      properties: {
        marca:  { bsonType: "string" },
        modelo: { bsonType: "string" },
        ano:    { bsonType: "int" },
        placa:  { bsonType: "string" },
        cor:    { bsonType: "string" }
      }
    }
  }
});

db.veiculo.createIndex({ placa: 1 }, { unique: true, name: "idx_veiculo_placa" });


// ── CLIENTE_VEICULO ────────────────────────────────────────────────────────
// Registra a entrada de um cliente na oficina (criado pelo mecânico).
// Associa os dados do cliente ao veículo cadastrado.
// veiculo_id referencia a collection veiculo (_id ObjectId como string).

db.createCollection("cliente_veiculo", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nome_cliente", "telefone_cliente", "placa_veiculo", "veiculo_id"],
      properties: {
        nome_cliente:     { bsonType: "string" },
        telefone_cliente: { bsonType: "string" },
        email_cliente:    { bsonType: ["string", "null"] },
        modelo_veiculo:   { bsonType: ["string", "null"] },
        placa_veiculo:    { bsonType: "string" },
        veiculo_id:       { bsonType: "string" }   // ObjectId do veiculo como string
      }
    }
  }
});

db.cliente_veiculo.createIndex({ placa_veiculo: 1 }, { name: "idx_cliente_veiculo_placa" });
db.cliente_veiculo.createIndex({ nome_cliente: 1 },  { name: "idx_cliente_veiculo_nome" });


// ── SERVICO ────────────────────────────────────────────────────────────────
// Criado automaticamente pelo backend ao criar um orçamento.
// veiculo_id referencia a collection veiculo.

db.createCollection("servico", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["veiculo_id"],
      properties: {
        servico_orcado:    { bsonType: ["string", "null"] },
        veiculo_id:        { bsonType: "string" },           // ObjectId do veiculo
        data_entrada:      { bsonType: ["date", "null"] },
        descricao_problema:{ bsonType: ["string", "null"] },
        status:            { bsonType: ["string", "null"] }
      }
    }
  }
});

db.servico.createIndex({ veiculo_id: 1 }, { name: "idx_servico_veiculo_id" });
db.servico.createIndex({ status: 1 },     { name: "idx_servico_status" });


// ── PECA ───────────────────────────────────────────────────────────────────

db.createCollection("peca", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nome", "valor"],
      properties: {
        nome:     { bsonType: "string" },
        descricao:{ bsonType: ["string", "null"] },
        valor:    { bsonType: ["decimal", "double"] }
      }
    }
  }
});


// ── FORNECEDOR ─────────────────────────────────────────────────────────────

db.createCollection("fornecedor", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nome"],
      properties: {
        nome:    { bsonType: "string" },
        cep:     { bsonType: ["string", "null"] },
        telefone:{ bsonType: ["string", "null"] }
      }
    }
  }
});


// ── FORNECEDOR_HAS_PECA ────────────────────────────────────────────────────
// Relação N:N entre fornecedor e peça.
// Ambos os IDs são ObjectId armazenados como string.

db.createCollection("fornecedor_has_peca", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["fornecedor_id", "peca_id"],
      properties: {
        fornecedor_id: { bsonType: "string" },
        peca_id:       { bsonType: "string" }
      }
    }
  }
});

db.fornecedor_has_peca.createIndex(
  { fornecedor_id: 1, peca_id: 1 },
  { unique: true, name: "idx_fornecedor_peca_unique" }
);


// ── ORCAMENTO ──────────────────────────────────────────────────────────────
// itens é um array embedded — substitui a collection item_orcado.
// servico_id, veiculo_id e cliente_veiculo_id são ObjectIds como string.

db.createCollection("orcamento", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["servico_id", "veiculo_id", "status"],
      properties: {
        servico_id:          { bsonType: "string" },
        veiculo_id:          { bsonType: "string" },
        cliente_veiculo_id:  { bsonType: ["string", "null"] },
        valor_mao_de_obra:   { bsonType: ["decimal", "double", "null"] },
        valor_total:         { bsonType: ["decimal", "double", "null"] },
        data_orcamento:      { bsonType: ["date", "null"] },
        status:              { bsonType: "string" },
        motivo_cancelamento: { bsonType: ["string", "null"] },
        nome_cliente:        { bsonType: ["string", "null"] },
        telefone_cliente:    { bsonType: ["string", "null"] },
        email_cliente:       { bsonType: ["string", "null"] },
        descricao_servico:   { bsonType: ["string", "null"] },
        itens: {
          bsonType: ["array", "null"],
          items: {
            bsonType: "object",
            properties: {
              peca_id:    { bsonType: ["string", "null"] },
              nome_peca:  { bsonType: ["string", "null"] },
              valor:      { bsonType: ["decimal", "double", "null"] },
              quantidade: { bsonType: ["int", "null"] },
              fornecedor: { bsonType: ["string", "null"] }
            }
          }
        }
      }
    }
  }
});

db.orcamento.createIndex({ servico_id: 1 },         { name: "idx_orcamento_servico_id" });
db.orcamento.createIndex({ veiculo_id: 1 },         { name: "idx_orcamento_veiculo_id" });
db.orcamento.createIndex({ cliente_veiculo_id: 1 }, { name: "idx_orcamento_cliente_veiculo_id" });
db.orcamento.createIndex({ status: 1 },             { name: "idx_orcamento_status" });
db.orcamento.createIndex({ data_orcamento: 1 },     { name: "idx_orcamento_data" });


// ── PASSWORD_RESET_TOKEN ───────────────────────────────────────────────────
// user_id referencia a collection users.

db.createCollection("password_reset_token", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["token", "user_id", "expires_at", "used"],
      properties: {
        token:      { bsonType: "string" },
        user_id:    { bsonType: "string" },   // ObjectId do user
        expires_at: { bsonType: "date" },
        used:       { bsonType: "bool" }
      }
    }
  }
});

db.password_reset_token.createIndex({ token: 1 },   { unique: true, name: "idx_prt_token" });
db.password_reset_token.createIndex({ user_id: 1 }, { name: "idx_prt_user_id" });


// ── LOGIN_TOKENS ───────────────────────────────────────────────────────────

db.createCollection("login_tokens", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["user_id", "token", "expires_at", "used"],
      properties: {
        user_id:    { bsonType: "string" },   // ObjectId do user
        token:      { bsonType: "string" },
        expires_at: { bsonType: "date" },
        used:       { bsonType: "bool" }
      }
    }
  }
});

db.login_tokens.createIndex({ token: 1 },   { unique: true, name: "idx_lt_token" });
db.login_tokens.createIndex({ user_id: 1 }, { name: "idx_lt_user_id" });


print("✅  Setup do banco garagem_52 concluído com sucesso.");
print("    Collections criadas: users, veiculo, cliente_veiculo, servico,");
print("                         peca, fornecedor, fornecedor_has_peca,");
print("                         orcamento, password_reset_token, login_tokens");
