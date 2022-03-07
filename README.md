# Vehicles API :car: :car: :car:

Versão em português disponível em https://github.com/gpm22/API_Veiculos/blob/master/LEIAME.md.

## API Description

It is a RESTful API for management of users' vehicles, wherein users can register themselves and their vehicles in the system for later reference. The system will also add the price and vehicle rotation day to the vehicle info. Users will be capable of retrieve their own information, remove a vehicle from their list, update their personal information, delete their own profile, retrieve all vehicles registered in the API, and retrieve a specific registered vehicle.

## Technologies used

* Spring initializr;
  * To initialize the project;
* Spring Web;
  * To do the control of HTTP requests that will be received by the API;
* Spring Web Flex;
  * To consume externals APIs;
* H2 Database;
  * To use a data base embbed in the system;
* Spring Data JPA
  * To create the ORM and the communication with the data base;

## Starting the System

To use this API it is necessary, in addition to having a local clone of the current repository, to have **Gradle** installed. To install **Gradle** just follow the steps presented at https://gradle.org/install/.

After installing **Gradle**, just run the following command through a terminal/console in the project folder:

```bash
gradle bootRun
```

After running this command, a tomcat server containing the API will be available on port **8080**.

## Users Registration

User registration is made by sending a **POST** request to the **/apiveiculos/v1/usuario/** endpoint. The request body must contain a **JSON** with the following mandatory information:

1. **Name;**
2. **E-mail**, which must be unique for each user;
3. **CPF**, which must also be unique for each user;
4. **Date of birth**;

If the registration is successful, a response with status **201**, which contains in its body a **JSON** with the information of the registered user, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned.

* **Example:**

  1. A **POST** request was sent with the following **JSON** in its body to the endpoint **/apiveiculos/v1/user/**:

     ```json
     { 
         "name":"Luke Skywalker",    
      	"email":"greensaber@newrepublic.com",    
         "cpf": "126.764.550-44",    
         "birthDate": "21/07/2000"
     }
     ```

  2. As the information is correct, the registration occurred correctly and the body of the response returned was as follows:

     ```json
     {
         "id": 1,
         "name": "Luke Skywalker",
         "email": "greensaber@newrepublic.com",
         "cpf": "126.764.550-44",
         "birthDate": "21/07/2000",
         "vehicles": null
     }
     ```

  3. When repeating the first step, the following error was returned:

     ```json
     CPF: 126.764.550-44 já utilizado!
     ```

     

## Retrieving a User

To retrieve a specific registered user, just send a **GET** request to the endpoint **/apiveiculos/v1/user/{email_ou_cpf}**, where the email or CPF of the user to be returned must be in place of **{email_ou_cpf}**. If the request is successful, a response with status **200**, which contains in its body a **JSON** with the information of the requested user, will be returned, otherwise, a response with status **404** and containing an error message in its body will be returned.

**Example**:

1. A **GET** request was sent to the endpoint **/apiveiculos/v1/usuario/126.764.550-44**, where 126.764.550-44 is the cpf of the user registered in the previous section. The response body was as follows:

   ```json
   {
       "id": 1,
       "name": "Luke Skywalker",
       "email": "greensaber@newrepublic.com",
       "cpf": "126.764.550-44",
       "birthDate": "21/07/2000",
       "vehicles": []
   }
   ```

   

## Adding a Vehicle to a User

To add a vehicle to a user, the vehicle must first be registered in the system. To retrieve the list of registered vehicles, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/**. Now, having the information of the vehicle to be added, it is only necessary to send a **PUT** request to the endpoint **/apiveiculos/v1/user/{email_ou_cpf}/registro-veiculo/{vehicle_id}**, where the user email or CPF must be in place of **{email_ou_cpf}** and the id of the vehicle to be added must be in place of **{vehicle_id}**. If the addition is successful, a response with status **200**, which contains in its body a **JSON** with the information of the added vehicle, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned.

**Example**:

1. A **PUT** request was sent to the endpoint **/apiveiculos/v1/usuario/greensaber@newrepublic.com/registro-veiculo/2**, where **greensaber@newrepublic.com** is the email of the previously registered user and **2** is the **ID** of the already registered vehicle. The response body was as follows:

   ```json
   {
       "id": 2,
       "brand": "HYUNDAI",
       "model": "HD80 3.0 16V (diesel)(E5)",
       "year": "2018",
       "type": "caminhoes",
       "rotationDay": 6,
       "price": "R$ 100.446,00",
       "rotationActive": false
   }
   ```

2. To verify that the addition occurred correctly, a **GET** request was made to the endpoint **/apiveiculos/v1/usuario/126.764.550-44**. The response body was as follows:

   ```json
   {
       "id": 1,
       "name": "Luke Skywalker",
       "email": "greensaber@newrepublic.com",
       "cpf": "126.764.550-44",
       "birthDate": "21/07/2000",
       "vehicles": [
           {
               "id": 2,
               "brand": "HYUNDAI",
               "model": "HD80 3.0 16V (diesel)(E5)",
               "year": "2018",
               "type": "caminhoes",
               "rotationDay": 6,
               "price": "R$ 100.446,00",
               "rotationActive": false
           }
       ]
   }
   ```

   The vehicle was registered correctly!

## Removing a Vehicle from a User

To remove a vehicle from a user, it is only necessary to send a **DELETE** request to the endpoint **/apiveiculos/v1/user/ {email_ou_cpf}/registro-veiculo/{vehicle_id}**, where the user email or CPF must be in place of **{email_ou_cpf}** and the id of the vehicle to be removed must be in place of **{vehicle_id}**. If the removal is successful, a response with status **200**, which contains in its body a **JSON** with the information of the removed vehicle, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned.

**Example**:

1. A **DELETE** request was sent to the endpoint **/apiveiculos/v1/usuario/greensaber@newrepublic.com/registro-veiculo/2**, where **greensaber@newrepublic.com** is the email of the previously registered user and **2** is the **ID** of the already created and registered vehicle. The response body was as follows:

   ```json
   {
       "id": 2,
       "brand": "HYUNDAI",
       "model": "HD80 3.0 16V (diesel)(E5)",
       "year": "2018",
       "type": "caminhoes",
       "rotationDay": 6,
       "price": "R$ 100.446,00",
       "rotationActive": false
   }
   ```

2. To verify that the removal occurred correctly, a **GET** request was made to the endpoint **/apiveiculos/v1/usuario/ 126.764.550-44**. The response body was as follows:

   ```json
   {
       "id": 1,
       "name": "Luke Skywalker",
       "email": "greensaber@newrepublic.com",
       "cpf": "126.764.550-44",
       "birthDate": "21/07/2000",
       "vehicles": []
   }
   ```

   So the vehicle was removed correctly!

   

## Updating a User's Data

Updating a user's data is performed by sending a **PUT** request to the endpoint **/apiveiculos/v1/usuario/ {email_ou_cpf}**, where the email or CPF of the user to be updated must be in place of **{email_ou_cpf}** and the request body must contain a **JSON** with all the user information, even the unchanged ones, which are:

1. **Name;**
2. **Email;**
3. **CPF;**
4. **Date of birth;**

If the update is successful, a response with status **200**, which contains in its body a **JSON** with the information of the updated user, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned.

**Example**:

1. A **PUT** request was sent containing the following body to the endpoint **/apiveiculos/v1/usuario/126.764.550-44**, where 126.764.550-44 is the CPF of the previously registered user. An email change will be made from greensaber@newrepublic.com to greensaber@newnewrepublic.com.

   ```json
   { 
       "name":"Luke Skywalker",    
    	"email":"greensaber@newnewrepublic.com",    
       "cpf": "126.764.550-44",    
       "birthDate": "21/07/2000"
   }
   ```

2. As the data is correct, the request was successful and the response body was as follows:

   ```json
   {
       "id": 1,
       "name": "Luke Skywalker",
       "email": "greensaber@newnewrepublic.com",
       "cpf": "126.764.550-44",
       "birthDate": "21/07/2000",
       "vehicles": []
   }
   ```

   

## Deleting a user

To delete a user, just send a **DELETE** request to the endpoint **/apiveiculos/v1/usuario/ {email_ou_cpf}**, where the email or CPF of the user to be deleted must be in place of **{email_ou_cpf}**. If the removal is successful, a response with status **200**, which contains in its body a **JSON** with the information of the deleted user, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned.

**Example**:

1. A **DELETE** request was sent to the endpoint **/apiveiculos/v1/usuario/126.764.550-44**, where 126.764.550-44 is the CPF of the previously registered user. The response body was as follows:

   ```json
   {
       "id": 1,
       "name": "Luke Skywalker",
       "email": "greensaber@newrepublic.com",
       "cpf": "126.764.550-44",
       "birthDate": "21/07/2000",
       "vehicles": []
   }
   ```

2. To check if the user was deleted, an attempt was made to recover the deleted user's information by making a **GET** request to the endpoint **/apiveiculos/v1/usuario/126.764.550-44**. The body of the response was:

   ```json
   Não existe usuário com o cpf: 126.764.550-44
   ```

   So the user was deleted correctly.

   

## Vehicle Registration

The vhicle registration is performed by sending a **POST** request to the endpoint **/apiveiculos/v1/veiculo/**, where the body of the request must contain a **JSON** with the following mandatory information:

1. **Brand**;
2. **Model**;
3. **Year**;
4. **Type of vehicle**, which must be one of the following:
   * carros (cars);
   * motos (motorcycles);
   * caminhoes (trucks);
   * fipe;

This information must be the same as that is contained in [API FIPE](https://github.com/deividfortuna/fipe), as this API will be used to return the price of the vehicle. This api was chosen because the [FIPE table](https://veiculos.fipe.org.br/) contains the average prices of vehicles announced by sellers in the Brazilian market.

After the system receives the request, the following processes will be performed to register information about the price of the vehicle and vehicle rotation:

1. The rotation day is obtained through the last digit of the vehicle year using the following criteria:

   - Final 0-1: Monday - 2

   - Final 2-3: Tuesday - 3

   - Final 4-5: Wednesday - 4

   - Final 6-7: Thursday - 5

   - Final 8-9: Friday - 6

2. Then the rotation day is compared with the current day to fill the **isRotationActive** attribute;

3. Finally, the price of the vehicle is obtained through [FIPE API](https://github.com/deividfortuna/fipe).

If the registration is successful, a response with status **201**, which contains in its body a **JSON** with the information of the  registered vehicle, will be returned, otherwise, a response with status **400** and containing an error message in its body will be returned. If the vehicle has already been registered, the error message will be as follows: **Esse veículo já foi anteriormente cadastrado: {vehicle_id}**, where instead of **{vehicle_id}** it will be the id of the vehicle already registered.

**Example:**

1. A **POST** request containing the following **JSON** in its body was sent to the endpoint **/apiveiculos/v1/veiculo/**:

   ```json
   {    
       "brand":"HYUNDAI",    
       "model":"HD80 3.0 16V (diesel)(E5)",    
       "year": "2018",    
       "type": "caminhoes"
   }
   ```

2. As the information is correct, the registration occurred correctly and the body of the response returned was as follows:

   ```json
   {
       "id": 2,
       "brand": "HYUNDAI",
       "model": "HD80 3.0 16V (diesel)(E5)",
       "year": "2018",
       "type": "caminhoes",
       "rotationDay": 6,
       "price": "R$ 100.446,00",
       "rotationActive": false
   }
   ```

3. When repeating the first step, the following error was returned:

   ```json
   Esse veículo já foi anteriormente cadastrado: 2
   ```

   

## Retrieving All Vehicles

To retrieve the list of registered vehicles, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/**. If the request is successful, a response with status **200**, which contains in its body a **JSON** with a information list of all registered vehicles, will be returned, otherwise, a response with status **404** and containing an error message in its body will be returned.

**Example:**

1. A **GET** request was sent to the endpoint **/apiveiculos/v1/veiculo/** and the response body was as follows:

   ```json
   [
       {
           "id": 2,
           "brand": "HYUNDAI",
           "model": "HD80 3.0 16V (diesel)(E5)",
           "year": "2018",
           "type": "caminhoes",
           "rotationDay": 6,
           "price": "R$ 100.446,00",
           "rotationActive": false
       },
       {
           "id": 3,
           "brand": "FIAT",
           "model": "500 Sport Air 1.4 16V/1.4 Flex Mec.",
           "year": "2014 Gasolina",
           "type": "carros",
           "rotationDay": 4,
           "price": "R$ 44.208,00",
           "rotationActive": false
       }
   ]
   ```

   

## Retrieving a Vehicle

To retrieve a specific registered vehicle, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/{id}**, where the id of the vehicle to be returned must be in place of **{id}**. If the request is successful, a response with status **200**, which contains in its body a **JSON** with the information of the requested vehicle, will be returned, otherwise, a response with status **404** and containing an error message in its body will be returned.

**Example:**

1. A **GET** request was sent to the endpoint **/apiveiculos/v1/veiculo/3** and the response body was as follows:

   ```json
   {
       "id": 3,
       "brand": "FIAT",
       "model": "500 Sport Air 1.4 16V/1.4 Flex Mec.",
       "year": "2014 Gasolina",
       "type": "carros",
       "rotationDay": 4,
       "price": "R$ 44.208,00",
       "rotationActive": false
   }
   ```

   

## Endpoints

The following are the endpoints and http methods available for each endpoint:

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
