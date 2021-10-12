# Acceso a Datos, tema 2: JHipster

En este repositorio encontraréis el código explicado en los vídeos de los apartados 2.5 y siguientes del aula virtual.

## Entorno

Este repositorio se ha creado el 12 de octubre de 2021 con las versiones más actuales de Java, Node, Jhipster y Maven.

```bash
╰─ mvn --version                                                                         
Apache Maven 3.8.2 (ea98e05a04480131370aa0c110b8c54cf726c06f)
Maven home: /usr/local/Cellar/maven/3.8.2/libexec
Java version: 11.0.11, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
Default locale: es_ES, platform encoding: UTF-8
OS name: "mac os x", version: "11.5", arch: "x86_64", family: "mac"

╰─ java --version                    
openjdk 11.0.11 2021-04-20
OpenJDK Runtime Environment AdoptOpenJDK-11.0.11+9 (build 11.0.11+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK-11.0.11+9 (build 11.0.11+9, mixed mode)

╰─ node --version
v14.18.0

╰─ jhipster --version     
INFO! Using JHipster version installed globally
7.2.0
```

### Elasticsearch

Para tener una instancia Elasticsearch, podemos lanzar:

```bash
docker run --name "elasticsearch-cev" -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:7.15.0
```

## Comandos útiles

Estos comandos están en formato *ix, si queréis ejecutarlos en Windows, debéis ejecutar el cmd. Es recomendable que
utilicéis dos terminales distintas para front y back.

* Arrancar la aplicación (back): ` ./mvnw`.
* Arrancar la aplicación (front): `npm start`.
* Compilar back: `mvn clean install`
* Compilar front: `npm install --force`

## ¿Cómo probar el código?

Para probar este código tienes varias opciones:
a) Clonar el código de este GitHub
b) Descargar el código de la aplicación. 

A continuación, puedes abrir 

## ¿Como llegar hasta aquí?

### Creando nuestra nueva aplicación

Lo primero, lanzamos el asistente JHipster y nos genera el esqueleto de la apliación. Esta aplicación ya arranca y
funciona:

```shell
╰─ jhipster          
INFO! Using JHipster version installed globally


        ██╗ ██╗   ██╗ ████████╗ ███████╗   ██████╗ ████████╗ ████████╗ ███████╗
        ██║ ██║   ██║ ╚══██╔══╝ ██╔═══██╗ ██╔════╝ ╚══██╔══╝ ██╔═════╝ ██╔═══██╗
        ██║ ████████║    ██║    ███████╔╝ ╚█████╗     ██║    ██████╗   ███████╔╝
  ██╗   ██║ ██╔═══██║    ██║    ██╔════╝   ╚═══██╗    ██║    ██╔═══╝   ██╔══██║
  ╚██████╔╝ ██║   ██║ ████████╗ ██║       ██████╔╝    ██║    ████████╗ ██║  ╚██╗
   ╚═════╝  ╚═╝   ╚═╝ ╚═══════╝ ╚═╝       ╚═════╝     ╚═╝    ╚═══════╝ ╚═╝   ╚═╝

                            https://www.jhipster.tech

Welcome to JHipster v7.2.0
Application files will be generated in folder: /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
 _______________________________________________________________________________________________________________

  Documentation for creating an application is at https://www.jhipster.tech/creating-an-app/
  If you find JHipster useful, consider sponsoring the project at https://opencollective.com/generator-jhipster
 _______________________________________________________________________________________________________________

 ______________________________________________________________________________

  JHipster update available: 7.3.0 (current: 7.2.0)

  Run npm install -g generator-jhipster to update.

 ______________________________________________________________________________

? Which *type* of application would you like to create? Monolithic application (recommended for simple projects)
? What is the base name of your application? jhipster
? Do you want to make it reactive with Spring WebFlux? No
? What is your default Java package name? com.cev.accesoadatos.tema2.jhipster
? Which *type* of authentication would you like to use? JWT authentication (stateless, with a token)
? Which *type* of database would you like to use? SQL (H2, PostgreSQL, MySQL, MariaDB, Oracle, MSSQL)
? Which *production* database would you like to use? MySQL
? Which *development* database would you like to use? H2 with in-memory persistence
? Which cache do you want to use? (Spring cache abstraction) Ehcache (local cache, for a single node)
? Do you want to use Hibernate 2nd level cache? Yes
? Would you like to use Maven or Gradle for building the backend? Maven
? Do you want to use the JHipster Registry to configure, monitor and scale your application? No
? Which other technologies would you like to use? Elasticsearch as search engine
? Which *Framework* would you like to use for the client? Angular
? Do you want to generate the admin UI? Yes
? Would you like to use a Bootswatch theme (https://bootswatch.com/)? Default JHipster
? Would you like to enable internationalization support? No
? Please choose the native language of the application Spanish
? Besides JUnit and Jest, which testing frameworks would you like to use? 
? Would you like to install other generators from the JHipster Marketplace? No

KeyStore 'src/main/resources/config/tls/keystore.p12' generated successfully.

   create .prettierrc
  (...)
```




### Creando entidades

#### Categoria

```shell
╰─ jhipster entity categoria
INFO! Using JHipster version installed locally in current project's node_modules

The entity Categoria is being created.


Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? nombre
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Minimum length, Maximum length
? What is the minimum length of your field? 10
? What is the maximum length of your field? 200

================= Categoria =================
Fields
nombre (String) required minlength='10' maxlength='200'


Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? imagen
? What is the type of your field? [BETA] Blob
? What is the content of the Blob field? An image
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required

================= Categoria =================
Fields
nombre (String) required minlength='10' maxlength='200'
imagen (byte[] image) required


Generating field #3

? Do you want to add a field to your entity? No

================= Categoria =================
Fields
nombre (String) required minlength='10' maxlength='200'
imagen (byte[] image) required

Generating relationships to other entities

? Do you want to add a relationship to another entity? No

================= Categoria =================
Fields
nombre (String) required minlength='10' maxlength='200'
imagen (byte[] image) required



? Do you want to use separate service class for your business logic? No, the REST controller should use the repository directly
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? No

Everything is configured, generating the entity...
```


