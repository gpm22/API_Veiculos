# API Veículos :car: :car: :car:

## Descrição

API REST para controlar veículos de usuários. Basicamente trata-se de um sistema onde usuários podem se cadastrar e também cadastrar uma lista de veículos, sendo que o usuário pode realizar a leitura da própria lista de veículos.

O cadastro do usuário é realizado informando obrigatoriamento as seguintes informações:

1. Nome;
2. E-mail, que deve ser único para cada usuário;
3. CPF, que também deve ser único para cada usuário;
4. Data de nascimento;

Enquanto que o cadastro de um veículo é feito através do CPF ou email do usuário e informando obrigatoriamente as seguintes informações acerca do veículo:

1. Marca;
2. Modelo;
3. Ano;
4. Tipo de veículo:
   * Carros;
   * Motos;
   * *Caminhoes*;

Através do último dígito do ano do veículo a API irá guardar no banco de dados qual o dia da semana que o rodízio desse veículo será ativo utilizando os seguintes critérios:

1. Final 0-1: segunda-feira - 2
2. Final 2-3: terça-feira - 3
3. Final 4-5: quarta-feira - 4
4. Final 6-7: quinta-feira - 5
5. Final 8-9: sexta-feira - 6

Ao se solicitar a lista de veículos serão retornadas as informações do usuário junto da lista de veículos, sendo que as informações dos veículos a serem mostradas são as informações fornecidas durante o cadastro, o dia da semana em que o rodízio está ativo, se o rodízio está ativo no dia em que o retorno da lista foi requisitado e o preço do veículo que consta na tabela FIPE.

Desse modo, essa API consiste em 3 endpoints:

1. Cadastro de usuários;
2. Cadastro de veículos através do CPF ou email do usuário;
3. Retorno da lista de veículos através do CPF ou email do usuário;

O realizamento correto dos cadastros irá retornar *Status 201*, enquanto que a falha no cadastro retornará *Status 400*. Se a solicitação da lista de veículos for correta, o *Status 200* será retornado, enquanto irá retornar o *Status 404* se algo der errado. Em conjunto aos *status* de erro, mensagens explicando o que deu errado também serão mostradas.

## Tecnologias utilizadas

* Spring initializr;
  * Para inicializar o projeto;
* Spring Web;
  * Para realizar o controle das requisições HTTP que serão feitas a API REST;
* Spring Web Flex;
  * Para permiter a API realizar o consumo de uma API REST externa;
* H2 Database;
  * Para utilizar um banco de dados em memória;
* Spring Data JPA
  * Usado para criar um modelo que conecta objetos a elementos de um banco de dados relacional usando o **Hibernate**
  * Usado para ter acesso aos dados do banco de dados utilizando *queries* SQL através do **Java Persistence API (JPA)**
  * Desse modo, a API consegue acessar e alterar um certo banco de dados;

## Como usar

Para usar essa API é necessário, além de ter um clone local do atual repositório, ter o **Gradle** instalado. Para instalar o **Gradle** é só seguir os passos apresentados em https://gradle.org/install/.

Após instalar o **Gradle**, é só rodar o seguinte comando através de um terminal/console na pasta do projeto:

```bash
gradle bootRun
```

Após rodar esse comando, um servidor tomcat contendo a API será subido na porta **8080**. 

O sistema tem 4 end-points:

1. http://localhost:8080/apiveiculos/v1/usuario

   * Usado para cadastro de usuários através de um método post com corpo sendo um JSON seguindo o padrão:

     ```json
     {
         "name":"Luke Skywalker",    
      	"email":"greensaber@newrepublic.com",
         "cpf": "981.434.454-90",    
         "birthDate": "21/07/2000"
     }
     ```

     

2. http://localhost:8080/apiveiculos/v1/usuario/{cpf ou email}

   * Usado para recuperar todos os dados de um certo usuário;

3. http://localhost:8080/apiveiculos/v1/cadastrar-veiculo/{cpf ou Email}

   * Usado para cadastrar um certo veículo para um certo usuário;

   * O corpo deve conter um JSON no seguinte formato:

     ```json
     {    
         "brand":"HYUNDAI",    
         "model":"HD80 3.0 16V (diesel)(E5)",    
         "year": "2018",    
         "type": "caminhoes"}
     ```

   * Os valores do JSON devem ser os **mesmos** disponibilizados pela [FIPE API HTTP REST](https://deividfortuna.github.io/fipe/).

4. http://localhost:8080/apiveiculos/v1/lista-de-veiculos/{cpf ou email}

   * Usado para recuperar todos os veículos de um certo usuário;

# Exemplos

A seguir são mostrados alguns exemplo feitos utilizando o aplicativo [Postman](https://www.postman.com/).

## Cadastro de usuários

Fazendo 3 requisições *POST* separadas para http://localhost:8080/apiveiculos/v1/usuario, os seguintes usuários ilustres foram cadastrados:

```json
{
    "name":"Luke Skywalker",
    "email":"greensaber@newrepublic.com",
    "cpf": "981.434.454-90",
    "birthDate": "21/07/2000"
}

{
    "name":"Ash Ketchum",
    "email":"foreveryoung@bill.com",
    "cpf": "321.139.018-18",
    "birthDate": "20/02/1992"
}

{
    "name":"John Wick",
    "email":"lovemydog@neopets.com",
    "cpf": "439.518.752-45",
    "birthDate": "30/11/1978"
}
```

A resposta obtida para cada um desses cadastro foi:

```json
{
    "id": 1,
    "name": "Luke Skywalker",
    "email": "greensaber@newrepublic.com",
    "cpf": "981.434.454-90",
    "birthDate": "21/07/2000",
    "vehicles": null
}

{
    "id": 2,
    "name": "Ash Ketchum",
    "email": "foreveryoung@bill.com",
    "cpf": "321.139.018-18",
    "birthDate": "20/02/1992",
    "vehicles": null
}

{
    "id": 3,
    "name": "John Wick",
    "email": "lovemydog@neopets.com",
    "cpf": "439.518.752-45",
    "birthDate": "30/11/1978",
    "vehicles": null
}
```

Ao se tentar cadastrar algum usuário com algum desses emails o seguinte resultado aparece:

```java
Email: lovemydog@neopets.com já utilizado!
```

Tentativa de cadastro de algum CPF já cadastrado resulta em:

```
CPF: 439.518.752-45 já utilizado!
```

 ## Cadastro de veículos

Os seguintes veículso foram separamente cadastrados para o usuário **Ash Ketchum** ao se fazer separadas requisições *POST* para http://localhost:8080/apiveiculos/v1/cadastrar-veiculo/foreveryoung@biil.com:

```json
{
    "brand":"VW - VolksWagen",
    "model":"AMAROK High.CD 2.0 16V TDI 4x4 Dies. Aut",
    "year": "2014 Diesel",
    "type": "carros"
}

{
    "brand":"HYUNDAI",
    "model":"HD80 3.0 16V (diesel)(E5)",
    "year": "2018",
    "type": "caminhoes"
}
```

Que retornou a seguinte resposta:

```json
{
    "id": 4,
    "brand": "VW - VolksWagen",
    "model": "AMAROK High.CD 2.0 16V TDI 4x4 Dies. Aut",
    "year": "2014 Diesel",
    "type": "carros",
    "rotationDay": 4,
    "price": "R$ 102.451,00",
    "rotationActive": false
}

{
    "id": 7,
    "brand": "HYUNDAI",
    "model": "HD80 3.0 16V (diesel)(E5)",
    "year": "2018",
    "type": "caminhoes",
    "rotationDay": 6,
    "price": "R$ 90.515,00",
    "rotationActive": false
}
```

Mais dois veículos foram cadastrados para o nosso querido e imortal **Ash**, agora fazendo duas requisições *POST *seaparadas para http://localhost:8080/apiveiculos/v1/cadastrar-veiculo/321.139.018-18. Assim os seguintes veículos foram adicionados:

```json
{
    "brand":"Baby",
    "model":"Buggy RS Evolution 1.8 8V",
    "year": "2018 Gasolina",
    "type": "carros"
}

{
    "brand":"byCristo",
    "model":"Triciclo Star II Top / Super Top",
    "year": "2003",
    "type": "motos"
}

```

resultando em:

```json
{
    "id": 5,
    "brand": "Baby",
    "model": "Buggy RS Evolution 1.8 8V",
    "year": "2018 Gasolina",
    "type": "carros",
    "rotationDay": 6,
    "price": "R$ 36.828,00",
    "rotationActive": false
}

{
    "id": 6,
    "brand": "byCristo",
    "model": "Triciclo Star II Top / Super Top",
    "year": "2003",
    "type": "motos",
    "rotationDay": 3,
    "price": "R$ 16.945,00",
    "rotationActive": false
}
```

Tentar cadastrar um veículo com um email ou CPF não cadastrados resulta em:

```java
Não existe usuário com o email ou cpf: foreveryoungbill.com
```

## Solicitação da lista de veículos

Todos os veículos do persistente **Ash** foram requisitados através de uma requisição *GET* para http://localhost:8080/apiveiculos/v1/usuario/foreveryoung@bill.com, que resultou em:

```json
{
    "id": 2,
    "name": "Ash Ketchum",
    "email": "foreveryoung@bill.com",
    "cpf": "321.139.018-18",
    "birthDate": "20/02/1992",
    "vehicles": [
        {
            "id": 4,
            "brand": "VW - VolksWagen",
            "model": "AMAROK High.CD 2.0 16V TDI 4x4 Dies. Aut",
            "year": "2014 Diesel",
            "type": "carros",
            "rotationDay": 4,
            "price": "R$ 102.451,00",
            "rotationActive": false
        },
        {
            "id": 5,
            "brand": "Baby",
            "model": "Buggy RS Evolution 1.8 8V",
            "year": "2018 Gasolina",
            "type": "carros",
            "rotationDay": 6,
            "price": "R$ 36.828,00",
            "rotationActive": false
        },
        {
            "id": 6,
            "brand": "byCristo",
            "model": "Triciclo Star II Top / Super Top",
            "year": "2003",
            "type": "motos",
            "rotationDay": 3,
            "price": "R$ 16.945,00",
            "rotationActive": false
        },
        {
            "id": 7,
            "brand": "HYUNDAI",
            "model": "HD80 3.0 16V (diesel)(E5)",
            "year": "2018",
            "type": "caminhoes",
            "rotationDay": 6,
            "price": "R$ 90.515,00",
            "rotationActive": false
        }
    ]
}
```

Fazer a requisição para o CPF irá resultar na mesma respostas, como pode ser visto ao fazer uma requisição *GET* para

```json
{
    "id": 2,
    "name": "Ash Ketchum",
    "email": "foreveryoung@bill.com",
    "cpf": "321.139.018-18",
    "birthDate": "20/02/1992",
    "vehicles": [
        {
            "id": 4,
            "brand": "VW - VolksWagen",
            "model": "AMAROK High.CD 2.0 16V TDI 4x4 Dies. Aut",
            "year": "2014 Diesel",
            "type": "carros",
            "rotationDay": 4,
            "price": "R$ 102.451,00",
            "rotationActive": false
        },
        {
            "id": 5,
            "brand": "Baby",
            "model": "Buggy RS Evolution 1.8 8V",
            "year": "2018 Gasolina",
            "type": "carros",
            "rotationDay": 6,
            "price": "R$ 36.828,00",
            "rotationActive": false
        },
        {
            "id": 6,
            "brand": "byCristo",
            "model": "Triciclo Star II Top / Super Top",
            "year": "2003",
            "type": "motos",
            "rotationDay": 3,
            "price": "R$ 16.945,00",
            "rotationActive": false
        },
        {
            "id": 7,
            "brand": "HYUNDAI",
            "model": "HD80 3.0 16V (diesel)(E5)",
            "year": "2018",
            "type": "caminhoes",
            "rotationDay": 6,
            "price": "R$ 90.515,00",
            "rotationActive": false
        }
    ]
}
```

Requisições para CPFs ou emails inexistentes resulta em:

```java
Não existe usuário com o email ou cpf: 321.139.01818
```

Uma requisão *GET* para http://localhost:8080/apiveiculos/v1/usuario/lovemydog@neopets.com irá retornar os veículos do nosso querido amante dos animais John Wick:

```json
{
    "id": 3,
    "name": "John Wick",
    "email": "lovemydog@neopets.com",
    "cpf": "439.518.752-45",
    "birthDate": "30/11/1978",
    "vehicles": []
}
```

Um total incrível de 0 veículos, como o esperado! Isso demonstra que os veículos estão sendo cadastrados separadamente para cada usuário, como o programado.
