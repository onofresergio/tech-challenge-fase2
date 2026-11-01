# Tech Challenge - Fase 2

Este repositório contém a especificação da API em **OpenAPI (Swagger)**.

## 📖 Documentação da API

A documentação interativa da API pode ser acessada pelo Swagger UI hospedado no GitHub Pages:

👉 [Visualizar documentação da API](https://onofresergio.github.io/tech-challenge-fase2/)

## 📂 Estrutura do repositório

```
src/main/java/br/com/fiap/techchallenge
├── domain
│   ├── exception
│   └── model
├── usecase
│   ├── assembler
│   ├── interactor
│   ├── port
│   │   ├── input
│   │   └── output
├── infrastructure
│   ├── configuration
│   ├── controller
│   │   ├── exception
│   │   ├── request
│   │   └── response
│   ├── database
│   │   ├── adapter
│   │   ├── entity
│   │   └── jpa
│   ├── security
│   └── validation
└── TechchallengeApplication.java
````
## 🚀 Como usar

1. Clone este repositório:
   ```bash
   git clone https://github.com/onofresergio/tech-challenge-fase1.git

## 🐳 Executando com Docker Compose

Este projeto já possui um arquivo `docker-compose.yaml` que facilita a execução da aplicação em containers.

### Passo a passo

1. Certifique-se de ter o **Docker** e o **Docker Compose** instalados em sua máquina.
    - [Instalar Docker](https://docs.docker.com/get-docker/)
    - [Instalar Docker Compose](https://docs.docker.com/compose/install/)

2. No diretório raiz do projeto, execute:
   ```bash
   docker-compose up --build -d

3. Para remover as imagens geradas pelo comando anterior
   ```bash
    docker-compose down -v