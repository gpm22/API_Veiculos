# Vehicles API :car: :car: :car:

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

Exemple:

```json
{ 
    "name":"Luke Skywalker",    
 	"email":"greensaber@newrepublic.com",    
    "cpf": "126.764.550-44",    
    "birthDate": "21/07/2000"
}
```

If the registration is successful, a response with status **201** will be returned, otherwise, a response with status **400** will be returned.

## Retrieving a User

To retrieve a specific registered user, just send a **GET** request to the endpoint **/apiveiculos/v1/user/{email_ou_cpf}**, where the email or CPF of the user to be returned must be in place of **{email_ou_cpf}**. If the request is successful, a response with status **200** will be returned, otherwise, a response with status **404** will be returned.

## Adding a Vehicle to a User

To add a vehicle to a user, the vehicle must first be registered in the system. To retrieve the list of registered vehicles, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/**. Now, having the information of the vehicle to be added, it is only necessary to send a **PUT** request to the endpoint **/apiveiculos/v1/user/{email_ou_cpf}/registro-veiculo/{vehicle_id}**, where the user email or CPF must be in place of **{email_ou_cpf}** and the id of the vehicle to be added must be in place of **{vehicle_id}**. If the addition is successful, a response with status **200** will be returned, otherwise, a response with status **400** will be returned.

## Removing a Vehicle from a User

To remove a vehicle from a user, it is only necessary to send a **DELETE** request to the endpoint **/apiveiculos/v1/user/{email_ou_cpf}/registro-veiculo/{vehicle_id}**, where the user email or CPF must be in place of **{email_ou_cpf}** and the id of the vehicle to be removed must be in place of **{vehicle_id}**. If the removal is successful, a response with status **200** will be returned, otherwise, a response with status **400** will be returned.

## Updating a User's Data

Updating a user's data is performed by sending a **PUT** request to the endpoint **/apiveiculos/v1/usuario/{email_ou_cpf}**, where the email or CPF of the user to be updated must be in place of **{email_ou_cpf}** and the request body must contain a **JSON** with all the user information, even the unchanged ones, which are:

1. **Name;**
2. **Email;**
3. **CPF;**
4. **Date of birth;**

If the update is successful, a response with status **200** will be returned, otherwise, a response with status **400** will be returned.

## Deleting a user

To delete a user, just send a **DELETE** request to the endpoint **/apiveiculos/v1/usuario/ {email_ou_cpf}**, where the email or cpf of the user to be deleted must be in place of **{email_ou_cpf}**. If the removal is successful, a response with status **200** will be returned, otherwise, a response with status **400** will be returned.

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

Exemple:

```json
{    
    "brand":"HYUNDAI",    
    "model":"HD80 3.0 16V (diesel)(E5)",    
    "year": "2018",    
    "type": "caminhoes"
}
```

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

If the registration is successful, a response with status **201** will be returned, otherwise, a response with status **400** will be returned.

## Retrieving All Vehicles

To retrieve the list of registered vehicles, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/**. If the request is successful, a response with status **200** will be returned, otherwise, a response with status **404** will be returned.

## Retrieving a Vehicle

To retrieve a specific registered vehicle, just send a **GET** request to the endpoint **/apiveiculos/v1/veiculo/{id}**, where the id of the vehicle to be returned must be in place of **{id}**. If the request is successful, a response with status **200** will be returned, otherwise, a response with status **404** will be returned.

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
