# Ductus demo project for: Quarkus, Cucumber:Gherkin and OpenShift

101-Tutorial for setting up a Quarkus application running either docker (locally) or on an OpenShift cluster through OpenShift local (CRC). Meant as a demo project to familiarize oneself with these tools.

We suggest that you fork this repo and learn it in your own environment, if you think that this demo should be updated in some way when following the steps, please clone this repo and create a PR or simply add an issue. 

Branches:
 
 * main = the demo goal, this can work as a cheat sheet if you get stuck.
 * demo_start = the branch we created for you to start in.

### Requirements
---
* Git installed on your machine
* A fork of this repository
* Java 17, use for example `https://adoptium.net/`
* Docker
* RedHat developer account (more on this later)
* Hardware: 32GB RAM and at least 80GB storage available for CRC (more on this later)




### Running the Project
---

* DevMode, you will have live reloads for service logic and endpoint declaration when Quarkus run locally instead of in a container. 
    - Build the project: `.\mvnw clean package` or `.\mvnw clean install`
    - Start the DB container (postgresql): `docker compose up postgres-db`
    - Run the Migration container (inits tables): `docker compose run --rm flyway-migrate`
    - Run the Quarkus locally in dev mode: `.\mwnw quarkus:dev`   

* Prod, run the services inside docker containers (meant to resemble some more meaningful Openshift practice).
    - Build the project: `.\mvnw clean package` or `.\mvnw clean install`
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



## Requirements
something

## Running
something

## Dev Note
something
