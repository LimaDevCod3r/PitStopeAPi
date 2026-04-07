# PitStopAPI

API REST para gerenciamento de clientes, veículos e ordens de serviço de um centro automotivo.

## Tecnologias

- **Spring Boot 4.0.1** (Java 21)
- **Spring Data JPA** + **Hibernate**
- **MySQL 8** com **Flyway** para migrações
- **Lombok** para redução de boilerplate
- **Bean Validation** (`jakarta.validation`)
- **Springdoc OpenAPI** (Swagger UI)
- **JUnit 5** + **Mockito** + **AssertJ** para testes unitários

## Arquitetura

```
src/
├── controller/          # Endpoints REST (@RestController)
├── service/             # Regras de negócio
├── repository/          # Spring Data JPA repositories
├── model/               # Entidades JPA (@Entity)
├── dto/                 # Request/Response DTOs
├── mapper/              # Conversão entre Model ↔ DTO
└── exception/           # Custom exceptions + @ControllerAdvice
```

## Pré-requisitos

- JDK 21+
- Maven 3.9+
- MySQL 8 rodando localmente

## Configuração

Crie as variáveis de ambiente para conexão com o banco:

```bash
export DATABASE_URL=jdbc:mysql://localhost:3306/pitstop_db
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=your_password
```

Ou crie um arquivo `.env` e carregue no terminal.

## Executando

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Documentação (Swagger)

Após iniciar, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

### Clientes — `/clientes`

| Método | Rota          | Descrição                      |
|--------|---------------|-------------------------------|
| GET    | `/clientes?page=0&size=10&sortBy=name` | Lista clientes ativos (paginado) |
| GET    | `/clientes/{id}` | Busca cliente por ID          |
| POST   | `/clientes`   | Cria novo cliente              |
| PUT    | `/clientes/{id}` | Atualiza cliente            |
| DELETE | `/clientes/{id}` | Soft delete (desativa)       |

### Veículos — `/veiculos`

| Método | Rota          | Descrição                      |
|--------|---------------|-------------------------------|
| GET    | `/veiculos?page=0&size=10&sortBy=brand` | Lista veículos de clientes ativos |
| GET    | `/veiculos/{id}` | Busca veículo por ID         |
| POST   | `/veiculos`   | Cria novo veículo (vinculado a cliente ativo) |
| PUT    | `/veiculos/{id}` | Atualiza veículo           |
| DELETE | `/veiculos/{id}` | Hard delete                 |

### Ordens de Serviço — `/ordens-servico`

| Método | Rota                   | Descrição                              |
|--------|------------------------|---------------------------------------|
| GET    | `/ordens-servico?page=0&size=10&sortBy=id` | Lista todas as ordens |
| GET    | `/ordens-servico/{id}` | Busca ordem por ID                    |
| POST   | `/ordens-servico`      | Cria ordem (require cliente ativo + veículo existente) |

## Status Codes

| Código | Significado                                  |
|--------|---------------------------------------------|
| 200    | Sucesso                                     |
| 201    | Recurso criado                              |
| 400    | Validação falhou                            |
| 404    | Recurso não encontrado                      |
| 409    | Duplicidade (ex: placa, CPF, email)         |
| 410    | Recurso inativo (soft delete)               |

## Regras de Negócio

- **Soft delete**: clientes são desativados, não removidos
- **Filtro ativo**: `/clientes` retorna apenas clientes ativos, `/veiculos` retorna apenas veículos de clientes ativos
- **Validações de criação**:
  - Não é possível criar veículo para cliente inativo
  - Não é possível criar OS se cliente está inativo ou veículo não existe
  - Placa de veículo é única (formato Mercosul ou antigo)
  - CPF e email de cliente são únicos

## Rodando os testes

```bash
mvn test
```

```bash
# Teste específico
mvn test -Dtest=VehicleServiceTest
mvn test -Dtest=CustomerServiceTest
```

## Migrações (Flyway)

Rodadas automaticamente na inicialização em `src/main/resources/db/migrations/`:

- `V1__Create_...` — tabela `tb_customers`
- `V2__Create_...` — tabela `tb_vehicles`
- `V3__Create_service_orders` — tabela `tb_service_orders`
