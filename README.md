<h1>Projeto API VollMed</h1>
Projeto da API VollMed aprendida no alura<br><br>

Feito com JDK 17, Maven, Spring Boot 3.4.1<br>
Conta com banco de testes H2 embarcado<br><br>

O Link para a workspace no Postman usado para testar a API:<br>
https://www.postman.com/technical-astronaut-9928490/workspace/vollmed-api<br><br>

<h2>Como Testar?</h2>
Clone o projeto e execute o comando <code>mvn clean spring-boot:run</code> na pasta raiz do projeto<br>
Execute as collections na workspace do postman para testar<br><br>

Para verificar os testes unitários, execute <code>mvn clean test</code> para a bateria de testes<br> ou <code>mvn clean test -Dtest=NomeDaClasseDeTeste</code> para um teste específico<br>
Ex: <code>mvn clean test -Dtest=MedicoServiceTest</code><br><br>

<h2>Outras observações</h2>
Além dos testes automatizados dentro do projeto, quero automatizar as requisições no postman para simplificar a bateria de testes, mas ficará para um momento posterior
