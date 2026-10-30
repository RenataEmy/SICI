# Sistema Interno de Chamados IFRN - SICI - API REST

## 📄 O que é o projeto
Este projeto é uma API REST desenvolvida para centralizar e gerenciar o sistema de chamados de suporte do IFRN (Instituto Federal do Rio Grande do Norte). O objetivo é substituir os pedidos informais (via WhatsApp, e-mail ou pessoalmente) por um sistema estruturado onde é possível registrar, priorizar e manter o histórico de problemas de infraestrutura e TI, como falhas em projetores ou quedas de rede.

O fluxo base permite que um **solicitante** abra um chamado, um **técnico** de um setor responsável assuma e resolva a demanda, mantendo todas as interações e o ciclo de vida do chamado devidamente registrados.

## 🛠 Tecnologias
O projeto foi construído utilizando as seguintes tecnologias e frameworks:
- **Java 21**
- **Spring Boot 4** (Spring Web, Spring Data JPA)
- **PostgreSQL** (Banco de dados relacional)
- **Flyway** (Versionamento de banco de dados / Migrations)
- **MapStruct** (Mapeamento de objetos para linguagem Java)
- **Lombok** (Redução de boilerplate de código)
- **Docker & Docker Compose** (Containerização e orquestração)
- **Maven** (Gerenciamento de dependências)

### ⚙️ Sobre o uso do Flyway (Nota de Estudo)
Neste projeto, a criação e alteração do banco de dados não são feitas pelo Hibernate. Utilizamos a propriedade `spring.jpa.hibernate.ddl-auto=validate` combinada com o **Flyway**.
- **Como funciona:** O Flyway cria uma tabela chamada `flyway_schema_history` no banco. Ela guarda o registro de todas as migrations (arquivos SQL) que já foram aplicadas.
- **Checksums:** Cada arquivo de migration possui um *checksum* (uma assinatura gerada a partir do seu conteúdo). Se você tentar editar uma migration que já foi aplicada, o Flyway detectará a diferença no checksum ao iniciar a aplicação e bloqueará a execução, garantindo a integridade e rastreabilidade da base de dados. Correções devem sempre ser feitas em novas versões de arquivos (ex: `V3__correcao.sql`).
- **Por que não usar `ddl-auto=update` em produção?** Em ambientes produtivos, usar o *update* automático do Hibernate é extremamente perigoso, pois ele pode alterar colunas ou apagar estruturas de forma imprevisível, causando perda de dados. As migrations oferecem controle absoluto, explícito e seguro sobre a evolução do *schema* do banco de dados.

## 📥 Como baixar
Para baixar o projeto em sua máquina, abra o terminal e execute o comando abaixo para clonar o repositório:

```bash
git clone https://github.com/RenataEmy/SICI.git
cd SICI
```

## 🚀 Como rodar local e no Docker

### Rodando via Docker (Recomendado)
A maneira mais fácil de rodar o projeto sem precisar configurar o banco de dados na sua máquina é utilizando o Docker.
Na raiz do projeto (onde está o arquivo `docker-compose.yml`), execute:

```bash
docker-compose up --build -d
```
A API subirá automaticamente e estará disponível em `http://localhost:8080`. O banco de dados também será inicializado, com as tabelas criadas e os dados iniciais injetados pelo Flyway.

### Rodando Localmente (Para desenvolvimento)
1. Certifique-se de ter o **PostgreSQL** instalado e rodando.
2. Crie um banco de dados vazio para a aplicação (ex: `chamados_db`).
3. Configure as variáveis de ambiente ou ajuste o arquivo `application-dev.properties` com suas credenciais do banco:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/chamados_db
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```
4. Execute o projeto utilizando o Maven:
   ```bash
   mvn spring-boot:run
   ```

## 📋 Requisitos Funcionais e Regras de Negócio

### Regras Gerais
1. Todo chamado nasce com status **ABERTO**, `dataAbertura` gerada pelo sistema e prioridade **MEDIA** (quando não informada).
2. O ciclo de vida de um chamado é: `ABERTO` -> `EM_ATENDIMENTO` -> `RESOLVIDO`. Ou `ABERTO` -> `CANCELADO`. Outras transições são proibidas.
3. Chamados **RESOLVIDO** ou **CANCELADO** não aceitam alterações nem novas interações.
4. Apenas o solicitante e o técnico atribuído podem registrar interações no chamado.
5. Não é permitido excluir um setor ou categoria que já tenha chamados vinculados.
6. A identificação do usuário que realiza a ação deve vir no corpo da requisição (ex: `solicitanteId`, `tecnicoId`).

### Papéis e Restrições Específicas
- **Solicitantes:** Apenas usuários do tipo `SOLICITANTE` podem abrir chamados. Somente o próprio solicitante pode cancelar seu chamado (apenas se estiver com status ABERTO).
- **Técnicos:** Um técnico só pode assumir um chamado ABERTO se a categoria do chamado pertencer ao setor do técnico. Ao assumir, o chamado muda para `EM_ATENDIMENTO`. Somente o técnico atribuído pode resolver o chamado, informando obrigatoriamente a solução.
- **Administradores / Cadastros Base:**
    - Só é possível cadastrar setores e categorias se for um usuário **ADMINISTRADOR**.
    - Só é possível cadastrar uma **categoria** se ela tiver um **setor** vinculado.
    - É perfeitamente possível cadastrar um **setor sem categoria** vinculada inicialmente.
    - Ao deletar um setor (que não tenha chamados), os usuários (técnicos) e categorias que pertenciam a ele ficarão com o campo de setor **nulo** (sem setor vinculado).

## 🛣 Rotas Existentes

Todas as rotas de listagem (`GET` que retorna coleção) são paginadas e aceitam filtros por *query string* (ex: `?page=0&size=10&sort=dataAbertura,desc`).

### Cadastros Básicos
| Entidade | Método | Rota | Descrição |
| :--- | :--- | :--- | :--- |
| **Setores** | `POST`, `GET`, `PUT`, `DELETE` | `/setores` | Gerencia os setores (Filtro GET: `nome`) |
| **Categorias**| `POST`, `GET`, `PUT`, `DELETE` | `/categorias` | Gerencia as categorias (Filtros GET: `nome`, `setorId`) |
| **Categorias**| `GET` | `/setores/{id}/categorias` | Lista as categorias de um setor específico |
| **Usuários** | `POST`, `GET`, `PUT` | `/usuarios` | Gerencia os usuários (Filtros GET: `nome`, `tipo`, `setorId`) |

### Chamados e Interações
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/chamados` | Abre um novo chamado |
| `GET` | `/chamados` | Lista chamados paginados com múltiplos filtros |
| `GET` | `/chamados/{id}` | Detalha um chamado e suas interações |
| `GET` | `/usuarios/{id}/chamados` | Lista chamados de um usuário específico (Filtro: `status`) |
| `PATCH` | `/chamados/{id}/assumir` | Um técnico assume o chamado |
| `PATCH` | `/chamados/{id}/resolver` | Técnico resolve o chamado (exige solução) |
| `PATCH` | `/chamados/{id}/cancelar` | Solicitante cancela o chamado |
| `POST` | `/chamados/{id}/interacoes`| Registra uma nova interação em um chamado |

## 💡 Exemplo de Rota

Abaixo está um exemplo de como fazer uma requisição para a rota de **Abertura de Chamado**.

**Endpoint:** `POST /chamados`

**Corpo da Requisição (JSON):**
```json
{
  "title": "Projetor não liga na sala 10",
  "description": "O projetor da sala 10 não está ligando, a luz vermelha fica piscando mas não projeta imagem.",
  "location": "Sala 10 - Bloco A",
  "priority": "ALTA",
  "categoryId": 2,
  "requestId": 5
}
```

**Exemplo de Resposta de Sucesso (Status 201 Created):**
```json
{
  "id": 12,
  "title": "Projetor não liga na sala 10",
  "description": "O projetor da sala 10 não está ligando, a luz vermelha fica piscando mas não projeta imagem.",
  "location": "Sala 10 - Bloco A",
  "priority": "ALTA",
  "status": "ABERTO",
  "openDate": "2026-10-29T10:30:00Z",
  "category": {
    "id": 2,
    "name": "Equipamento Audiovisual"
  },
  "request": {
    "id": 5,
    "name": "Maria Silva"
  }
}
```

## 📦 Como usar a Collection do Postman

Para facilitar os testes, foi incluída uma collection do Postman na raiz do projeto (arquivo `Chamados-IFRN-API.postman_collection.json`).

1. Abra o **Postman** (ou Insomnia).
2. Clique no botão **"Import"** no canto superior esquerdo.
3. Selecione o arquivo `postman/SICI.postman_collection.json` e `postman/SICI.postman_environment.json` presente neste repositório.
4. As rotas estarão organizadas por pastas (Setores, Usuários, Chamados). Basta abrir, alterar os IDs conforme necessário no corpo (`Body`) da requisição e clicar em **Send**.
   *(Nota: Certifique-se de que a API local ou no Docker esteja rodando ao disparar as requisições).*