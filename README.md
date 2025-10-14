# **Workshop Spring Boot JPA - Sistema de Gerenciamento de Pedidos**

Uma API RESTful completa para gerenciamento de pedidos, desenvolvida com Spring Boot e Spring Data JPA para fins de estudo.

## **Sobre o Projeto**

Este projeto implementa um sistema completo de gerenciamento de pedidos com funcionalidades para:

- Cadastro de usuários, produtos e categorias
- Criação e gerenciamento de pedidos
- Controle de itens de pedido
- Processamento de pagamentos
- Relacionamentos complexos entre entidades

## **Tecnologias Utilizadas**

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **PostgreSQL**
- **Maven**
- **Jakarta Persistence**
- **RESTful Web Services**

## **Estrutura do Projeto**


```
workshop-springboot-jpa/
├── src/main/java/com/fudaliarthur/webservices/
│   ├── entities/              # Entidades JPA
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Payment.java
│   │   ├── User.java
│   │   ├── Product.java
│   │   └── Category.java
│   ├── repositories/          # Interfaces Spring Data JPA
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   ├── PaymentRepository.java
│   │   ├── UserRepository.java
│   │   ├── CategoryRepository.java
│   │   └── ProductRepository.java
│   ├── services/              # Camada de serviço
│   │   ├── OrderService.java
│   │   ├── CategoryService.java
│   │   ├── ProductService.java
│   │   └── UserService.java
│   ├── resources/             # Controladores REST
│   │   ├── OrderResource.java
│   │   ├── CategoryResource.java
│   │   ├── ProductResource.java
│   │   └── UserResource.java
│   ├── dto/                   # Data Transfer Objects
│   │   ├── OrderRequestDTO.java
│   │   ├── OrderItemDTO.java
│   │   └── OrderItemRequestDTO.java
│   └── exceptions/            # Tratamento de exceções
│       |── StandarError.java
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties
```

## **Modelo de Dados**

### **Entidades Principais**

- **User**: Clientes do sistema
- **Product**: Produtos disponíveis para venda
- **Order**: Pedidos com status e dados do cliente
- **OrderItem**: Itens individuais de um pedido
- **Payment**: Pagamentos associados aos pedidos
- **Category**: Categorias de produtos

### **Relacionamentos**

- `User` (1) ↔ (N) `Order` - Um usuário pode ter vários pedidos
- `Order` (1) ↔ (N) `OrderItem` - Um pedido pode ter vários itens
- `OrderItem` (N) ↔ (1) `Product` - Um item referencia um produto
- `Order` (1) ↔ (1) `Payment` - Um pedido tem um pagamento
- `Product` (N) ↔ (N) `Category` - Um produto pode ter várias categorias

## **Configuração e Execução**

### **Pré-requisitos**

- Java 17 ou superior
- Maven 3.6+
- IDE de sua preferência (Spring Tools, IntelliJ, VS Code, Eclipse)

### **Instalação**

1. **Clone o repositório**

    
    ```
    git clone https://github.com/arthurfudali/workshop-springboot-jpa.git
    cd workshop-springboot-jpa
    ```
    
2. **Configure o banco de dados**
    - O projeto usa H2 Database em memória
    - Configurações no `application.properties` -> perfil `test`
    - Para uso de Postgre, mude para o perfil `dev`
3. **Execute a aplicação**

    
    ```
    mvn spring-boot:run
    ```
    
4. **Acesse a aplicação**
    - API: [http://localhost:8080](http://localhost:8080/)
    - H2 Console: http://localhost:8080/h2-console
        - JDBC URL: `jdbc:h2:mem:testdb`
        - Username: `sa`
        - Password: (vazio)

## **Endpoints da API**

### **Exemplo: Pedidos (Orders)**

Documentação completa em: http://localhost:8080/swagger-ui/index.html#/

| **Método** | **Endpoint** | **Descrição** |
| --- | --- | --- |
| `GET` | `/orders` | Lista todos os pedidos |
| `GET` | `/orders/{id}` | Busca pedido por ID |
| `POST` | `/orders` | Cria novo pedido com itens |
| `POST` | `/orders/{id}/payment` | Adiciona pagamento ao pedido |
| `DELETE` | `/orders/{id}` | Exclui um pedido |

### **Exemplo de Uso**

**Criar Pedido:**

bash

```
POST http://localhost:8080/orders
Content-Type: application/json

{
  "moment": "2024-01-15T10:30:00Z",
  "orderStatus": "WAITING_PAYMENT",
  "clientId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 90.5
    },
    {
      "productId": 3,
      "quantity": 1,
      "price": 1250.0
    }
  ]
}
```

**Adicionar Pagamento:**

bash

```
POST http://localhost:8080/orders/1/payment
```

## **Funcionalidades Implementadas**

### **Criar Pedido com Múltiplos Itens**

- Criação atômica de pedido com todos os itens
- Validação de estoque e preços
- Cálculo automático de totais

### **Gestão de Pagamentos**

- Associação de pagamentos a pedidos
- Controle de status do pedido
- Prevenção de pagamentos duplicados

### **Tratamento de Erros**

- Respostas padronizadas para erros
- Status HTTP apropriados
- Mensagens descritivas para clientes da API

### **Boas Práticas**

- Injeção de dependência por construtor
- Padrão DTO para transferência de dados
- Transações com `@Transactional`
- Mapeamento JPA com relacionamentos

## **Exemplos de Resposta**

**Pedido Criado com Sucesso (201 Created):**

json

```
{
  "id": 4,
  "moment": "2024-01-15T10:30:00Z",
  "orderStatus": "WAITING_PAYMENT",
  "client": {
    "id": 1,
    "name": "Maria Brown",
    "email": "maria@gmail.com",
    "phone": "988888888"
  },
  "items": [
    {
      "price": 90.5,
      "quantity": 2,
      "subTotal": 181.0,
      "product": {
        "id": 1,
        "name": "The Lord of the Rings",
        "price": 90.5,
        "categories": [
          {
            "id": 2,
            "name": "Books"
          }
        ]
      }
    }
  ],
  "payment": null,
  "total": 181.0
}
```

**Erro de Validação (409 Conflict):**

json

```
{
  "timestamp": "2024-01-15T10:35:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Este pedido já possui um pagamento associado.",
  "path": "/orders/2/payment"
}
```

## **Desenvolvimento**

### **Padrões de Código**

- Nomenclatura consistente em inglês
- Commits semânticos
- Código documentado

### **Próximas Melhorias**

- Implementar autenticação JWT
- Adicionar testes unitários e de integração
- Implementar paginação nas consultas
- Adicionar documentação Swagger/OpenAPI
- Configurar Docker e Docker Compose
- Deploy no Render

## **Licença**

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](https://github.com/arthurfudali/workshop-springboot-jpa/blob/main/LICENSE) para mais detalhes.
