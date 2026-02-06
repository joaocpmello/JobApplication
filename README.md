# 💼 JobBoard - Sistema de Vagas de Emprego

Sistema completo de gerenciamento de vagas de emprego estilo LinkedIn, desenvolvido com **Spring Boot** (backend) e **React** (frontend).

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-blue?style=flat&logo=react)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat&logo=postgresql)](https://www.postgresql.org/)

---

## 📋 Sobre o Projeto

Sistema full-stack que permite que candidatos busquem vagas e se candidatem, enquanto empresas podem publicar vagas e gerenciar candidaturas recebidas.

### ✨ Funcionalidades Principais

#### 👤 **Candidatos**
- ✅ Cadastro e autenticação com JWT
- ✅ Busca de vagas com filtros (título, status, empresa)
- ✅ Candidatura a vagas com carta de apresentação
- ✅ Acompanhamento do status das candidaturas (Aguardando, Em análise, Aprovado, Rejeitado)
- ✅ Gerenciamento das próprias candidaturas

#### 🏢 **Empresas**
- ✅ Cadastro e autenticação com JWT
- ✅ Criação e gerenciamento de perfil corporativo
- ✅ Publicação de vagas de emprego
- ✅ Visualização de candidaturas recebidas
- ✅ Atualização de status das candidaturas
- ✅ Gerenciamento completo de vagas (criar, editar, deletar)

#### 🔐 **Sistema**
- ✅ Autenticação via JWT (JSON Web Token)
- ✅ Autorização baseada em roles (CANDIDATE, COMPANY, ADMIN)
- ✅ Validação de senha forte
- ✅ Soft delete para preservação de dados
- ✅ Paginação em listagens
- ✅ API RESTful documentada com Swagger

---

## 🛠️ Tecnologias Utilizadas

### **Backend**
- **Java 17**
- **Spring Boot 3.x**
  - Spring Security (JWT)
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL** (banco de dados)
- **Hibernate** (ORM)
- **Maven** (gerenciamento de dependências)
- **Swagger/OpenAPI** (documentação da API)

### **Frontend**
- **React 18**
- **React Router** (navegação)
- **Axios** (requisições HTTP)
- **Vite** (build tool)
- **CSS Modules** (estilização)

---

## 🚀 Como Executar o Projeto

### **Pré-requisitos**

- **Java 17** ou superior
- **Maven 3.6+**
- **Node.js 18+** e **npm**
- **PostgreSQL 12+**
- **IntelliJ IDEA** (ou outra IDE Java) - opcional
- **VS Code** (ou outro editor) - opcional

---

### **1️⃣ Configurar o Banco de Dados**

#### Criar o banco de dados no PostgreSQL:

```bash
# Conectar ao PostgreSQL
psql -U postgres

# Criar o banco
CREATE DATABASE jobboard;

# Sair
\q
```

---

### **2️⃣ Configurar e Rodar o Backend**

#### **Passo 1:** Clone o repositório

```bash
git clone https://github.com/joaocpmello/JobApplication.git
cd JobApplication
```

#### **Passo 2:** Configure o `application.properties`

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/jobboard
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=jobboard-secret-key-change-this-in-production-minimum-32-characters
jwt.expiration=86400000

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

#### **Passo 3:** Execute o backend

```bash
# Via Maven
mvn spring-boot:run

# Ou via IntelliJ
# Clique com botão direito em JobboardApplication.java → Run
```

O backend estará rodando em: **http://localhost:8080**

---

### **3️⃣ Configurar e Rodar o Frontend**

#### **Passo 1:** Navegue até a pasta do frontend

```bash
cd jobboard-frontend
```

#### **Passo 2:** Instale as dependências

```bash
npm install
```

#### **Passo 3:** Configure o arquivo `.env`

Crie o arquivo `.env` na raiz do frontend:

```
VITE_API_URL=http://localhost:8080
```

#### **Passo 4:** Execute o frontend

```bash
npm run dev
```

O frontend estará rodando em: **http://localhost:5173**

---

## 📚 Documentação da API

### **Swagger UI**
Acesse a documentação interativa da API:

```
http://localhost:8080/swagger-ui.html
```

### **Principais Endpoints**

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/auth/register` | Cadastro de usuário | ❌ |
| POST | `/api/auth/login` | Login | ❌ |
| GET | `/api/users/me` | Perfil do usuário logado | ✅ |
| GET | `/api/jobs` | Listar vagas (paginado) | ❌ |
| POST | `/api/jobs` | Criar vaga | 🏢 COMPANY |
| POST | `/api/applications` | Candidatar-se | 👤 CANDIDATE |
| PATCH | `/api/applications/{id}/status` | Atualizar status | 🏢 COMPANY |
| GET | `/api/companies/{id}` | Ver empresa | ❌ |

---

## 🧪 Testando o Sistema

### **1. Cadastrar um Candidato**

```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@test.com",
  "password": "Senha@123",
  "role": "CANDIDATE"
}
```

### **2. Fazer Login**

```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "joao@test.com",
  "password": "Senha@123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@test.com"
  }
}
```

### **3. Buscar Vagas**

```bash
GET http://localhost:8080/api/jobs?page=0&size=10&status=OPEN
```

### **4. Candidatar-se**

```bash
POST http://localhost:8080/api/applications
Authorization: Bearer {seu-token}
Content-Type: application/json

{
  "jobId": 1,
  "coverLetter": "Tenho grande interesse nesta vaga..."
}
```

---

## 📁 Estrutura do Projeto

### **Backend**
```
src/main/java/com/jobs/jobboard/
├── config/
│   ├── security/          # JWT, Security Config
│   ├── CorsConfig.java
│   └── OpenApiConfig.java
├── controller/            # Endpoints REST
├── dto/
│   ├── request/          # DTOs de entrada
│   └── response/         # DTOs de saída
├── entity/               # Entidades JPA
├── exception/            # Tratamento de exceções
├── repository/           # Repositórios JPA
└── service/              # Lógica de negócio
```

### **Frontend**
```
src/
├── components/           # Componentes reutilizáveis
├── context/              # Context API (Auth)
├── pages/                # Páginas da aplicação
├── services/             # API client (Axios)
├── App.jsx
└── main.jsx
```

---

## 🗂️ Modelo de Dados

### **Entidades Principais**

```
User (Usuário)
├── id
├── name
├── email
├── password (criptografada)
├── role (CANDIDATE, COMPANY, ADMIN)
└── timestamps

Company (Empresa)
├── id
├── name
├── description
├── cnpj
├── website
├── user_id (FK)
└── timestamps

JobVacancy (Vaga)
├── id
├── title
├── description
├── location
├── status (OPEN, CLOSED, HIRED)
├── salaryMin
├── salaryMax
├── company_id (FK)
└── timestamps

Application (Candidatura)
├── id
├── candidate_id (FK → User)
├── job_vacancy_id (FK → JobVacancy)
├── coverLetter
├── status (PENDING, REVIEWING, APPROVED, REJECTED)
└── timestamps
```

---

## 🔒 Segurança

- ✅ Autenticação JWT stateless
- ✅ Senhas criptografadas com BCrypt
- ✅ Validação de senha forte (8+ caracteres, maiúsculas, minúsculas, números, especiais)
- ✅ Autorização baseada em roles
- ✅ CORS configurado
- ✅ Proteção contra SQL Injection (JPA/Hibernate)
- ✅ Soft delete para preservação de dados

---

## 🎨 Interface do Usuário

### **Páginas Disponíveis**

- 🔐 **Login / Cadastro** - Autenticação de usuários
- 💼 **Listagem de Vagas** - Busca e filtros
- 📄 **Detalhes da Vaga** - Informações completas + candidatura
- 📋 **Minhas Candidaturas** - Acompanhamento (candidatos)
- 🏢 **Dashboard Empresa** - Gerenciamento de vagas
- 📊 **Candidaturas Recebidas** - Gerenciamento de status (empresas)
- ⚙️ **Perfil da Empresa** - Configurações corporativas

---

## 🚧 Melhorias Futuras

- [ ] Upload de currículos (PDF)
- [ ] Sistema de notificações por email
- [ ] Chat entre candidato e empresa
- [ ] Filtros avançados (salário, localização, área)
- [ ] Sistema de avaliação de empresas
- [ ] Testes automatizados (JUnit, Mockito, Jest)
- [ ] CI/CD com GitHub Actions
- [ ] Deploy em produção (Railway, Vercel)

---

## 👨‍💻 Autor

**João Carlos Pereira de Mello**

- GitHub: [@joaocpmello](https://github.com/joaocpmello)
- LinkedIn: [João Carlos](https://www.linkedin.com/in/joao-cpmello/)
- Email: joaocpmello@hotmail.com

---

Projeto desenvolvido como parte do portfólio profissional para demonstração de habilidades em desenvolvimento full-stack com Java/Spring Boot e React.

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**
