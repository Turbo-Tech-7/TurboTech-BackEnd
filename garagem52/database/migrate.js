// =============================================================
// MIGRATION — atualiza collections existentes para o novo modelo
// Remove validators antigos (com *_id numéricos) e recria corretos.
//
// Execute APENAS se o banco já existia antes da migração para MongoDB.
// Se o banco for novo, use setup.js diretamente.
//
// Como executar:
//   mongosh garagem_52 < migrate.js
// =============================================================

use("garagem_52");

// ── helper: remove validator de uma collection ────────────────────────────
function removeValidator(collectionName) {
  db.runCommand({
    collMod: collectionName,
    validator: {},
    validationLevel: "off"
  });
  print("✓ Validator removido: " + collectionName);
}

// ── 1. Remove todos os validators antigos ────────────────────────────────
const collections = [
  "users",
  "veiculo",
  "servico",
  "peca",
  "fornecedor",
  "fornecedor_has_peca",
  "orcamento",
  "item_orcado",
  "password_reset_token",
  "login_tokens"
];

collections.forEach(removeValidator);

// ── 2. Recria validators corretos (ObjectId como string, sem *_id numéricos)

db.runCommand({
  collMod: "users",
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
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: users");

db.runCommand({
  collMod: "veiculo",
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
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: veiculo");

db.runCommand({
  collMod: "servico",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["veiculo_id"],
      properties: {
        servico_orcado:     { bsonType: ["string", "null"] },
        veiculo_id:         { bsonType: "string" },
        data_entrada:       { bsonType: ["date", "null"] },
        descricao_problema: { bsonType: ["string", "null"] },
        status:             { bsonType: ["string", "null"] }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: servico");

db.runCommand({
  collMod: "peca",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nome", "valor"],
      properties: {
        nome:      { bsonType: "string" },
        descricao: { bsonType: ["string", "null"] },
        valor:     { bsonType: ["decimal", "double"] }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: peca");

db.runCommand({
  collMod: "fornecedor",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nome"],
      properties: {
        nome:     { bsonType: "string" },
        cep:      { bsonType: ["string", "null"] },
        telefone: { bsonType: ["string", "null"] }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: fornecedor");

db.runCommand({
  collMod: "fornecedor_has_peca",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["fornecedor_id", "peca_id"],
      properties: {
        fornecedor_id: { bsonType: "string" },
        peca_id:       { bsonType: "string" }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: fornecedor_has_peca");

db.runCommand({
  collMod: "orcamento",
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
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: orcamento");

db.runCommand({
  collMod: "password_reset_token",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["token", "user_id", "expires_at", "used"],
      properties: {
        token:      { bsonType: "string" },
        user_id:    { bsonType: "string" },
        expires_at: { bsonType: "date" },
        used:       { bsonType: "bool" }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: password_reset_token");

db.runCommand({
  collMod: "login_tokens",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["user_id", "token", "expires_at", "used"],
      properties: {
        user_id:    { bsonType: "string" },
        token:      { bsonType: "string" },
        expires_at: { bsonType: "date" },
        used:       { bsonType: "bool" }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});
print("✓ Validator atualizado: login_tokens");

// ── 3. Cria collection cliente_veiculo se não existir ─────────────────────
const existingCollections = db.getCollectionNames();
if (!existingCollections.includes("cliente_veiculo")) {
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
          veiculo_id:       { bsonType: "string" }
        }
      }
    }
  });
  db.cliente_veiculo.createIndex({ placa_veiculo: 1 }, { name: "idx_cliente_veiculo_placa" });
  db.cliente_veiculo.createIndex({ nome_cliente: 1 },  { name: "idx_cliente_veiculo_nome" });
  print("✓ Collection criada: cliente_veiculo");
} else {
  db.runCommand({
    collMod: "cliente_veiculo",
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
          veiculo_id:       { bsonType: "string" }
        }
      }
    },
    validationLevel: "strict",
    validationAction: "error"
  });
  print("✓ Validator atualizado: cliente_veiculo");
}

// ── 4. Remove collection item_orcado (substituída por array embedded) ─────
if (existingCollections.includes("item_orcado")) {
  db.item_orcado.drop();
  print("✓ Collection removida: item_orcado (substituída por array embedded em orcamento)");
}

// ── 5. Garante índices nas collections principais ─────────────────────────
db.users.createIndex({ email: 1 },       { unique: true, name: "idx_users_email" });
db.veiculo.createIndex({ placa: 1 },     { unique: true, name: "idx_veiculo_placa" });
db.servico.createIndex({ veiculo_id: 1 },{ name: "idx_servico_veiculo_id" });
db.orcamento.createIndex({ data_orcamento: 1 }, { name: "idx_orcamento_data" });
db.orcamento.createIndex({ status: 1 },         { name: "idx_orcamento_status" });
db.login_tokens.createIndex({ token: 1 },   { unique: true, name: "idx_lt_token" });
db.login_tokens.createIndex({ user_id: 1 }, { name: "idx_lt_user_id" });
db.password_reset_token.createIndex({ token: 1 }, { unique: true, name: "idx_prt_token" });

print("");
print("Migração concluída com sucesso.");
print("Todos os validators foram atualizados para usar ObjectId (string).");
print("Collection cliente_veiculo criada/atualizada.");
print("Collection item_orcado removida (itens agora são embedded em orcamento).");
