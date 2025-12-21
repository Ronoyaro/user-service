# 📌 User Service

API RESTful de usuários utilizando Java Spring Boot — um projeto CRUD básico, com arquitetura em camadas (Controller, Service, Repository).

Projeto inicial para gerenciar usuários via API — gradualmente será implementado testes e melhorias.

## 🚀 Tecnologias

✔️ Java (versão compatível com Spring Boot)  
✔️ Spring Boot (framework principal)  
✔️ Spring Web (REST API)  
✔️ Maven (gerenciamento de dependências)  
✔️ Camadas bem definidas (Controller → Service → Repository)


## 📦 Funcionalidades

| Método | Endpoint                       | Descrição                                   |
| ------ |--------------------------------|---------------------------------------------|
| GET    | `/v1/users or /v1/users?name=""` | Retorna todos usuários ou filtra o usuário  |
| GET    | `/v1/users/{id}`               | Retorna usuário por ID                      |
| POST   | `/v1/users`                    | Cria um novo usuário                        |
| PUT    | `/v1/users `                   | Atualiza um usuário existente               |
| DELETE | `/v1/users/{id}`               | Remove um usuário                           |

## 🧪 Testes

Esse projeto está estruturado para receber testes nas seguintes camadas:

Testes Unitários (Service, Repository mockado)  
Testes Unitrários utilizando Mockito

###  Informações Adicionais
Projeto ainda sendo implementado, onde eu demonstro o que venho aprendendo a cada dia sobre Spring Web.
Dá uma olhadinha nos commits! :)