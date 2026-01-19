# 🧑‍💻 User Service

API RESTful desenvolvida com Java + Spring Boot, focada em estudo e boas práticas de desenvolvimento backend.
O projeto implementa um CRUD de usuários e perfis (profiles), com arquitetura em camadas, validação, testes e integração com banco de dados via Docker.

## 🚀 Funcionalidades

*  CRUD de Usuários  
*  CRUD de Perfis (Profiles)  
*  Filtro por parâmetros via query string  
*  Validação de dados com Jakarta Validation  
*  Tratamento de exceções  
*  Testes de controller com MockMvc
*  Banco de dados MySQL com Docker Compose

## 📍 Endpoints da API 
### 👤 Users


| Método   | Endpoint                | Descrição                         |
| -------- | ----------------------- | --------------------------------- |
| `GET`    | `/v1/users`             | Lista todos os usuários           |
| `GET`    | `/v1/users?name={name}` | Lista usuários filtrando por nome |
| `GET`    | `/v1/users/{id}`        | Busca usuário por ID              |
| `POST`   | `/v1/users`             | Cria um novo usuário              |
| `PUT`    | `/v1/users`             | Atualiza um usuário               |
| `DELETE` | `/v1/users/{id}`        | Remove um usuário                 |

### 🛂 Profiles
| Método | Endpoint                   | Descrição                       |
| ------ | -------------------------- | ------------------------------- |
| `GET`  | `/v1/profiles`             | Lista todos os perfis           |
| `GET`  | `/v1/profiles?name={name}` | Lista perfis filtrando por nome |
| `GET`  | `/v1/profiles/{id}`        | Busca um perfil por ID          |
| `POST` | `/v1/profiles`             | Cria um novo perfil             |

## 📌 Observação:

Caso o perfil não seja encontrado, a API retorna **`{"status": 404, "message:" "Not Found"}`**

Quando não há resultado na busca por nome, a API retorna uma lista vazia

## 🧱 Arquitetura

O projeto segue uma arquitetura em camadas:

📦 controller\
📦 service\
📦 repository\
📦 domain\
📦 dtos\
📦mapper\
📦exception\
📦utils

Controller: Exposição dos endpoints REST  
Service: Regras de negócio  
Repository: Acesso a dados  
DTOs: Entrada e saída de dados  
Mapper: Conversão entre entidades e DTOs  
Exception: Tratamento de erros padronizado

## 🧪 Testes

* Testes de Controller com **@WebMvcTest**
* Mock de repositórios com **@MockBean**
* Validação de:
  * Status HTTP
  * Payload de resposta
  * Casos de sucesso e erro (404 Not Found)

* Uso de MockMvc, Mockito e ObjectMapper
* Exemplo testado:
  * `GET /v1/profiles`
  * `GET /v1/profiles?name=Admin`
  * `GET /v1/profiles/{id}`
  * `POST /v1/profiles`

## 🐳 Banco de Dados (Docker)
O projeto utiliza MySQL via Docker Compose.

```yaml
services:
  mysql:
    image: mysql:9.0.1
    container_name: user-service-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: ronoyaro
      MYSQL_PASSWORD: dev
    ports:
      - "3306:3306"
    volumes:
      - user_service-db:/var/lib/mysql

volumes:
  user_service-db:
```

## 🚀 Como executar o projeto

Clone o repositório
```git
git clone https://github.com/Ronoyaro/user-service.git
```
Suba o banco de dados
```git
docker-compose up -d
```
Execute a aplicação
```git
mvn clean install
mvn spring-boot:run
```
Acesse
```git
http://localhost:8080
```

## 🛠️ Tecnologias

* Java 17+
* Spring Boot
* Spring Web
* Spring Validation
* Maven
* MySQL
* Docker / Docker Compose
* JUnit 5
* Mockito
* Lombok

## 🎯 Objetivo do Projeto

* Este projeto tem como objetivo estudo e evolução contínua, servindo como base para:
* Implementação de autenticação (JWT / Spring Security)
* Paginação
* Versionamento de API
* Testes de integração
* Documentação com Swagger/OpenAPI