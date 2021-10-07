# Acceso a datos, Tema 2: JHipster

## Primeros pasos o cosas que debemos tener en nuestro equipo:
* Un entorno de desarrollo con el que nos sintamos cómodos.
* Maven
* JDK 11 o posterior, preferentemente una versión LTS.
* Node (Versión LTS)

## Creación de un proyecto.

Simplemente, lanzamos `jhipster` (comando) en la carpeta que queramos y seguimos las preguntas, como en el video.

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
    
    ? Which *type* of application would you like to create? Monolithic application (recommended for simple projects)
    ? What is the base name of your application? jhipster
    ? Do you want to make it reactive with Spring WebFlux? No
    ? What is your default Java package name? com.cev.ad.tema2
    ? Which *type* of authentication would you like to use? JWT authentication (stateless, with a token)
    ? Which *type* of database would you like to use? SQL (H2, PostgreSQL, MySQL, MariaDB, Oracle, MSSQL)
    ? Which *production* database would you like to use? PostgreSQL
    ? Which *development* database would you like to use? H2 with in-memory persistence
    ? Which cache do you want to use? (Spring cache abstraction) Ehcache (local cache, for a single node)
    ? Do you want to use Hibernate 2nd level cache? Yes
    ? Would you like to use Maven or Gradle for building the backend? Maven
    ? Do you want to use the JHipster Registry to configure, monitor and scale your application? No
    ? Which other technologies would you like to use? Elasticsearch as search engine
    ? Which *Framework* would you like to use for the client? Angular
    ? Do you want to generate the admin UI? Yes
    ? Would you like to use a Bootswatch theme (https://bootswatch.com/)? Litera
    ? Choose a Bootswatch variant navbar theme (https://bootswatch.com/)? Light
    ? Would you like to enable internationalization support? No
    ? Please choose the native language of the application Spanish
    ? Besides JUnit and Jest, which testing frameworks would you like to use?
    ? Would you like to install other generators from the JHipster Marketplace? No
    
    KeyStore 'src/main/resources/config/tls/keystore.p12' generated successfully.
    
       create .prettierrc
       create .prettierignore
       create .gitattributes
    jsxBracketSameLine is deprecated.
       create .editorconfig
       create .husky/pre-commit
       create package.json
       create .gitignore
        force .yo-rc-global.json
       create sonar-project.properties
        force .yo-rc.json
        
        (...)
        
    58 vulnerabilities (7 moderate, 16 high, 35 critical)
    
    To address issues that do not require attention, run:
      npm audit fix
    
    To address all issues possible (including breaking changes), run:
      npm audit fix --force
    
    Some issues need review, and may require choosing
    a different dependency.
    
    Run `npm audit` for details.
    Application successfully committed to Git from /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster.
    
    If you find JHipster useful consider sponsoring the project https://www.jhipster.tech/sponsors/
    
    Server application generated successfully.
    
    Run your Spring Boot application:
    ./mvnw
    
    Client application generated successfully.
    
    Start your Webpack development server with:
     npm start
    
    
    > jhipster@0.0.1-SNAPSHOT clean-www /Users/jose/PROYECTOS/CEV/AD/acceso-a-datos-tema2-jhipster
    > rimraf target/classes/static/app/{src,target/}
    
    Congratulations, JHipster execution is complete!
    Sponsored with ❤️  by @oktadev.
    
    ```

En la parte final, tenemos un mensaje clave:

_Server application generated successfully._

**_Run your Spring Boot application:_**
**_./mvnw_**

_Client application generated successfully._

**_Start your Webpack development server with:_**
**_npm start_**

