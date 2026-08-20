Validação de Dados (Bean Validation)

"No projeto original, a API aceitava requisições com dados vazios. Eu criei um DTO de entrada (ClienteRequest) usando o spring-boot-starter-validation. Agora, o Spring valida se o nome está preenchido e se o CEP tem um formato válido antes de tentar consultar o ViaCEP, retornando 400 Bad Request se algo estiver errado."

Exclusão Lógica (Soft Delete)

"Em vez de deletar o registro do banco de dados com deleteById(), implementei o Soft Delete. Adicionei um atributo ativo na entidade Cliente. Quando o método de exclusão é chamado, a API apenas altera esse campo para false. Além disso, ajustei as buscas no repositório para ignorar registros inativos, mantendo a integridade dos dados no banco."

Tratamento de Exceção e Códigos HTTP Corretores (404)

"Ajustei a busca por ID para tratar cenários em que o cliente não existe ou foi desativado. Em vez de permitir uma exceção e estourar erro 500 Internal Server Error, a API agora trata a ausência do registro e responde com 404 Not Found, seguindo as boas práticas do padrão REST."
