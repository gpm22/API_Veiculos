# API Veículos  :car: :car: :car:

Criado por Gabriel Pachêco Milhomem:

* [LinkedIn](https://br.linkedin.com/in/gabriel-pachêco-milhomem-a176a7136);
* [Github](https://github.com/gpm22);
* :email: [Email](gabrielpacmil2@gmail.com);

Projeto disponível em: https://github.com/gpm22/API_Veiculos;

## Descrição

API REST para controlar véiculos de usuários. Basicamente trata-se de um sistema onde usuários podem se cadastrar e também cadastrar uma lista de veículos, sendo que o usuário pode realizar a leitura da própria lista de véiculos.

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

Ao se solicitar a lista de veículos serão retornadas as informações do usuário junto da lista de véiculos, sendo que as informações dos veículos a serem mostradas são as informações fornecidas durante o cadastro, o dia da semana em que o rodízio está ativo, se o rodízio está ativo no dia em que o retorno da lista foi requisitado e o preço do veículo que consta na tabela FIPE.

Desse modo, essa API consiste em 3 endpoints:

1. Cadastro de usuários;
2. Cadastro de véiculos através do CPF ou email do usuário;
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

## 1 - Inicialização do projeto

O projeto é inicializado utilizando o site [Spring Initializr](https://start.spring.io/) com as seguintes configurações:

* **Project**: Gradle Project
  * Pois possui uma performance mais elevada do que Maven, fonte: [Gradle](https://gradle.org/gradle-vs-maven-performance/[);
* **Language**: Java
* **Spring Boot**: 2.5.0
  * Pois é a versão mais recente e estável;
* **Packaging**: Jar
  * Pois facilita a execução do programa compilado através da linha de comando;
* **Java**: 16
  * Por ser a versão mais recente;
* **Dependencies**:
  * Spring Web
  * Spring Reactive Web
  * Spring Data JPA
  * H2 Database

## 2 - Configuração para utilização do banco de dados

Essa configuração é feita através do arquivo `application.properties` , onde é configurado co mas seguintes informações:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=User
spring.datasource.password=Password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Desse modo, será criado um banco de dados em memória, isto é, o banco de dados só existirá durante a execução da API;

## 3 - Criação das *Entities*

Essas *Entities* são as classes *Owner*, que modela o usuário, e *Vehicle*, que modela os veículos, que serão relacionadas ao banco de dados através do *Hibernate*. Uma classe é transformada em uma *Entity* através da anotação `@Entity`. O nome tabela que é será persistida pela *Entity* é fornecido utilizando a anotação `@Table`.

Essas classes devem ter como atributos os dados que serão guardados no banco de dados, dessa forma são:

* Para a classe *Owner*:
  * id;
  * name;
  * email;
  * cpf;
  * birthDate;
  * vehicles;
* Para a classe *Vehicle*:
  * id;
  * brand;
  * model;
  * year;
  * type;
  * rotationDay;
  * isRotationActive;
  * price;

Cada parâmetro é marcado com a anotação `@Column`, que relaciona cada atributo com a sua respectiva coluna na tabela do banco de dados. Os atributos que são obrigatórios de serem fornecidos, como data de nascimento de um usuário ou o modelo de um veículo, são marcados com o parâmetro `nullable = false`, o que não permite que esses parâmetros sejam *null* no banco de dados. Os atributos CPF e email, que devem ser únicos para cada usuário, são marcados como `unique = true`, que não permite duplicata no banco de dados.

A anotação `@ElementCollection`também é utilizada no atributo `vehicles`da classe *Owner* para criar uma relação entre essas 2 classes;

As anotações `@Id`e `@GeneratedValue`são usadas no parâmetro *id* para que o id de cada objetivo seja gerado automaticamente durante sua criação.

A seguir estão os códigos dessas classes:

* **Owner.java**
  <details>
    import javax.persistence.*;

    import java.util.List;

    @Entity
    @Table(name = "owner")
    public class Owner {
        private long id;
        private String name;
        private String email;
        private String cpf;
        private String birthDate;
        private List<Vehicle> vehicles;

        public Owner(){

        }

        public Owner(String name, String email, String cpf, String birthDate){
            this.name = name;
            this.email = email;
            this.cpf = cpf;
            this.birthDate = birthDate;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Column(name = "name", nullable = false)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Column(name = "email", nullable = false, unique = true)
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Column(name = "cpf", nullable = false, unique = true)
        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        @Column(name = "birthDate", nullable = false)
        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        @Column(name = "vehicles")
        @ElementCollection(targetClass = Vehicle.class)
        public List<Vehicle> getVehicles() {
            return vehicles;
        }

        public void setVehicles(List<Vehicle> vehicles) {
            this.vehicles = vehicles;
        }

        public void addVehicle(Vehicle vehicle) {
            this.vehicles.add(vehicle);
        }
    }
  </details>

* **Vehicle.java**
  <details>
    import javax.persistence.*;

    @Entity
    @Table(name = "vehicle")
    public class Vehicle {
        private long id;
        private String brand;
        private String model;
        private String year;
        private String type;
        private int rotationDay;
        private Boolean isRotationActive;
        private String price;

        public Vehicle(){

        }

        public Vehicle(String brand, String model, String year, String type){
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.type = type;

        }

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Column(name = "brand", nullable = false)
        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        @Column(name = "model", nullable = false)
        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        @Column(name = "year", nullable = false)
        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Column(name = "type", nullable = false)
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Column(name = "rotationDay")
        public int getRotationDay() {
            return rotationDay;
        }

        public void setRotationDay(int rotationDay) {
            this.rotationDay = rotationDay;
        }

        @Column(name = "rotationActive")
        public Boolean getRotationActive() {
            return isRotationActive;
        }

        public void setRotationActive(Boolean rotationActive) {
            isRotationActive = rotationActive;
        }

        @Column(name = "price")
        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
  </details>

## 4 - Criação dos *Repositories*

Um *Repository* é uma interface que permite a realização de *queries* SQL no banco de dados. Os *Repositories* aqui utilziados são uma extenção da interface `JpaRepository`.

O *Repository* da classe `Vehicle` apenas extende o `JpaRepository`, enquanto que para o `Repository`da classe `Owner` são criadas 2 funções, através do uso da anotação `@Query`, que realizam `queries`especificas. Essas funções são:

*  `findByCpf`
  * Que irá buscar e retornar no banco de dados o usuário com o CPF fornecido;
* `findByEmail`
  * Que irá buscar e retornar no banco de dados o usuário com o email fornecido;

A seguir estão os códigos dessas interfaces:

* **OwnerRepository.java**

  <details>
    import com.github.gpm22.API_Veiculos.Entities.Owner;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;

    public interface OwnerRepository extends JpaRepository<Owner, Long> {
        @Query(value = "select o from Owner o where o.cpf = ?1")
        Owner findByCpf(final String cpf);
        @Query(value = "select o from Owner o where o.email = ?1")
        Owner findByEmail(final String email);
    }
  </details>


* **VehicleRepository.java**

  <details>
    import com.github.gpm22.API_Veiculos.Entities.Vehicle;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    }
  </details>

## 5 - Criação do *Client*

Um *Client* é uma classe responsável em permitir ao programa a possibilidade de consumir uma API externa, que no trabalho atual é a [FIPE API HTTP REST](https://deividfortuna.github.io/fipe/). Esse consumo é feito utilizando a framework *Spring WebFlux*, que possui a interface *WebClient*, que é uma interface que permite realizar facilmente as requisições HTTP necessárias.

A API FIPE possui 4 *endpoints* para requisições *GET*:

1. Retorno de todas as marcas para um certo tipo de veículo;

   * https://parallelum.com.br/fipe/api/v1/tipo/marcas;
   * Onde tipo deve ser substituído por carros, motos ou caminhoes;

2. Retorno de todos os modelos de um certo tipo de veículo e de uma certa marca;

   * https://parallelum.com.br/fipe/api/v1/tipo/marcas/codigo_marca/modelos;
   * Onde tipo deve ser substituído por carros, motos ou caminhoes;
   * Onde codigo_marca deve ser substituído pelo respectivo código da marca do veículo, obtido no *endpoint* 1;

3. Retorno dos anos para um certo tipo de veículo;

   * https://parallelum.com.br/fipe/api/v1/tipo/marcas/codigo_marca/modelos/codigo_modelo/anos;
   * Onde tipo deve ser substituído por carros, motos ou caminhoes;
   * Onde codigo_marca deve ser substituído pelo respectivo código da marca do veículo, obtido no *endpoint* 1;
   * Onde codigo_modelo deve ser substituído pelo respectivo código do modelo do veículo, obtido no *endpoint* 2;

4. Retorno de todas as informações de um certo veículo, incluindo o valor, contidos na tabela FIPE;

   * https://parallelum.com.br/fipe/api/v1/tipo/marcas/codigo_marca/modelos/codigo_modelo/anos/codigo_ano;
   * Onde tipo deve ser substituído por carros, motos ou caminhoes;
   * Onde codigo_marca deve ser substituído pelo respectivo código da marca do veículo, obtido no *endpoint* 1;
   * Onde codigo_modelo deve ser substituído pelo respectivo código do modelo do veículo, obtido no *endpoint* 2;
   * Onde codigo_ano deve ser substituído pelo respectivo código do ano do veículo, obtido no *endpoint* 3;

Desse modo, são necessárioss 4 métodos, sendo que cada um é responsável pela requisição de um desses *endpoints*. Porém, antes da implementação desses métodoso, é necessário realizar a criação das classes que serão utilizadas para criar objetos com os dados das respostas das requisições feitas para esses *endpoints*. Essas classes devem possuir atributos com o mesmo nome dos atributos encontrados no JSON retornado pela API FIPE. Além disso, como não se tem interesse em todas as informações retornadas pela API FIPE, todas essas classes possuem a anotação `JsonIgnoreProperties(ignoreUnknown = true)`, que permite que apenas sejam salvas as informações do JSON que possuírem atributos com nomes iguais aos atributos das classes do programa aqui mostrado, isto é, todas as outras informações são ignoradas.

Essas classes são:

1. `Brand`

   * Que é utilizada para criar o objeto do retorno do primeiro *endpoint* ;
   * Possui os atributos nome e codigo;
   * É utilizada pelo método `getBrandList` da classe *Client*;
   * Código:

     <details>
         import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

         @JsonIgnoreProperties(ignoreUnknown = true)
         public class Brand {
             private String nome;
             private String codigo;

             public String getNome() {
                 return nome;
             }

             public void setNome(String nome) {
                 this.nome = nome;
             }

             public String getCodigo() {
                 return codigo;
             }

             public void setCodigo(String codigo) {
                 this.codigo = codigo;
             }
         }
     </details>

2. `ModelYear`

   * Que é utilizada para criar o objeto que é o retorno do segundo *endpoint* ;
   * Possui os atributos anos, que é da classe `Year`,  e modelos, que é da classe `Model`;
   * É utilizada pelo método `getModelList` da classe *Client*;
   * Código:
      <details>
            import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public class ModelYear {
                private Year[] anos;
                private Model[] modelos;

                public Year[] getAnos() {
                    return anos;
                }

                public void setAnos(Year[] anos) {
                    this.anos = anos;
                }

                public Model[] getModelos() {
                    return modelos;
                }

                public void setModelos(Model[] modelos) {
                    this.modelos = modelos;
                }
            }

      </details>


3. `Model`

   * Que é utilizada para criar o objeto que é um dos atributos do retorno do segundo *endpoint* ;
   * Possui os atributos nome e codigo;
   * Código:
      <details>
            import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public class Model {
                private String nome;
                private String codigo;

                public String getNome() {
                    return nome;
                }

                public void setNome(String nome) {
                    this.nome = nome;
                }

                public String getCodigo() {
                    return codigo;
                }

                public void setCodigo(String codigo) {
                    this.codigo = codigo;
                }
            }
      </details>

4. `Year`

   * Que é utilizada para criar os objetos de dois *endopoints*:
     1. Sendo um dos atributos do retorno do segundo *endpoint* ;
     2. Sendo o retorno do terceiro *endpoint*;

   * Possui os atributos nome e codigo;
   * É utilizada pelo método `getYearlList` da classe *Client*;
   * Código:
      <details>
          import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

          @JsonIgnoreProperties(ignoreUnknown = true)
          public class Year {
              private String nome;
              private String codigo;

              public String getNome() {
                  return nome;
              }

              public void setNome(String nome) {
                  this.nome = nome;
              }

              public String getCodigo() {
                  return codigo;
              }

              public void setCodigo(String codigo) {
                  this.codigo = codigo;
              }
          }
      </details>

5. `Price`

   * Que é utilizada para criar o objeto do retorno do quarto e último *endpoint* ;
   * Possui o atributo valor;
     * Por padrão o java coloca todos os atributos com letra minúscula, e como no caso do último *endpoint* os atributos são inicializados com letra maúscula, o uso da anotação `@JsonProperty("Valor")` foi utilizada;
   * É utilizada pelo método `getFipePrice` da classe *Client*;
   * Código:

      <details>
          import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
          import com.fasterxml.jackson.annotation.JsonProperty;

          @JsonIgnoreProperties(ignoreUnknown = true)
          public class Price {
              private String valor;

              public String getValor() {
                  return valor;
              }

              @JsonProperty("Valor")
              public void setValor(String Valor) {
                  this.valor = Valor;
              }
          }
      </details>

Agora assim os métodos para realizar as requisições *GET* podem ser implementadas na classe `ApiVeiculosClient` seguindo as seguintes diretrizes:

1. O(s) paramêtro(s) desses métodos são a parte que falta na URI para se fazer a requisição necessária, como exemplo para o método `getBrandList` do primeiro *endpoint* que possui a apenas o parâmetro tipo, ou para o método `getModelList` do segundo *endpoint* que necessita dos parâmetros tipo e codigo_marca;

2. Todo o processo de requisição até receber o JSON é o mesmo para os 4 *endpoints*, apenas alterando-se a URI utilizada, desse modo criou-se o método privado `getRetrieve`, que é responsável por fazer a requisição e recebimento da resposta e tendo como parâmetro a URI necessária:

   ```java
   private WebClient.ResponseSpec getRetrieve(String uri){
           return WebClient
                   .create() // método que cria o objeto responsável pelas requisições
                   .method(HttpMethod.GET) // método que cria o objeto responsável para representar qual requisição será feita
                   .uri(uri) // método que cria o objeto responsável por definir a URI a ser requisitada
                   .retrieve(); // método que cria o objeto responsável por receber a resposta da requisição feita
       }
   ```

3. Os métodos responsáveis por retornar os dados dos *endpoints* basicamente recebem a resposta da requisição pelo método `getRetrieve`,  então fazem as tranformações necessárias na resposta, utilizando os métodos `bodyToMono(ClasseApropriada.class)` e `block()`, para por fim retornar um objeto da classe que for apropriada.

4. Nos métodos para o primeiro (`getCodeBrand`) e terceiro *endpoints* (`getYearlList`) são retornadas listas de objetos e desse modo, além do passo 3, é necessária a utilização de um *mapper* para realizar a conversão de cada objeto dessa lista para a classe apropriada. Esse *mapper* é criado através de:

   ```java
   ObjectMapper mapper = new ObjectMapper();
   ```

* Código da classe `ApiVeiculosClient`:

  <details>
    import com.fasterxml.jackson.databind.ObjectMapper;

    import com.github.gpm22.API_Veiculos.Client.Classes.*;

    import org.springframework.http.HttpMethod;

    import org.springframework.web.reactive.function.client.WebClient;
    import java.util.Arrays;
    import java.util.stream.Stream;

    public class ApiVeiculosClient {

        private final String uri_base = "https://parallelum.com.br/fipe/api/v1/";

        private WebClient.ResponseSpec getRetrieve(String uri){
            return WebClient
                    .create()
                    .method(HttpMethod.GET)
                    .uri(uri)
                    .retrieve();
        }

        public Stream<Brand> getBrandList(String type) {

            ObjectMapper mapper = new ObjectMapper();

            return Arrays
                    .stream(getRetrieve(uri_base +type+"/marcas")
                            .bodyToMono(Brand[].class)
                            .block())
                    .map(value -> mapper.convertValue(value, Brand.class));
        }

        public Model[] getModelList(String type, String codeBrand) {

            return getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos")
                    .bodyToMono(ModelYear.class)
                    .block()
                    .getModelos();

        }

        public Stream<Year> getYearlList(String type, String codeBrand, String codeModel) {

            ObjectMapper mapper = new ObjectMapper();
            return Arrays
                    .stream(getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos")
                            .bodyToMono(Year[].class)
                            .block())
                    .map(value -> mapper.convertValue(value, Year.class));
        }

        public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

            return getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos/"+year)
                    .bodyToMono(Price.class)
                    .block();
        }

    }

  </details>

## 6 - Criação do *Service*

A classe *Service* é a responsável pela realização dos "trabalhos" exigidos pelo API, como por exemplo informar o preço de um veículo ou validar alguma dado. Uma classe se transformar em *Service* através da anotação `@Service`. A classe `ApiVeiculosService`possui os seguinte métodos:

1. `rotationDay`- que retorna o dia da semana onde o rodízio do veículo é ativo;

2. `isRotationDayActive` - que retorna se o rodizio do veículo está ativo no dia que a lista de veículos é solicitada;

3. `ownerNameValidation` - que valida o nome do usuário através da seguinte *regex* obtida em [stackoverflow](https://pt.stackoverflow.com/questions/242948/validar-nome-e-sobrenome-com-express%C3%A3o-regular):

   ```java
   "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$"
   ```

4. `ownerEmailValidation` - que valida o email do usuário através da seguinte regex:

   ```java
   "^[A-Za-z0-9+_.-]+@(.+)$"
   ```

5. `ownerCpflValidation` - que valida o cpf do usuário através da seguinte regex:

   ```java
   "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}"
   ```

6. `ownerBirthDateValidation` - que valida a data de nascimento do usuário, no formato dia/mês/ano, através da seguinte regex obtida em [hkotsubo](https://hkotsubo.github.io/blog/2019-04-05/posso-usar-regex-para-validar-datas):

   ```java
   "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
   ```

7. `getCodeBrand`- que recebe como parâmetro o tipo do veículo e a marca do veículo e então irá chamar o método `getBrandList` da classe `ApiVeiculosClient`. Após isso, irá realizar uma filtragem na lista retornada buscando o objeto que possui o nome igual ao da marca do veículo. Por fim, após a busca, é retornado o código referente a marca do veículo;

8. `getCodeModel`- que recebe como parâmetros o tipo do veículo, o código da marca do veículo e o modelo do véiculo e então irá chamar o método `getModelList` da classe `ApiVeiculosClient`. Após isso, irá realizar uma filtragem na lista retornada buscando o objeto que possui o nome igual ao do modelo do veículo. Por fim, após a busca, é retornado do código referente ao modelo do veículo;

9. `getFipeYear`- que recebe como parâmetros o tipo do veículo, o código da marca do veículo, o código do modelo do véiculo e o ano do veículo e então irá chamar o método `getModelList` da classe `ApiVeiculosClient`. Após isso, irá realizar uma filtragem na lista retornada buscando o objeto que possui o nome igual ao do modelo do veículo. Por fim, após a busca, é retornado do código referente ao ano do veículo;

10. `getFipePrice`- que recebe como parâmetro um objeto da classe veículo e então irá utilizar os métodos `getCodeBrand`, `getCodeModel` e `getFipeYear` para obter os parâmetros necessários para chamar o método `getFipePrice`da classe `ApiVeiculosClient`. Após isso, irá retornar o valor do veículo;

* Código da classe `ApiVeiculosService`:

  <details>
    import com.github.gpm22.API_Veiculos.Client.ApiVeiculosClient;
    import com.github.gpm22.API_Veiculos.Entities.Vehicle;
    import org.springframework.stereotype.Service;
    import java.util.Arrays;
    import java.util.Calendar;
    import java.util.regex.Pattern;

    @Service
    public class ApiVeiculosService{

        ApiVeiculosClient client = new ApiVeiculosClient();

        public int rotationDay(String year){
            String lastDigit = year.substring(3,4);

            return switch (lastDigit) {
                case "0" -> Calendar.MONDAY;
                case "1" -> Calendar.MONDAY;
                case "2" -> Calendar.TUESDAY;
                case "3" -> Calendar.TUESDAY;
                case "4" -> Calendar.WEDNESDAY;
                case "5" -> Calendar.WEDNESDAY;
                case "6" -> Calendar.THURSDAY;
                case "7" -> Calendar.THURSDAY;
                case "8" -> Calendar.FRIDAY;
                case "9" -> Calendar.FRIDAY;
                default -> Calendar.SUNDAY;
            };
        }

        public Boolean isRotationActive(int day){
            return day == Calendar
                    .getInstance()
                    .get(Calendar.DAY_OF_WEEK);
        }

        public Boolean ownerNameValidation(String ownerName) {
            String nameValidation = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
            return Pattern
                    .compile(nameValidation)
                    .matcher(ownerName)
                    .matches();
        }

        public Boolean ownerEmailValidation(String ownerEmail) {
            String emailValidation = "^[A-Za-z0-9+_.-]+@(.+)$";
            return Pattern
                    .compile(emailValidation)
                    .matcher(ownerEmail)
                    .matches();
        }

        public Boolean ownerCpfValidation(String ownerCpf) {
            String cpfValidation = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

            return Pattern
                    .compile(cpfValidation)
                    .matcher(ownerCpf)
                    .matches();
        }

        public Boolean ownerBirthDateValidation(String ownerBirthDate) {
            String birthDateValidation="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

            return Pattern
                    .compile(birthDateValidation)
                    .matcher(ownerBirthDate)
                    .matches();
        }

        private String getCodeBrand(String type, String vehicleBrand){
            return client
                    .getBrandList(type)
                    .filter(brand -> brand.getNome().equals(vehicleBrand))
                    .findAny()
                    .get()
                    .getCodigo();
        }

        private String getCodeModel(String type, String codeBrand , String vehicleModel){
            return Arrays
                    .stream(client
                            .getModelList(type, codeBrand))
                    .sequential().filter(model -> model.getNome().equals(vehicleModel))
                    .findAny()
                    .get()
                    .getCodigo();
        }

        private String getFipeYear(String type, String codeBrand , String codeModel , String vehicleYear){

            return client
                    .getYearlList(type, codeBrand, codeModel)
                    .filter(year -> year.getNome().equals(vehicleYear))
                    .findAny()
                    .get()
                    .getCodigo();
        }

        public String getFipePrice(Vehicle vehicle){

            String type = vehicle.getType();
            String codeBrand = getCodeBrand(type, vehicle.getBrand());
            String codeModel = getCodeModel(type, codeBrand, vehicle.getModel());
            String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear());

            return client
                    .getFipePrice(type, codeBrand, codeModel, fipeYear)
                    .getValor();
        }

    }
  </details>

## 7 - Criação do *Controller*

A última parte a ser feita é a classe `ApiVeiculosController`, que é a classe responsável pelas requisições que serão recebidas pela API aqui criada. Uma classe é transformada em *Controller* através da anotação `@RequestController` que é utlizada em conjunto da anotação `@RequestMapping`, que define a URI base da API.

Existem três métodos na classe *Controller* aqui utilizada, sendo que cada uma representa um *endpoint* diferente:

1. `createOwner`
   * Responsável por criar um usuário ao receber uma requisição *POST* contendo os dados necessários;
   * Marcado com a anotação `PostMapping("/usuario")`, que permite a API receber requisições *POST* através dessa URI.
   * Após receber os dados, são realizadas as validações dos dados, se estão no formato correto ou se o CPF ou email já foram utilizados. Se falhar em algum dessas validações, a mensagem apropriada aparece indicando o que ocorreu junto de um *Status 400*.
   * Se os dados forem aprovados, o *Status 201* é retornado junto ao objeto criado;
2. `createVehicle`
   * Responsável por cadastrar um veículo relacionado a um usuário, quando a API receber uma requisição *POST* contendo os dados necessários para a URI apropriada e fornecendo no corpo da URI o email ou cpf do usuário.
   * Marcado com a anotação `PostMapping("/cadastrar-veiculo/{email_ou_cpf}")`, que permite a API receber requisições *POST* através dessa URI e ler o valor passado em `{email_ou_cpf}`através da anotação *@PathVariable*..
   * Após receber os dados, primeiro verifica-se se o usuário existe e então verifica-se se nenhum dos atributos está vazio. Não faz sentido realizar verificações mais sofisticadas do que essas, pois os dados fornecidos devem ser iguais aos contidos na tabela FIPE para o programa funcionar apropriadamente.  Se falhar em algum dessas validações, a mensagem apropriada aparece indicando o que ocorreu junto de um *Status 400*.
   * Se os dados forem aprovados, os atributos restantes são preenchidos pelos métodos contidos na classe `ApiVeiculosService` e então o *Status 201* é retornado junto ao objeto criado;
3. `getVehicles`
   * Responsável por retornar os dados de um usuário junto da lista dos seus veículos cadastrados, quando a API recer uma requisição `GET` para a URI apropriada e fornecendo no corpo da URI o email ou cpf do usuário.
   * Marcado com a anotação `GetMapping("/lista-de-veiculos/{email_ou_cpf}")`, que permite a API receber requisições *GET* através dessa URI e ler o valor passado em `{email_ou_cpf}` através da anotação *@PathVariable*.
   * Após receber a requisição, o usuário fornecido será procurado no banco de dados. Acaso o usuário não exista no banco de dados, retorna-se o *Satus 404* junto de um mensagem apropriada.
   * Acaso o usuário exista, o atributo `isRotationDay`de todos os veículos desse usuário são atualizados e então um *Status 200* é retornado em conjunto com um JSON contendo as informações solicitadas.

* Código da classe `ApiVeiculosController`:

  <details>
    import com.github.gpm22.API_Veiculos.Client.ApiVeiculosClient;
    import com.github.gpm22.API_Veiculos.Entities.Owner;
    import com.github.gpm22.API_Veiculos.Entities.Vehicle;
    import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
    import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;

    import com.github.gpm22.API_Veiculos.Services.ApiVeiculosService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/apiveiculos/v1")
    public class ApiVeiculosController {
        @Autowired
        private OwnerRepository ownerRepository;

        @Autowired
        private VehicleRepository vehicleRepository;

        @Autowired
        private ApiVeiculosService service;

        private ApiVeiculosClient client = new ApiVeiculosClient();

        @PostMapping("/usuario")
        public ResponseEntity createOwner(@RequestBody Owner owner) {

            if(!service.ownerNameValidation(owner.getName())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Nome: " + owner.getName() +" é inválido!");
            }

            if(!service.ownerCpfValidation(owner.getCpf())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("CPF: " + owner.getCpf() +" é inválido!");
            }

            if(!service.ownerEmailValidation(owner.getEmail())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Email: " + owner.getEmail() +" é inválido!");
            }

            if(!service.ownerBirthDateValidation(owner.getBirthDate())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Data: " + owner.getBirthDate() +" é inválida!");
            }

            if(ownerRepository.findByCpf(owner.getCpf()) != null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("CPF: " + owner.getCpf() +" já utilizado!");
            }

            if(ownerRepository.findByEmail(owner.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email: " + owner.getEmail() +" já utilizado!");
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ownerRepository.save(owner));
        }

        @PostMapping("/cadastrar-veiculo/{email_ou_cpf}")
        public ResponseEntity createVehicle(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Vehicle vehicle){

            Owner owner;

            Owner ownerEmail = ownerRepository.findByEmail(emailOuCpf);

            if (ownerEmail != null) {
                owner = ownerEmail;
            } else {
                owner = ownerRepository.findByCpf(emailOuCpf);
            }

            if(owner == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Não existe usuário com o email ou cpf: " + emailOuCpf);
            }

            if(vehicle.getBrand() == "" || vehicle.getBrand() == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Parâmetro marca não pode ser vazio!");
            }

            if(vehicle.getModel() == "" || vehicle.getModel() == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Parâmetro modelo não pode ser vazio!");
            }

            if(vehicle.getYear() == "" || vehicle.getYear() == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Parâmetro ano não pode ser vazio!");
            }

            if(vehicle.getType() == "" || vehicle.getType() == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Parâmetro tipo não pode ser vazio!\n Deve ser: carros, motos ou caminhoes.");
            }

            vehicle.setRotationDay(service.rotationDay(vehicle.getYear()));
            vehicle.setRotationActive(service.isRotationActive(vehicle.getRotationDay()));
            vehicle.setPrice(service.getFipePrice(vehicle));
            owner.addVehicle(vehicle);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(vehicleRepository.save(vehicle));
        }

        @GetMapping("/lista-de-veiculos/{email_ou_cpf}")
        public ResponseEntity getVehicles(@PathVariable(value="email_ou_cpf") String emailOuCpf){

            Owner owner;

            Owner ownerEmail = ownerRepository.findByEmail(emailOuCpf);

            if (ownerEmail != null) {
                owner = ownerEmail;
            } else {
                owner = ownerRepository.findByCpf(emailOuCpf);
            }

            Optional<Owner> optional = Optional.ofNullable(owner);

            if (optional.isPresent()) {
                List<Vehicle> vehicles = optional.get().getVehicles();
                vehicles.forEach(n -> n.setRotationActive(service.isRotationActive(n.getRotationDay())));
                return ResponseEntity.ok().body(optional.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Não existe usuário com o email ou cpf: " + emailOuCpf);
            }
        }
    }

  </details>

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

Todos os veículos do persistente **Ash** foram requisitados através de uma requisição *GET* para http://localhost:8080/apiveiculos/v1/lista-de-veiculos/foreveryoung@bill.com, que resultou em:

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

Uma requisão *GET* para http://localhost:8080/apiveiculos/v1/lista-de-veiculos/lovemydog@neopets.com irá retornar os veículos do nosso querido amante dos animais John Wick:

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

## Conclusão

Uma API REST para controle de veículos foi desenvolvida utilizando diversas frameworks Spring, sendo que o seu funcionamento foi minuciosamente explicado e demonstrado. Uma limitação da aplicação está relacionada ao consumo da API FIPE, pois faz-se necessário que os dados sejam exatemente identicos entre as APIs para que ocorra um o funcionamento apropriado da API Veiculos.
