Projeto desenvolvido durante o curso de Java/Spring Boot Santander e aprimorado com boas práticas de desenvolvimento de APIs REST. A aplicação utiliza a integração com a API do ViaCEP para preenchimento automático de endereços e aplica padrões de projeto fundamentais da arquitetura Spring.

Melhorias Implementadas:
-Validação de Dados (Bean Validation): Criação de DTO de entrada (ClienteRequest) com validação de campos obrigatórios (@NotBlank) e formato do CEP via Regex.
-Exclusão Lógica (Soft Delete): Inclusão da propriedade ativo na entidade Cliente. A exclusão altera a flag para false, garantindo retenção de histórico no banco de dados e filtrando os registros inativos nas consultas.
-Tratamento de Respostas HTTP: Tratamento de buscas por ID inexistente ou inativo para retornar 404 Not Found em vez de exceções não tratadas (500).

Design Patterns Aplicados:
-Facade: Abstração e isolamento da comunicação com a API externa do ViaCEP.
-Strategy: Abstração da camada de persistência com Spring Data JPA.
-Singleton: Injeção de dependências e gerenciamento de Beans feito pelo Spring IoC.
