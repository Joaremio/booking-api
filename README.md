# Booking API

API REST para agendamento de recursos (salas, equipamentos e outros espaços). Permite autenticação com JWT, gestão de recursos por administradores e reserva de horários por usuários, com prevenção de conflitos de horário.

> Projeto de estudo/portfólio desenvolvido no contexto do IMD/UFRN.

## Tecnologias

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

- **Java 21**
- **Spring Boot 4.0.7**
- **Spring Security + JWT (jjwt 0.12.6)** — autenticação *stateless*
- **Spring Data JPA / Hibernate**
- **PostgreSQL 16** com constraint de exclusão (`btree_gist` + `tsrange`) para evitar conflitos de horário
- **Flyway** — migrações versionadas do banco
- **MapStruct + Lombok** — mapeamento de DTOs e entidades
- **SpringDoc / OpenAPI (Swagger UI)** — documentação interativa
- **Maven** — build e gerenciamento de dependências
- **Docker / docker-compose** — banco e aplicação

## Funcionalidades

- Autenticação e autorização com **JWT** e dois papéis: `ADMIN` e `USER`
- CRUD de **recursos** com *soft-delete* (desativação em vez de remoção)
- **Agendamentos** com validação de conflito de horário (no serviço e via constraint no banco)
- Consulta de **disponibilidade** de um recurso por dia
- Acesso restrito: apenas o proprietário ou um `ADMIN` pode ver/cancelar um agendamento
- **Tratamento global de erros** com respostas padronizadas
- Documentação **Swagger** completa com autenticação Bearer
- **Admin** criado automaticamente na primeira execução
- Configuração via **variáveis de ambiente**

## Como rodar

### Opção 1 — Docker (recomendada)

É preciso ter [Docker](https://www.docker.com/) instalado.

```bash
docker compose up --build
```

Isso sobe dois containers: o banco **PostgreSQL** e a aplicação. Depois de iniciar, acesse:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

### Opção 2 — Local (Maven)

Pré-requisitos: **JDK 21** e um **PostgreSQL** rodando.

1. Crie um banco (padrão: `booking`, usuário `postgres`, senha `1234567`):

```sql
CREATE DATABASE booking;
```

2. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

> No Windows, use `mvnw.cmd` no lugar de `./mvnw`.

### Variáveis de ambiente

Todas as variáveis possuem valores padrão e podem ser sobrescritas:

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/booking` | URL do banco |
| `DB_USERNAME` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `1234567` | Senha do banco |
| `JWT_SECRET` | (valor padrão embutido) | Chave secreta para assinatura do JWT |
| `ADMIN_EMAIL` | `admin@email.com` | E-mail do admin criado na inicialização |
| `ADMIN_PASSWORD` | `admin123456` | Senha do admin criado na inicialização |

**Importante:** ao fazer deploy, configure `JWT_SECRET`, `DB_*` e `ADMIN_*` com valores próprios — não use os padrões em produção.

## Credenciais padrão

Em toda inicialização, o sistema cria um usuário administrador (se ainda não existir):

- **E-mail:** `admin@email.com`
- **Senha:** `admin123456`

Usuários comuns são criados pelo endpoint `POST /auth/register`.

## Endpoints

### Autenticação
| Método | Caminho | Descrição | Acesso |
|--------|---------|-----------|--------|
| `POST` | `/auth/register` | Registra um novo usuário | Público |
| `POST` | `/auth/login` | Autentica e retorna o token JWT | Público |

### Recursos
| Método | Caminho | Descrição | Acesso |
|--------|---------|-----------|--------|
| `GET` | `/api/resources` | Lista recursos ativos | Autenticado |
| `GET` | `/api/resources/{id}` | Busca um recurso por ID | Autenticado |
| `POST` | `/api/resources` | Cria um recurso | ADMIN |
| `PUT` | `/api/resources/{id}` | Atualiza um recurso | ADMIN |
| `DELETE` | `/api/resources/{id}` | Desativa um recurso (soft-delete) | ADMIN |

### Agendamentos
| Método | Caminho | Descrição | Acesso |
|--------|---------|-----------|--------|
| `POST` | `/api/bookings` | Cria um agendamento | Autenticado |
| `GET` | `/api/bookings/me` | Lista os agendamentos do usuário logado | Autenticado |
| `GET` | `/api/bookings/availability?resourceId={id}&date={yyyy-MM-dd}` | Horários ocupados de um recurso no dia | Autenticado |
| `GET` | `/api/bookings/{id}` | Busca um agendamento por ID | Dono ou ADMIN |
| `PATCH` | `/api/bookings/{id}/cancel` | Cancela um agendamento | Dono ou ADMIN |

## Exemplos de uso

### Registrar um usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"João da Silva","email":"joao@email.com","password":"senhaSegura123"}'
```

### Fazer login e guardar o token

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@email.com","password":"admin123456"}'
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}
```

### Criar um recurso (somente ADMIN)

```bash
curl -X POST http://localhost:8080/api/resources \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"name":"Sala de Reunião A","description":"Sala com capacidade para 20 pessoas","capacity":20,"pricePerHour":50.00}'
```

### Criar um agendamento

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"resourceId":"<RESOURCE_ID>","startDateTime":"2026-09-10T14:00:00","endDateTime":"2026-09-10T16:00:00"}'
```

## Estrutura do projeto

```
src/main/java/br/ufrn/imd/booking/
├── config/        # OpenAPI e seed do admin
├── controller/    # Camada de API (Auth, Resource, Booking)
├── dto/           # Records de entrada/saída com validação
├── entity/        # Entidades JPA
├── enums/         # Role e Status
├── exception/     # Exceções de domínio + handler global
├── mapper/        # MapStruct (entidade ↔ DTO)
├── repository/    # Spring Data JPA
├── security/      # JWT, filtros e configuração de segurança
└── service/       # Regras de negócio

src/main/resources/
├── application.yml          # Configuração principal (env vars)
└── db/migration/            # Migrações Flyway (V1 a V4)

src/test/java/               # Testes unitários dos serviços
```

## Testes

Execute a suíte de testes unitários:

```bash
./mvnw test
```

## Licença

Projeto de cunho acadêmico/portfólio.
