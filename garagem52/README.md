# 🔷 garagem52 — Arquitetura Hexagonal + Spring Boot + JWT

---

## 📐 Estrutura de Pacotes

```
com.garagem52
│
├── adapter/
│   ├── input/
│   │   ├── controller/         ← Inbound Adapters (HTTP)
│   │   │   ├── AuthController
│   │   │   └── UserController
│   │   ├── dto/                ← DTOs de entrada e saída
│   │   └── exceptionHandler/   ← Traduz exceções de domínio → HTTP
│   │       └── GlobalExceptionHandler
│   │
│   └── output/
│       └── persistence/
│           ├── JpaUserRepository       ← Spring Data JPA (ORM)
│           ├── database/
│           │   └── UserRepositoryAdapter  ← Outbound Adapter (implementa OutputPort)
│           ├── entity/
│           │   └── UserEntity          ← @Entity JPA mapeada pelo Hibernate
│           └── mapper/
│               └── UserPersistenceMapper  ← Domain ↔ Entity (MapStruct)
│
├── config/
│   ├── BeanConfiguration       ← Instancia Services e injeta OutputPorts
│   ├── SecurityConfig          ← Spring Security stateless (JWT)
│   └── jwt/
│       ├── JwtService          ← Gera e valida tokens JWT
│       └── JwtAuthFilter       ← Intercepta requisições e valida token
│
├── domain/
│   ├── exception/
│   │   ├── UserNotFoundException
│   │   └── EmailAlreadyExistsException
│   ├── model/
│   │   └── User                ← Entidade de domínio pura (sem frameworks)
│   ├── ports/
│   │   ├── input/
│   │   │   └── UserInputPort   ← Contrato dos casos de uso (interface)
│   │   └── output/
│   │       └── UserOutputPort  ← Contrato de persistência (interface)
│   └── service/
│       └── UserService         ← Implementa UserInputPort, usa UserOutputPort
│
└── Garagem52Application.java
```

---

## 🔄 Fluxo de uma Requisição

```
HTTP Request
    ↓
JwtAuthFilter (valida token)
    ↓
UserController / AuthController  [adapter/input]
    ↓ chama interface
UserInputPort
    ↓ implementado por
UserService  [domain/service]
    ↓ chama interface
UserOutputPort
    ↓ implementado por
UserRepositoryAdapter  [adapter/output/persistence/database]
    ↓ usa
JpaUserRepository  [Spring Data JPA]
    ↓ ORM/Hibernate
MySQL (garagem52_db)
```

---

## 🚀 Como rodar

```bash
# 1. Crie o banco
mysql -u root -p < database/setup.sql

# 2. Edite src/main/resources/application.properties
# → spring.datasource.password=SUA_SENHA

# 3. Execute
mvn spring-boot:run
```

---

## 📡 Endpoints

### Públicos (sem JWT)
| Método | Endpoint | Body |
|---|---|---|
| POST | `/auth/register` | `{ name, email, password }` |
| POST | `/auth/login` | `{ email, password }` |

### Protegidos (header: `Authorization: Bearer <token>`)
| Método | Endpoint | |
|---|---|---|
| GET | `/users` | Lista todos |
| GET | `/users/{id}` | Busca por ID |
| PUT | `/users/{id}` | Atualiza |
| DELETE | `/users/{id}` | Remove |
