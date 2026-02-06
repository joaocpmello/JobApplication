JobBoard
Visão geral
O JobBoard é uma plataforma de vagas com backend em Spring Boot e frontend em React, permitindo o fluxo de autenticação, navegação por vagas e gestão de candidaturas e vagas por empresas.

Funcionalidades
Autenticação e cadastro de usuários (com papéis de candidato e empresa) e login/logout.

Listagem e filtro de vagas com busca por título/status e acesso ao detalhe da vaga.

Candidaturas e gestão de vagas (minhas candidaturas, vagas da empresa, aplicações por vaga).

Perfil de empresa e atualização de dados da empresa autenticada.

Stack e dependências principais
Backend
Spring Boot 4.0.1, Java 21, JPA, Security, Validation, WebMVC e OpenAPI (Swagger).

JWT para autenticação e PostgreSQL como banco principal (com H2 opcional).

Frontend
React + Vite, com React Router e Axios para consumo de API.

Configuração do backend
Porta padrão: 8080.

PostgreSQL configurado em application.yml/application.properties (URL/usuário/senha).

JWT configurável por variáveis de ambiente (JWT_SECRET e JWT_EXPIRATION_MS).

Swagger disponível em /swagger-ui.html e /v3/api-docs.

Perfil de desenvolvimento (H2)
Para desenvolvimento local, há configurações em application-dev.properties usando H2 em memória e console em /h2-console.

Configuração do frontend
A API base é definida por VITE_API_URL e, por padrão, aponta para http://localhost:8080.

Scripts disponíveis: dev, build, lint, preview.

Como executar localmente
Backend (Spring Boot)
Configure o banco (PostgreSQL) conforme application.yml ou application.properties.

Para rodar em modo dev com H2:

./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
(usa as configs de application-dev.properties).

Frontend (React + Vite)
Entre na pasta frontend e instale dependências.

Rode o servidor de desenvolvimento:

npm run dev
(script definido no package.json).
