# 📦 ListaComprasAPI
<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/>
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white"/>
  <img src="https://img.shields.io/badge/Mockito-FF9900?style=for-the-badge&logo=mockito&logoColor=white"/>
</p>

<h1 align="center"> API de Gerenciamento de Lista de Compras </h1>

## 📌 Descrição

API REST desenvolvida em Java com Spring Boot para gerenciamento de uma lista de compras.

O sistema oferece operações completas de CRUD e filtragem de itens por categoria e status, com categorização automática dos itens utilizando uma API externa em Python baseada em machine learning.

A aplicação foi projetada para futura integração com um aplicativo mobile Android, visando oferecer praticidade ao usuário durante compras em supermercados e organização de sua lista pessoal.

## 🚀 Funcionalidades

✅ Adicionar, listar, atualizar e excluir itens da lista

📦 Categorização automática dos itens com IA

📂 Filtro de itens por:

- Categoria

- Status (Comprado, Pendente, Não Encontrado)

- Combinação entre categoria e status

🔍 Busca por ID

❌ Tratamento centralizado de erros

🧪 Testes automatizados com JUnit

## 🛠 Tecnologias Utilizadas

| Tecnologia      |	Descrição |
|-----------------|-----------|
| **Java 17** ☕	| Linguagem principal do projeto |
| **Spring Boot** 🌱 | Framework para criação da API REST |
| **Spring Data JPA** 📊 |	Abstração para acesso e persistência de dados |
| **Spring Web** 🌍 |	Módulo para criação de endpoints HTTP |
| **Maven** 📦 |	Gerenciador de dependências e build |
| **Hibernate** 🛢️ | ORM utilizado pelo JPA |
| **PostgreSQL** 🐘 |	Banco de dados relacional |
| **Lombok** ✍️ |	Reduz a verbosidade do código Java |
| **Swagger (OpenAPI)** 📄 |	Documentação interativa da API |
| **JUnit & Mockito** ✅ |	Testes unitários e mocks |
| **RestTemplate** 🌐 |	Cliente HTTP para integração com API externa |
| **Python (API externa)** 🐍 |	Serviço de categorização via ML |

## 📁 Estrutura do Projeto

O projeto segue a arquitetura em camadas, separando responsabilidades de forma clara:

| Camada         | Responsabilidade                                                    |
|----------------|---------------------------------------------------------------------|
| **Config**     | Configurações como RestTemplate, mensagens e CORS                   |
| **Controller** | Recebe as requisições HTTP e delega para os serviços                |
| **DTO**        | Objetos para transportar dados entre as camadas                     |
| **Exception**  | Tratamento centralizado de erros e exceções personalizadas          |
| **Model**      | Define as entidades que representam as tabelas do banco de dados    |
| **Repository** | Acesso e manipulação dos dados no banco via Spring Data JPA         |
| **Service**    | Contém a lógica de negócio e orquestra chamadas para outras camadas |
---

## 🧪 Testes Automatizados

Os testes cobrem:

- Regras de negócio na camada de serviço

- Validações e status HTTP na camada de controller

- Tratamento de exceções

- Casos de erro e integração com serviços externos

## 🔗 Integração com API Python

A categorização dos itens é feita via requisição POST para a API Python, que aplica um modelo de aprendizado de máquina (machine learning) e retorna a categoria mais provável com base no nome do item. Caso a API esteja indisponível, a aplicação trata a exceção e adiciona o item com uma **categoria padrão**.

A URL da API é configurável via `application.properties`:

```properties
categoria.api.url=http://127.0.0.1:8000/prever/
```

---

## 🚀 Como rodar localmente

### ✅ Pré-requisitos

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- API Python em execução (porta `8000`)

---

### 🧱 Configuração do banco de dados

1. Crie o banco no PostgreSQL:

```sql
CREATE DATABASE listacompras_db;
```

2. Atualize o arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/listacompras_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

categoria.api.url=http://127.0.0.1:8000/prever/
```

---

### ▶️ Executar a aplicação

```bash
git clone https://github.com/seu-usuario/nome-do-repositorio.git
cd nome-do-repositorio
mvn clean install
mvn spring-boot:run
```

---

### 📋 Documentação da API

Acesse após rodar:

```
http://localhost:8080/swagger-ui.html
```

---

### 🧪 Rodando os testes

```bash
mvn test
```

---

## 📌 Próximos passos

- 🔨 Desenvolver o aplicativo Android com integração à API
- 🧠 Melhorar o modelo de categorização da API Python
- 🔐 Implementar autenticação (JWT)
---

## 👨‍💻 Autor

**Hércules  Herculano**

Desenvolvedor Java | Apaixonado por resolver problemas com código limpo e boas práticas

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil-blue?logo=linkedin)](https://www.linkedin.com/in/herculesbruno/) | [![GitHub](https://img.shields.io/badge/GitHub-Perfil-black?logo=github)](https://github.com/herculesherculano)
