# Task API

API REST simples para gerenciamento de tarefas (tasks), construída em **Java puro**, sem uso de frameworks como Spring Boot.

> **Importante:** o objetivo principal deste projeto **não é o CRUD de tasks em si**, mas sim demonstrar a construção de uma **camada HTTP do zero em Java**, utilizando apenas a `com.sun.net.httpserver.HttpServer` (nativa do JDK) como base, com roteamento, tratamento de exceções, serialização JSON e camadas de aplicação implementados manualmente. O CRUD de tasks serve apenas como domínio de exemplo para exercitar essa camada.

## Tecnologias utilizadas

- **Java**
- **com.sun.net.httpserver.HttpServer** — servidor HTTP nativo do JDK, sem frameworks web
- **PostgreSQL** — banco de dados
- **Jackson** (`jackson-core`, `jackson-databind`, `jackson-datatype-jsr310`) — serialização/deserialização JSON
- **db-connection-manager** — biblioteca própria (via JitPack) para gerenciamento de conexões com o banco
- **Maven** — gerenciamento de dependências e build

## Arquitetura

O projeto segue uma separação em camadas inspirada em arquiteturas tradicionais (Controller → Service → Repository), mas toda a infraestrutura HTTP (servidor, roteador, request/response, status codes, tratamento de erros) é implementada manualmente, sem depender de nenhum framework web.

```
br.com.devmribeiro.taskapi
│
├── TaskApp.java                  # Ponto de entrada da aplicação (main)
│
├── config/
│   ├── PropertiesFileConfig.java # Leitura do application.properties
│   └── Log.java                  # Utilitário de logging (baseado em java.util.logging)
│
├── http/
│   ├── server/
│   │   ├── Server.java           # Inicializa o HttpServer nativo e registra o contexto raiz
│   │   ├── HttpRequest.java      # Abstração própria de requisição HTTP
│   │   ├── HttpResponse.java     # Abstração própria de resposta HTTP
│   │   ├── HttpMethod.java       # Enum com os métodos HTTP suportados (GET, POST, PUT, DELETE)
│   │   ├── HttpStatus.java       # Enum com os status HTTP suportados (código + descrição)
│   │   └── Json.java             # Wrapper sobre o Jackson para serializar/deserializar JSON
│   │
│   └── router/
│       └── Router.java           # Roteador manual: mapeia método + path para o Controller correto
│
├── controller/
│   └── TaskController.java       # Recebe a requisição já roteada e delega ao Service
│
├── service/
│   └── TaskService.java          # Regras de negócio e validações
│
├── repository/
│   └── TaskRepository.java       # Acesso ao banco via JDBC puro (PreparedStatement)
│
├── dto/
│   ├── TaskCreateDTO.java        # Dados de entrada para criação de task
│   ├── TaskUpdateDTO.java        # Dados de entrada para atualização de task
│   └── ApiResponse.java          # Formato padrão de resposta para erros/mensagens
│
├── model/
│   └── Task.java                 # Representação da entidade Task
│
├── types/
│   ├── TaskStatus.java           # Enum de status da task (TODO, IN_PROGRESS, DONE)
│   └── TaskPriority.java         # Enum de prioridade da task (LOW, MEDIUM, HIGH)
│
└── exception/
    ├── BadRequestException.java      # Exceção de negócio (erro 400)
    └── GlobalExceptionHandler.java   # Tratamento centralizado de exceções da aplicação
```

### Fluxo de uma requisição

1. **`TaskApp`** lê a porta configurada em `application.properties` (via `PropertiesFileConfig`) e inicializa o **`Server`**.
2. **`Server`** cria um `com.sun.net.httpserver.HttpServer` nativo do JDK e registra um único contexto (`"/"`), onde toda requisição recebida é repassada ao **`Router`**.
3. **`Router`** inspeciona manualmente o método HTTP e o path da requisição (`if/else` explícitos, sem anotações) e decide qual método do **`TaskController`** deve ser chamado, extraindo parâmetros de path (como o `taskId`) quando necessário.
4. **`TaskController`** converte o corpo da requisição (quando existir) de JSON para DTO usando a classe utilitária **`Json`** (baseada em Jackson) e delega a regra de negócio ao **`TaskService`**.
5. **`TaskService`** valida os dados recebidos e, se estiverem corretos, aciona o **`TaskRepository`**; caso contrário, lança uma **`BadRequestException`**.
6. **`TaskRepository`** executa o acesso ao PostgreSQL via JDBC puro (`PreparedStatement`), utilizando a lib `db-connection-manager` para obter/fechar conexões.
7. O retorno é encapsulado em um **`HttpResponse`** (classe própria, não do JDK) e devolvido até o **`Server`**, que escreve o status, headers e corpo diretamente na resposta HTTP.
8. Qualquer exceção lançada durante esse fluxo é capturada pelo **`GlobalExceptionHandler`**, que traduz o erro para um **`HttpStatus`** apropriado (400 para `BadRequestException`, 500 para erros inesperados) e retorna um corpo padronizado via **`ApiResponse`**.

### Destaques da camada HTTP sem framework

- **Servidor**: usa apenas `com.sun.net.httpserver.HttpServer`, disponível nativamente no JDK — não há Spring, Jetty, Tomcat embarcado, etc.
- **Roteamento manual**: o `Router` compara método HTTP e path "na mão", sem anotações como `@GetMapping`.
- **Modelo de Request/Response próprio**: `HttpRequest` e `HttpResponse` são classes criadas para abstrair a manipulação do `HttpExchange` do JDK.
- **Status HTTP tipado**: `HttpStatus` é um enum próprio que centraliza código e descrição de cada status usado.
- **Serialização JSON manual**: a classe `Json` centraliza o uso do Jackson (`ObjectMapper`), sem depender de conversores automáticos de framework.
- **Tratamento de erros centralizado**: `GlobalExceptionHandler` funciona como um "exception handler" manual, similar ao `@ControllerAdvice` do Spring, porém implementado à mão.

## Configuração

Antes de rodar o projeto, copie o arquivo de exemplo e ajuste os valores:

```bash
cp src/main/resources/application_example.properties src/main/resources/application.properties
```

```properties
PORT=8080

## Database connection
DB_URL=jdbc:postgresql://localhost:5432/task-db
DB_USER=postgres
DB_PASS=root
```

O banco de dados precisa da tabela `task`, criada pelo script:

```
src/main/resources/db/create_table_task.sql
```

## Endpoints

Base URL: `http://localhost:{PORT}`

| Método | Rota          | Descrição                          | Corpo (JSON)        | Sucesso |
|--------|---------------|-------------------------------------|----------------------|---------|
| GET    | `/tasks`      | Lista todas as tasks                | —                    | 200     |
| GET    | `/tasks/{id}` | Busca uma task pelo `id` (UUID)     | —                    | 200     |
| POST   | `/tasks`      | Cria uma nova task                  | `TaskCreateDTO`      | 201     |
| PUT    | `/tasks/{id}` | Atualiza uma task existente         | `TaskUpdateDTO`      | 200     |
| DELETE | `/tasks/{id}` | Remove uma task pelo `id` (UUID)    | —                    | 200     |

### Corpo das requisições

**`POST /tasks`** — `TaskCreateDTO`
```json
{
  "title": "Minha tarefa",
  "description": "Descrição da tarefa",
  "priority": "HIGH",
  "dueDate": "2026-12-31T23:59:59"
}
```

**`PUT /tasks/{id}`** — `TaskUpdateDTO`
```json
{
  "title": "Minha tarefa atualizada",
  "description": "Nova descrição",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "dueDate": "2026-12-31T23:59:59"
}
```

### Valores possíveis (enums)

- **`status`**: `TODO`, `IN_PROGRESS`, `DONE`
- **`priority`**: `LOW`, `MEDIUM`, `HIGH`

### Respostas de erro

Erros são retornados no formato `ApiResponse`:

```json
{
  "status": 400,
  "message": "Preencha os todos os campos corretamente",
  "timestamp": "18-09-2026 10:00:00"
}
```

- `400 Bad Request` — dados inválidos ou incompletos (`BadRequestException`)
- `404 Not Found` — rota não encontrada
- `500 Internal Server Error` — erro inesperado

## Como executar

```bash
mvn clean package
java -jar target/taskapi-0.0.1-SNAPSHOT.jar
```

O servidor iniciará na porta configurada em `application.properties` (padrão: `8080`).
