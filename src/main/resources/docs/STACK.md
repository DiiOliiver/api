
## Stack utilizada

**Back-end:** Java 17, Spring boot 3.4.3, Spring security 6 e PostgreSQL 14\

| Dependências |  Descrição  |
|:-----|:--------:|
| Spring Boot Starter Web   | Para criar APIs REST, fornecendo o núcleo do MVC e facilitando a configuração de servidores embutidos (Tomcat). |
| Spring Boot Starter Data JPA   |  Para a integração com o banco de dados via JPA/Hibernate, permitindo a persistência dos dados com repositórios. |
| Spring Boot Starter Security   | Para implementar segurança, autenticação e autorização utilizando JWT e os filtros personalizados. |
| Flyway   | Para gerenciar migrações de banco de dados, garantindo que a estrutura do banco seja versionada e consistente. |
| PostgreSQL Driver   | Para conectar a um banco de dados PostgreSQL. |
| Lombok   |Para reduzir o boilerplate (código repetitivo), facilitando a criação de getters, setters, construtores e outras operações. |
| Springdoc OpenAPI   | Para gerar automaticamente a documentação da API (Swagger UI), facilitando o entendimento e testes dos endpoints. |
| Guava   | Biblioteca do Google que fornece coleções avançadas, manipulação de strings e outras utilidades. |
| Gson   | Biblioteca para conversão entre objetos Java e JSON. |
| Hibernate Validator   |Implementação do Jakarta Bean Validation para validar dados de entrada e saída. |
| Spring Boot DevTools   | Ferramenta para facilitar o desenvolvimento, permitindo reload automático de código e melhorias na produtividade. |
| Spring Security Test   |  Para testar funcionalidades de segurança, permitindo simular autenticações e validar restrições de acesso em endpoints protegidos. |
| Spring Boot Starter OAuth2 Client   | Para permitir a autenticação OAuth 2.0 como cliente, facilitando a integração com provedores de identidade como Google, Facebook e HubSpot. |
| Spring Boot Starter OAuth2 Server   | Para configurar um servidor OAuth 2.0, possibilitando a emissão de tokens de acesso e a autenticação centralizada. |
| Spring Boot Starter WebFlux   | Para lidar com chamadas reativas e não bloqueantes, aumentando a escalabilidade em cenários de alto desempenho e consumo de APIs assíncronas. |
| Spring Boot Starter Test   | Para fornecer um conjunto completo de ferramentas para testes unitários e de integração, incluindo JUnit, Mockito e AssertJ. |

> [Instalação](INSTALACAO.md)
