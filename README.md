# SICI — Sistema de Chamados Institucionais

API REST para registrar, distribuir e acompanhar chamados de suporte. O sistema organiza solicitações por categoria e setor, permite que técnicos assumam e resolvam chamados e mantém interações associadas ao atendimento.

## Tecnologias

- Java 21
- Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA e Bean Validation
- PostgreSQL 16 e Flyway
- Maven e MapStruct
- Docker e Docker Compose

> A versão efetivamente configurada no projeto é Java 21 / Spring Boot 4.1.1.

## Execução com Docker

Pré-requisitos: Docker Engine/Desktop e Docker Compose.

Na raiz do repositório:

```bash
docker compose up --build
```

O Compose inicia o PostgreSQL, aguarda o banco ficar saudável e então compila e inicia a API. A API fica disponível em `http://localhost:8080`; o PostgreSQL é publicado em `localhost:5432`. As credenciais locais de desenvolvimento estão definidas no `docker-compose.yml`.

Para parar os containers:

```bash
docker compose down
```

Para remover também o volume persistente do banco (apaga os dados locais):

```bash
docker compose down -v
```

O build da imagem executa `mvn clean package -DskipTests`. Para executar localmente fora do Docker, configure `DB_URL`, `DB_USER` e `DB_PASS` para um PostgreSQL e rode `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).

## API e identificação de usuários

A API não usa Spring Security nem tokens. Quando uma ação precisa identificar quem a executa, o ID é enviado no JSON do corpo, em campos como `requestId`, `techId`, `userId` ou `adminId`. Operações cujo contrato atual não recebe um usuário, como exclusão de categoria, não exigem esse campo.

Paginação usa os parâmetros `page` e `size`. Os exemplos de status válidos são `ABERTO`, `EM_ATENDIMENTO`, `RESOLVIDO` e `CANCELADO`; prioridades: `BAIXA`, `MEDIA` e `ALTA`.

### Chamados

| Método e rota | Finalidade | Exemplo de corpo |
|---|---|---|
| `POST /chamados` | Abre chamado | `{"title":"Projetor sem imagem","description":"O projetor não liga","location":"Sala 12","priority":"MEDIA","categoryId":1,"requestId":10}` |
| `GET /chamados` | Lista com filtros/paginação | Query opcional: `status=ABERTO&priority=ALTA&sectorId=1&page=0&size=20` |
| `GET /chamados/{id}` | Detalha chamado e interações | — |
| `PUT /chamados/{id}/assumir` | Técnico assume chamado | JSON numérico, por exemplo `12` (`techId`) |
| `PUT /chamados/{id}/resolver` | Técnico informa solução | `{"techId":12,"solution":"Cabo HDMI substituído"}` |
| `PUT /chamados/{id}/cancelar` | Solicitante cancela chamado aberto | JSON numérico, por exemplo `10` (`requestId`) |
| `POST /chamados/{id}/interacoes` | Registra interação | `{"text":"O equipamento está disponível para atendimento","userId":10}` |

Resposta resumida de `POST /chamados` (201):

```json
{
  "ticketId": 31,
  "title": "Projetor sem imagem",
  "description": "O projetor não liga",
  "location": "Sala 12",
  "priority": "MEDIA",
  "status": "ABERTO",
  "requestId": 10,
  "categoryId": 1,
  "techId": null
}
```

### Categorias

| Método e rota | Finalidade | Exemplo de corpo |
|---|---|---|
| `POST /categorias` | Cria categoria (administrador) | `{"name":"Equipamentos","description":"Projetores e computadores","sectorId":1,"adminId":2}` |
| `GET /categorias` | Lista com filtros/paginação | Query opcional: `name=equip&sectorId=1&page=0&size=20` |
| `GET /categorias/{id}` | Busca categoria | — |
| `PUT /categorias/{id}` | Atualiza categoria (administrador) | `{"name":"Equipamentos","description":"Equipamentos audiovisuais","sectorId":1,"adminId":2}` |
| `DELETE /categorias/{id}` | Exclui categoria sem chamados vinculados | — |

Resposta resumida de categoria:

```json
{"categoryId":1,"name":"Equipamentos","description":"Projetores e computadores","sectorId":1}
```

### Setores

| Método e rota | Finalidade | Exemplo de corpo |
|---|---|---|
| `POST /setores` | Cria setor (administrador) | `{"name":"Tecnologia da Informação","acronym":"TI","categoriesId":[],"techsId":[],"adminId":2}` |
| `GET /setores` | Pesquisa/pagina setores | O contrato atual lê o nome do setor como corpo de texto; paginação por `page` e `size`. |
| `GET /setores/{id}` | Busca setor | — |
| `GET /setores/{id}/categorias` | Lista categorias do setor | — |
| `PUT /setores/{id}` | Atualiza setor (administrador) | `{"name":"Tecnologia","acronym":"TI","categoriesId":[1],"techsId":[12],"adminId":2}` |
| `DELETE /setores/{id}` | Exclui setor sem chamados associados | — |

Resposta resumida de setor:

```json
{"sectorId":1,"name":"Tecnologia da Informação","acronym":"TI","categoriesId":[1],"techsId":[12]}
```

### Usuários

| Método e rota | Finalidade | Exemplo de corpo |
|---|---|---|
| `POST /usuarios` | Cria usuário | `{"name":"Ana Silva","email":"ana@example.org","registration":1234,"type":"SOLICITANTE","ticketsId":[],"sectorId":null}` |
| `GET /usuarios` | Pesquisa/pagina usuários | Query opcional: `name=ana&type=SOLICITANTE&sectorId=1&page=0&size=20` |
| `GET /usuarios/{id}` | Busca usuário | — |
| `GET /usuarios/{id}/chamados` | Lista chamados do usuário; filtro opcional de status | Query: `status=ABERTO&page=0&size=20` |
| `PUT /usuarios/{id}` | Atualiza usuário | Mesmo formato do `POST /usuarios`. |

## Postman

Importe `postman/SICI.postman_collection.json` e `postman/SICI.postman_environment.json`. Selecione o ambiente **SICI Local**; ele define `base_url` como `http://localhost:8080`. A collection contém exemplos para todos os endpoints implementados.

## Formato de erros

Erros são respondidos como JSON com `timestamp`, `status`, `message` e `path`. Validação de entrada retorna 400, recurso inexistente 404 e conflitos/regras de negócio 409.
