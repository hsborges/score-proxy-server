# Score Proxy Server

Este projeto é um servidor proxy REST para consulta de score, desenvolvido em Spring Boot, com ênfase em modularidade e aplicação de padrões de projeto.

## Funcionalidades

- Endpoint `/proxy/score?cpf=...` que encaminha requisições para um serviço upstream.
- Documentação automática via OpenAPI/Swagger.
- Modularização e extensibilidade por meio de interfaces e decorators.

## Padrões de Projeto Aplicados

### 1. **Strategy (Interfaces Client e Cache)**

A interface `Client` define a estratégia para obtenção do score e `Cache` permite a implementação de diferentes estratégias de *caching*.

### 2. **Decorator (ClientDecorator)**

O padrão Decorator é utilizado para adicionar funcionalidades ao client principal sem alterar seu código:

- `ClientDecorator`: classe abstrata base para decorators.
- `RateLimitingDecorator`: limita a taxa de requisições (1 por segundo) usando fila e thread dedicada, implementando também o padrão Command para desacoplar a execução.
- `CachingDecorator`: adiciona cache em memória para respostas, utilizando a interface `Cache`.

### 3. **Command (Command)**

O padrão Command é aplicado no `RateLimitingDecorator`, encapsulando cada requisição como um comando que pode ser enfileirado e executado de forma assíncrona.

### 4. **Configuration/Dependency Injection (Spring)**

A composição dos clients e decorators é feita via configuração Spring (`@Configuration` e `@Bean`), permitindo fácil extensão e troca de implementações.

### 5. **Properties/Validation**

Configurações como `clientId` e `baseUrl` são injetadas via `@ConfigurationProperties` e validadas com Bean Validation.

## Estrutura dos Principais Arquivos

- `Client`: interface para obtenção de score.
- `UpstreamClient`: implementação que faz a chamada HTTP real.
- `ClientDecorator`: base para decorators.
- `RateLimitingDecorator`: decorator para limitação de taxa.
- `CachingDecorator`: decorator para cache.
- `Cache`/`InMemoryCache`: abstração e implementação de cache.
- `ProxyController`: expõe o endpoint REST.
- `ClientConfiguration`: compõe os decorators e client principal.
- `AppProperties`: configurações externas.

## Como Executar

1. Configure as propriedades via variáveis de ambiente:

   ```sh
   export PROXY_CLIENT_ID=SEU_CLIENT_ID
   export PROXY_BASE_URL=https://score.hsborges.dev/api
   ```

2. Execute o projeto com Maven:

   ```sh
   ./mvnw spring-boot:run
   ```

3. Acesse a documentação em `/docs`.

---

Projeto acadêmico - Padrões de Projeto com Spring Boot.
