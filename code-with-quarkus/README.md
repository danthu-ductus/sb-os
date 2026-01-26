# Ductus Quarkus+OpenShift Demo Project

### Requirements
---
* Java 17, use for example `https://adoptium.net/`
* Docker

### Running the Project
---

* DevMode, you will have live reloads for service logic and endpoint declaration when Quarkus run locally instead of in a container. 
    - Build the project: `\.mvnw clean package` or `\.mvnw clean install`
    - Start the DB container (postgresql): `docker compose up postgres-db`
    - Run the Migration container (inits tables): `docker compose run --rm flyway-migrate`
    - Run the Quarkus locally in dev mode: `\.mwnw quarkus:dev`   

* Prod, run the services inside docker containers (meant to resemble some more meaningful Openshift practice).
    - Build the project: `\.mvnw clean package` or `\.mvnw clean install`
    - Run the containers (DB start, DB migrate, Quarkus app): `docker compose up --build`

### Dev Notes
---
* There are some really good Quarkus guide on how to do different stuff: `https://quarkus.io/guides/`
* The default swagger ui url is: `http://localhost:8080/q/swagger-ui`
* Do you want to check that something u POST actually will persist in the DB? Then you can do the following to access the DB cli inside the container:
```pws
> docker compose exec postgres-db psql -U demo -d demo
> \dt //\dt will list tables

               List of relations
 Schema |         Name          | Type  | Owner
--------+-----------------------+-------+-------
 public | flyway_schema_history | table | demo
 public | users                 | table | demo


> SELECT * FROM users; // will list all the users in the table

> \q // exit

````
 
<small>Below follows the starter README info generated from initializing a Quarkus project as explain in this guide: `https://quarkus.io/get-started/` </small>

## code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

### Running the application in dev mode
---

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

### Packaging and running the application
---

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable
---

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

### Provided Code
---
#### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
