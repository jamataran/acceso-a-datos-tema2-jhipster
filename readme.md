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

* Generar entidades, regenerar, etc.. : `jhipster`. **Siempre con front/back apagado, después recompilar**.
* Arrancar la aplicación (back): ` ./mvnw`.
* Arrancar la aplicación (front): `npm start`.
* Compilar back: `mvn clean install`. **Siempre debemos compilar despues de hacer cualquier cosa con el asistente de JHipster**.
* Compilar front: `npm install --force` **Siempre debemos compilar despues de hacer cualquier cosa con el asistente de JHipster**.

## Links últiles
* [Aplicación](http://localhost:9000/), login con admin/admin o user/user
* [Base de datos](http://localhost:8080/h2-console)
* [Swagger](http://localhost:8080/admin/docs)

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




### Creando entidades con filtrado

```shell

╰─ jhipster entity pelicula 
INFO! Using JHipster version installed locally in current project's node_modules

The entity Pelicula is being created.


Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? titulo
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Minimum length, Maximum length
? What is the minimum length of your field? 10
? What is the maximum length of your field? 255

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'


Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? fechaEstreno
? What is the type of your field? Instant
? Do you want to add validation rules to your field? No

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 


Generating field #3

? Do you want to add a field to your entity? Yes
? What is the name of your field? descripcion
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? 

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 


Generating field #4

? Do you want to add a field to your entity? Yes
? What is the name of your field? enCines
? What is the type of your field? Boolean
? Do you want to add validation rules to your field? No

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 
enCines (Boolean) 


Generating field #5

? Do you want to add a field to your entity? No

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 
enCines (Boolean) 


Generating relationships to other entities

? Do you want to add a relationship to another entity? No

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 
enCines (Boolean) 



? Do you want to use separate service class for your business logic? Yes, generate a separate service class
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Dynamic filtering for the entities with JPA Static metamodel
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? No

Everything is configured, generating the entity...


Found the .jhipster/Categoria.json configuration file, entity can be automatically generated!

     info Creating changelog for entities Categoria,Pelicula
jsxBracketSameLine is deprecated.
    force .yo-rc-global.json
    force .yo-rc.json
    force .jhipster/Pelicula.json
    force .jhipster/Categoria.json
identical src/main/resources/config/liquibase/fake-data/blob/hipster.png
 conflict src/main/resources/config/liquibase/changelog/20211012145248_added_entity_Categoria.xml
? Overwrite src/main/resources/config/liquibase/changelog/20211012145248_added_entity_Categoria.xml? overwrite this and all others
    force src/main/resources/config/liquibase/changelog/20211012145248_added_entity_Categoria.xml
identical src/main/resources/config/liquibase/fake-data/categoria.csv
   create src/main/resources/config/liquibase/changelog/20211012150330_added_entity_Pelicula.xml
   create src/main/webapp/app/entities/pelicula/pelicula.model.ts
   create src/main/resources/config/liquibase/fake-data/pelicula.csv
   create src/main/webapp/app/entities/pelicula/route/pelicula-routing-resolve.service.ts
   create src/main/webapp/app/entities/pelicula/update/pelicula-update.component.ts
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/PeliculaRepository.java
   create src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.ts
   create src/main/webapp/app/entities/pelicula/list/pelicula.component.html
   create src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.spec.ts
   create src/main/webapp/app/entities/pelicula/list/pelicula.component.ts
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/service/PeliculaQueryService.java
   create src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.html
   create src/main/webapp/app/entities/pelicula/list/pelicula.component.spec.ts
   create src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.ts
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/service/PeliculaService.java
   create src/main/webapp/app/entities/pelicula/route/pelicula-routing-resolve.service.spec.ts
   create src/main/webapp/app/entities/pelicula/pelicula.module.ts
   create src/main/webapp/app/entities/pelicula/service/pelicula.service.ts
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/service/criteria/PeliculaCriteria.java
   create src/main/webapp/app/entities/pelicula/service/pelicula.service.spec.ts
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/search/PeliculaSearchRepository.java
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/web/rest/PeliculaResource.java
   create src/main/webapp/app/entities/pelicula/update/pelicula-update.component.spec.ts
   create src/main/webapp/app/entities/pelicula/update/pelicula-update.component.html
   create src/main/webapp/app/entities/pelicula/route/pelicula-routing.module.ts
   create src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.spec.ts
   create src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.html
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/domain/Pelicula.java
   create src/test/java/com/cev/accesoadatos/tema2/jhipster/domain/PeliculaTest.java
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/repository/search/CategoriaSearchRepositoryMockConfiguration.java
   create src/test/java/com/cev/accesoadatos/tema2/jhipster/web/rest/PeliculaResourceIT.java
   create src/test/java/com/cev/accesoadatos/tema2/jhipster/repository/search/PeliculaSearchRepositoryMockConfiguration.java
    force src/main/webapp/app/entities/categoria/categoria.model.ts
    force src/main/webapp/app/entities/categoria/update/categoria-update.component.ts
    force src/main/webapp/app/entities/categoria/list/categoria.component.html
    force src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.ts
    force src/main/webapp/app/entities/categoria/detail/categoria-detail.component.html
    force src/main/webapp/app/entities/categoria/detail/categoria-detail.component.spec.ts
    force src/main/webapp/app/entities/entity-routing.module.ts
    force src/main/resources/config/liquibase/master.xml
    force src/main/webapp/app/entities/categoria/categoria.module.ts
    force src/main/webapp/app/entities/categoria/list/categoria.component.spec.ts
    force src/main/webapp/app/entities/categoria/route/categoria-routing.module.ts
    force src/main/webapp/app/entities/categoria/route/categoria-routing-resolve.service.spec.ts
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/web/rest/CategoriaResource.java
    force src/main/webapp/app/entities/categoria/route/categoria-routing-resolve.service.ts
    force src/main/webapp/app/entities/categoria/service/categoria.service.spec.ts
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/search/CategoriaSearchRepository.java
    force src/main/webapp/app/entities/categoria/list/categoria.component.ts
    force src/main/webapp/app/entities/categoria/update/categoria-update.component.spec.ts
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/CategoriaRepository.java
    force src/main/webapp/app/entities/categoria/detail/categoria-detail.component.ts
    force src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.spec.ts
    force src/main/webapp/app/entities/categoria/service/categoria.service.ts
    force src/main/webapp/app/entities/categoria/update/categoria-update.component.html
    force src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.html
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/config/CacheConfiguration.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/domain/Categoria.java
    force src/test/java/com/cev/accesoadatos/tema2/jhipster/web/rest/CategoriaResourceIT.java
    force src/test/java/com/cev/accesoadatos/tema2/jhipster/domain/CategoriaTest.java
    force src/main/webapp/app/layouts/navbar/navbar.component.html

No change to package.json was detected. No package manager install will be executed.
Entity Pelicula generated successfully.
Entity Categoria generated successfully.

Running `webapp:build` to update client app


> jhipster@0.0.1-SNAPSHOT webapp:build /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> npm run clean-www && npm run webapp:build:dev


> jhipster@0.0.1-SNAPSHOT clean-www /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> rimraf target/classes/static/app/{src,target/}


> jhipster@0.0.1-SNAPSHOT webapp:build:dev /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> ng build --configuration development

⠙ Generating browser application bundles (phase: building)...<w> [webpack.cache.PackFileCacheStrategy] Restoring pack failed from /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster/target/webpack/default-development.pack: TypeError: Cannot read property 'length' of undefined
✔ Browser application bundle generation complete.
✔ Copying assets complete.
✔ Index html generation complete.

Initial Chunk Files                                                    | Names         |      Size
styles.css                                                             | styles        | 183.38 kB
polyfills.js                                                           | polyfills     | 134.19 kB
runtime.js                                                             | runtime       |  12.21 kB
main.js                                                                | main          | 780 bytes

                                                                       | Initial Total | 330.53 kB

Lazy Chunk Files                                                       | Names         |      Size
src_main_webapp_bootstrap_ts.js                                        | -             |   4.35 MB
src_main_webapp_app_admin_metrics_metrics_module_ts.js                 | -             | 167.86 kB
src_main_webapp_app_account_account_module_ts.js                       | -             | 156.05 kB
src_main_webapp_app_admin_user-management_user-management_module_ts.js | -             | 105.99 kB
src_main_webapp_app_entities_categoria_categoria_module_ts.js          | -             |  96.62 kB
src_main_webapp_app_entities_pelicula_pelicula_module_ts.js            | -             |  88.69 kB
src_main_webapp_app_admin_health_health_module_ts.js                   | -             |  28.73 kB
src_main_webapp_app_admin_configuration_configuration_module_ts.js     | -             |  25.41 kB
src_main_webapp_app_admin_logs_logs_module_ts.js                       | -             |  23.13 kB
src_main_webapp_app_login_login_module_ts.js                           | -             |  16.12 kB
src_main_webapp_app_admin_docs_docs_module_ts.js                       | -             |   4.69 kB
src_main_webapp_app_admin_admin-routing_module_ts.js                   | -             |   4.09 kB
common.js                                                              | common        |   2.06 kB

Build at: 2021-10-12T15:04:10.945Z - Hash: 5035a57dbfeac83de765 - Time: 28622ms
Congratulations, JHipster execution is complete!
Sponsored with ❤️  by @oktadev.


```



### One to One con JHipster

En primer lugar, creo la entidad `Estreno`:

```
╰─ jhipster entity estreno
INFO! Using JHipster version installed locally in current project's node_modules

The entity Estreno is being created.


Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? fecha
? What is the type of your field? Instant
? Do you want to add validation rules to your field? No

================= Estreno =================
Fields
fecha (Instant) 


Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? lugar
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Minimum length, Maximum length
? What is the minimum length of your field? 10
? What is the maximum length of your field? 255

================= Estreno =================
Fields
fecha (Instant) 
lugar (String) required minlength='10' maxlength='255'


Generating field #3

? Do you want to add a field to your entity? No

================= Estreno =================
Fields
fecha (Instant) 
lugar (String) required minlength='10' maxlength='255'


Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? pelicula
? What is the name of the relationship? pelicula
? What is the type of the relationship? one-to-one
? Is this entity the owner of the relationship? Yes
? Do you want to use JPA Derived Identifier - @MapsId? No
? What is the name of this relationship in the other entity? estreno
? When you display this relationship on client-side, which field from 'pelicula' do you want to use? This field will be displayed as a String, so it cannot be a Blob titulo
? Do you want to add any validation rules to this relationship? No

================= Estreno =================
Fields
fecha (Instant) 
lugar (String) required minlength='10' maxlength='255'

Relationships
pelicula (Pelicula) one-to-one 


Generating relationships to other entities

? Do you want to add a relationship to another entity? No

================= Estreno =================
Fields
fecha (Instant) 
lugar (String) required minlength='10' maxlength='255'

Relationships
pelicula (Pelicula) one-to-one 



? Do you want to use separate service class for your business logic? No, the REST controller should use the repository directly
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with pagination links and sorting headers

Everything is configured, generating the entity...


Found the .jhipster/Categoria.json configuration file, entity can be automatically generated!


Found the .jhipster/Pelicula.json configuration file, entity can be automatically generated!

     info Creating changelog for entities Categoria,Pelicula,Estreno
jsxBracketSameLine is deprecated.
    force .yo-rc-global.json
    force .yo-rc.json
    force .jhipster/Estreno.json
    force .jhipster/Categoria.json
    force .jhipster/Pelicula.json
   create src/main/webapp/app/entities/estreno/estreno.model.ts
   create src/main/webapp/app/entities/estreno/update/estreno-update.component.ts
 conflict src/main/webapp/app/entities/categoria/categoria.module.ts
? Overwrite src/main/webapp/app/entities/categoria/categoria.module.ts? overwrite this and all others
    force src/main/webapp/app/entities/categoria/categoria.module.ts
   create src/main/webapp/app/entities/estreno/list/estreno.component.html
   create src/main/webapp/app/entities/estreno/delete/estreno-delete-dialog.component.ts
   create src/main/webapp/app/entities/estreno/detail/estreno-detail.component.html
   create src/main/webapp/app/entities/estreno/detail/estreno-detail.component.spec.ts
   create src/main/webapp/app/entities/estreno/estreno.module.ts
   create src/main/webapp/app/entities/estreno/list/estreno.component.spec.ts
   create src/main/webapp/app/entities/estreno/route/estreno-routing.module.ts
   create src/main/webapp/app/entities/estreno/route/estreno-routing-resolve.service.spec.ts
   create src/main/webapp/app/entities/estreno/route/estreno-routing-resolve.service.ts
   create src/main/webapp/app/entities/estreno/service/estreno.service.spec.ts
   create src/main/webapp/app/entities/estreno/list/estreno.component.ts
   create src/main/webapp/app/entities/estreno/update/estreno-update.component.spec.ts
identical src/main/webapp/app/entities/categoria/update/categoria-update.component.html
   create src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/EstrenoRepository.java
   create src/main/webapp/app/entities/estreno/detail/estreno-detail.component.ts
   create src/main/webapp/app/entities/estreno/delete/estreno-delete-dialog.component.spec.ts
identical src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.html
```

A continuación, añado la relación en `Pelicula`:

```shell
? Is this entity the owner of the relationship? No
? What is the name of this relationship in the other entity? pelicula

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 
enCines (Boolean) 

Relationships
estreno (Estreno) one-to-one 


Generating relationships to other entities

? Do you want to add a relationship to another entity? No

================= Pelicula =================
Fields
titulo (String) required minlength='10' maxlength='255'
fechaEstreno (Instant) 
descripcion (String) 
enCines (Boolean) 

Relationships
estreno (Estreno) one-to-one 




Found the .jhipster/Categoria.json configuration file, entity can be automatically generated!


Found the .jhipster/Estreno.json configuration file, entity can be automatically generated!

     info Creating changelog for entities Categoria,Pelicula,Estreno
jsxBracketSameLine is deprecated.
    force .yo-rc.json
    force .yo-rc-global.json
    force .jhipster/Pelicula.json
    force .jhipster/Categoria.json
    force .jhipster/Estreno.json
 conflict src/main/webapp/app/entities/pelicula/pelicula.model.ts
? Overwrite src/main/webapp/app/entities/pelicula/pelicula.model.ts? overwrite this and all others
    force src/main/webapp/app/entities/pelicula/pelicula.model.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/web/rest/PeliculaResource.java
identical src/main/webapp/app/entities/pelicula/service/pelicula.service.ts
identical src/main/webapp/app/entities/pelicula/route/pelicula-routing-resolve.service.spec.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/CategoriaRepository.java
identical src/main/webapp/app/entities/categoria/detail/categoria-detail.component.ts
identical src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.spec.ts
identical src/main/webapp/app/entities/pelicula/list/pelicula.component.html
identical src/main/webapp/app/entities/pelicula/update/pelicula-update.component.html
identical src/main/webapp/app/entities/pelicula/service/pelicula.service.spec.ts
identical src/main/webapp/app/entities/categoria/service/categoria.service.ts
identical src/main/webapp/app/entities/pelicula/update/pelicula-update.component.spec.ts
identical src/main/webapp/app/entities/categoria/update/categoria-update.component.html
identical src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.html
identical src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.html
identical src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.spec.ts
identical src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.html
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/search/PeliculaSearchRepository.java
identical src/main/webapp/app/entities/pelicula/pelicula.module.ts
identical src/main/webapp/app/entities/categoria/categoria.model.ts
identical src/main/webapp/app/entities/categoria/update/categoria-update.component.ts
identical src/main/webapp/app/entities/pelicula/update/pelicula-update.component.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/PeliculaRepository.java
identical src/main/webapp/app/entities/categoria/list/categoria.component.html
identical src/main/webapp/app/entities/categoria/delete/categoria-delete-dialog.component.ts
identical src/main/webapp/app/entities/categoria/detail/categoria-detail.component.html
identical src/main/webapp/app/entities/categoria/detail/categoria-detail.component.spec.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/web/rest/EstrenoResource.java
identical src/main/webapp/app/entities/pelicula/route/pelicula-routing.module.ts
identical src/main/webapp/app/entities/pelicula/route/pelicula-routing-resolve.service.ts
identical src/main/webapp/app/entities/pelicula/delete/pelicula-delete-dialog.component.ts
identical src/main/webapp/app/entities/categoria/categoria.module.ts
identical src/main/webapp/app/entities/categoria/list/categoria.component.spec.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/search/EstrenoSearchRepository.java
identical src/main/webapp/app/entities/pelicula/list/pelicula.component.ts
identical src/main/resources/config/liquibase/fake-data/blob/hipster.png
identical src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.spec.ts
identical src/main/resources/config/liquibase/changelog/20211012145248_added_entity_Categoria.xml
identical src/main/webapp/app/entities/categoria/route/categoria-routing.module.ts
identical src/main/resources/config/liquibase/fake-data/categoria.csv
identical src/main/webapp/app/entities/categoria/route/categoria-routing-resolve.service.spec.ts
identical src/main/resources/config/liquibase/changelog/20211012150330_added_entity_Pelicula.xml
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/EstrenoRepository.java
identical src/main/resources/config/liquibase/fake-data/pelicula.csv
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/web/rest/CategoriaResource.java
identical src/main/webapp/app/entities/categoria/route/categoria-routing-resolve.service.ts
identical src/main/webapp/app/entities/categoria/service/categoria.service.spec.ts
identical src/main/webapp/app/entities/pelicula/detail/pelicula-detail.component.ts
identical src/main/resources/config/liquibase/changelog/20211012153015_added_entity_Estreno.xml
identical src/main/webapp/app/entities/pelicula/list/pelicula.component.spec.ts
identical src/main/resources/config/liquibase/changelog/20211012153015_added_entity_constraints_Estreno.xml
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/repository/search/CategoriaSearchRepository.java
identical src/main/resources/config/liquibase/fake-data/estreno.csv
identical src/main/webapp/app/entities/categoria/list/categoria.component.ts
identical src/main/resources/config/liquibase/master.xml
identical src/main/webapp/app/entities/categoria/update/categoria-update.component.spec.ts
identical src/main/webapp/app/entities/estreno/estreno.model.ts
identical src/main/webapp/app/entities/estreno/list/estreno.component.html
identical src/main/webapp/app/entities/estreno/detail/estreno-detail.component.html
identical src/main/webapp/app/entities/estreno/detail/estreno-detail.component.spec.ts
identical src/main/webapp/app/entities/estreno/estreno.module.ts
identical src/main/webapp/app/entities/estreno/list/estreno.component.spec.ts
identical src/main/webapp/app/entities/estreno/route/estreno-routing.module.ts
identical src/main/webapp/app/entities/estreno/route/estreno-routing-resolve.service.spec.ts
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/domain/PeliculaTest.java
identical src/main/webapp/app/entities/estreno/route/estreno-routing-resolve.service.ts
identical src/main/webapp/app/entities/estreno/list/estreno.component.ts
identical src/main/webapp/app/entities/estreno/detail/estreno-detail.component.ts
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/web/rest/EstrenoResourceIT.java
identical src/main/webapp/app/entities/estreno/service/estreno.service.ts
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/repository/search/EstrenoSearchRepositoryMockConfiguration.java
identical src/main/webapp/app/entities/estreno/update/estreno-update.component.html
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/domain/EstrenoTest.java
identical src/main/webapp/app/entities/estreno/delete/estreno-delete-dialog.component.html
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/web/rest/CategoriaResourceIT.java
identical src/main/webapp/app/entities/estreno/update/estreno-update.component.ts
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/repository/search/CategoriaSearchRepositoryMockConfiguration.java
identical src/main/webapp/app/entities/estreno/delete/estreno-delete-dialog.component.ts
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/domain/CategoriaTest.java
identical src/test/java/com/cev/accesoadatos/tema2/jhipster/repository/search/PeliculaSearchRepositoryMockConfiguration.java
identical src/main/webapp/app/entities/estreno/service/estreno.service.spec.ts
identical src/main/webapp/app/entities/estreno/update/estreno-update.component.spec.ts
identical src/main/webapp/app/entities/estreno/delete/estreno-delete-dialog.component.spec.ts
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/config/CacheConfiguration.java
identical src/main/webapp/app/layouts/navbar/navbar.component.html
identical src/main/java/com/cev/accesoadatos/tema2/jhipster/domain/Categoria.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/service/PeliculaQueryService.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/service/PeliculaService.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/service/criteria/PeliculaCriteria.java
    force src/test/java/com/cev/accesoadatos/tema2/jhipster/web/rest/PeliculaResourceIT.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/domain/Pelicula.java
    force src/main/java/com/cev/accesoadatos/tema2/jhipster/domain/Estreno.java

No change to package.json was detected. No package manager install will be executed.
Entity Pelicula generated successfully.
Entity Categoria generated successfully.
Entity Estreno generated successfully.

Running `webapp:build` to update client app


> jhipster@0.0.1-SNAPSHOT webapp:build /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> npm run clean-www && npm run webapp:build:dev


> jhipster@0.0.1-SNAPSHOT clean-www /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> rimraf target/classes/static/app/{src,target/}


> jhipster@0.0.1-SNAPSHOT webapp:build:dev /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
> ng build --configuration development

✔ Browser application bundle generation complete.
✔ Copying assets complete.
✔ Index html generation complete.

Initial Chunk Files                                                    | Names         |      Size
styles.css                                                             | styles        | 183.38 kB
polyfills.js                                                           | polyfills     | 134.19 kB
runtime.js                                                             | runtime       |  12.21 kB
main.js                                                                | main          | 780 bytes

                                                                       | Initial Total | 330.53 kB

Lazy Chunk Files                                                       | Names         |      Size
src_main_webapp_bootstrap_ts.js                                        | -             |   4.36 MB
src_main_webapp_app_admin_metrics_metrics_module_ts.js                 | -             | 167.86 kB
src_main_webapp_app_account_account_module_ts.js                       | -             | 156.05 kB
src_main_webapp_app_admin_user-management_user-management_module_ts.js | -             | 105.23 kB
src_main_webapp_app_entities_estreno_estreno_module_ts.js              | -             |  99.88 kB
src_main_webapp_app_entities_categoria_categoria_module_ts.js          | -             |  96.62 kB
src_main_webapp_app_entities_pelicula_pelicula_module_ts.js            | -             |  80.57 kB
src_main_webapp_app_admin_health_health_module_ts.js                   | -             |  28.73 kB
src_main_webapp_app_admin_configuration_configuration_module_ts.js     | -             |  25.41 kB
src_main_webapp_app_admin_logs_logs_module_ts.js                       | -             |  23.13 kB
src_main_webapp_app_login_login_module_ts.js                           | -             |  16.12 kB
common.js                                                              | common        |  10.98 kB
src_main_webapp_app_admin_docs_docs_module_ts.js                       | -             |   4.69 kB
src_main_webapp_app_admin_admin-routing_module_ts.js                   | -             |   4.09 kB

Build at: 2021-10-12T15:33:14.304Z - Hash: e7075156dce71fc042c5 - Time: 18093ms
Congratulations, JHipster execution is complete!
Sponsored with ❤️  by @oktadev.
```
