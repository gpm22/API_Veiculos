# API Veículos :car: :car: :car:

## Descrição da API

É uma API RESTful para controlar veículos de usuários, onde usuários podem se cadastrar e também cadastrar uma lista de veículos para consulta posterior, sendo que o sistema irá adicionar ao veículo o seu preço e informações sobre rodízio. Um usuário também é capaz de recuperar suas informações, remover algum veículo da lista, editar suas informações pessoais, deletar o próprio perfil, recuperar todos os veículos cadastrados na API e recuperar um veículo em específico. 

## Tecnologias utilizadas

* **Spring initializr;**
  * Para inicializar o projeto;
* **Spring Web;**
  * Para realizar o controle das requisições HTTP que serão feitas a API REST;
* **Spring Web Flex;**
  * Para permiter a API realizar o consumo de uma API REST externa;
* **H2 Database;**
  * Para utilizar um banco de dados em memória;
* **Spring Data JPA;**
  * Para fazer o ORM e a comunicação com o banco de dados;

## Iniciando o Sistema

Para usar essa API é necessário, além de ter um clone local do atual repositório, ter o **Gradle** instalado. Para instalar o **Gradle** é só seguir os passos apresentados em https://gradle.org/install/.

Após instalar o **Gradle**, é só rodar o seguinte comando através de um terminal/console na pasta do projeto:

```bash
gradle bootRun
```

Após rodar esse comando, um servidor tomcat contendo a API será subido na porta **8080**. 

## Cadastro de usuários

O cadastro do usuário é realizado enviando uma requisição **POST** ao endpoint **/apiveiculos/v1/usuario/**, sendo que no corpo da requisição deve conter um **JSON** com as seguintes informações obrigatórias:

1. **Nome;**
2. **E-mail**, que deve ser único para cada usuário;
3. **CPF**, que também deve ser único para cada usuário;
4. **Data de nascimento;**

Exemplo:

```json
{ 
    "name":"Luke Skywalker",    
 	"email":"greensaber@newrepublic.com",    
    "cpf": "126.764.550-44",    
    "birthDate": "21/07/2000"
}
```

Se o cadastro der certo, uma resposta com status **201** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Recuperando um Usuário

Para recuperar um usuário cadastrado específico é só enviar uma requisição **GET** ao endpoint **/apiveiculos/v1/ usuario/{email_ou_cpf}**, onde no lugar de **{email_ou_cpf}** deve estar o email ou cpf do usuário a ser retornado. Se a solicitação der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **404**.

## Adicionando um Veículo ao Usuário

Para adicionar um veículo a um usuário, primeiramente o veículo deve já estar cadastrado no sistema. Para recuperar a lista de veículos cadastrados é só enviar uma requisição **GET** ao endpoint **/apiveiculos/v1/veiculo/**. Agora, sabendo as informações do veículo a ser adicionado, apenas é preciso enviar uma requisição **PUT** ao endpoint **/apiveiculos/v1/ usuario/{email_ou_cpf}/registro-veiculo/{vehicle_id}**, sendo que no lugar de **{email_ou_cpf}** deve estar o email ou cpf do usuário e no lugar de **{vehicle_id}** deve estar a id do veículo a ser adicionado. Se a adição der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Removendo um Veículo de um Usuário

Para remover um veículo de um usuário, apenas é preciso enviar uma requisição **DELETE** ao endpoint **/apiveiculos/v1/ usuario/{email_ou_cpf}/registro-veiculo/{vehicle_id}**, sendo que no lugar de **{email_ou_cpf}** deve estar o email ou cpf do usuário e no lugar de **{vehicle_id}** deve estar a id do veículo a ser removido. Se a remoção der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Atualização dos Dados de um Usuário

A atualização dos dados  de um usuário é realizada enviando uma requisição **PUT** ao endpoint **/apiveiculos/v1/usuario/{email_ou_cpf}**, sendo que no lugar de **{email_ou_cpf}** deve estar o email ou cpf do usuário a ser atualizado e o corpo da requisição deve conter um **JSON** com todas informações do usuário, mesmo as inalteradas, que são :

1. **Nome;**
2. **E-mail;**
3. **CPF;**
4. **Data de nascimento;**

Se a atualização der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Deletando um usuário

Para deletar um usuário, apenas é preciso enviar uma requisição **DELETE** ao endpoint **/apiveiculos/v1/usuario/ {email_ou_cpf}**, sendo que no lugar de **{email_ou_cpf}** deve estar o email ou cpf do usuário a ser deltado. Se a remoção der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Cadastro de Veículos

O cadastro de um veículo é realizado enviando uma requisição **POST** ao endpoint **/apiveiculos/v1/veiculo/**, sendo que no corpo da requisição deve conter um **JSON** com as seguintes informações obrigatórias:

1. **Marca**;
2. **Modelo**;
3. **Ano**;
4. **Tipo de veículo**, que deve ser um dos seguintes:
   * carros;
   * motos;
   * caminhoes;
   * fipe;

Exemplo:

```json
{    
    "brand":"HYUNDAI",    
    "model":"HD80 3.0 16V (diesel)(E5)",    
    "year": "2018",    
    "type": "caminhoes"
}
```

Sendo que essas informações devem estar iguais as contidas na [API FIPE](https://github.com/deividfortuna/fipe), pois essa API será usada na retornar o preço do veículo. Essa api foi escolhida, pois a [tabela FIPE](https://veiculos.fipe.org.br/) contém os preços médios dos veículos anunciados pelos vendedores no mercado brasileiro. 

Após o sistema receber a requisição, os seguintes processos serão executados para o cadastro das informações sobre o preço do veículo e rodízio de veículos:

1. O dia do ródizio é obtido através do último dígito do ano do veículo utilizando os seguintes critérios:

   - Final 0-1: segunda-feira - 2

   - Final 2-3: terça-feira - 3

   - Final 4-5: quarta-feira - 4

   - Final 6-7: quinta-feira - 5

   - Final 8-9: sexta-feira - 6

2. Então o dia do rodízio é comparado com o dia atual para preenchimento do atributo **isRotationActive**;

3. Por fim, o preço do veículo é obtido através da  [API FIPE](https://github.com/deividfortuna/fipe).

Se o cadastro der certo, uma resposta com status **201** será retornada, já se de errado, será retornada uma resposta com status **400**.

## Recuperando Todos os Veículos

Para recuperar a lista de veículos cadastrados é só enviar uma requisição **GET** ao endpoint **/apiveiculos/v1/veiculo/**. Se a solicitação der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **404**.

## Recuperando um Veículo

Para recuperar um veículo cadastrado específico é só enviar uma requisição **GET** ao endpoint **/apiveiculos/v1/ veiculo/{id}**, onde no lugar de **{id}** deve estar a id do veículo a ser retornado. Se a solicitação der certo, uma resposta com status **200** será retornada, já se de errado, será retornada uma resposta com status **404**.

## Endpoints

A seguir estão os endpoints e os métodos http disponíveis para cada um:

* **/apiveiculos/v1/usuario/**
  * OPTIONS
  * POST
* **/apiveiculos/v1/usuario/{email_ou_cpf}**
  * OPTIONS
  * GET
  * PUT
  * DELETE
* **/apiveiculos/v1/usuario/{email_ou_cpf}/registro-veiculo/{vehicle_id}**
  * OPTIONS
  * PUT
  * DELETE
* **/apiveiculos/v1/veiculo/**
  * OPTIONS
  * POST
  * GET
* **/apiveiculos/v1/veiculo/{id}**
  * OPTIONS
  * GET
