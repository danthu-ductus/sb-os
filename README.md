# Ductus demo project for: Quarkus, Cucumber:Gherkin and OpenShift

101-Tutorial for setting up a Quarkus application running either docker (locally) or on an OpenShift cluster through OpenShift local (CRC). Meant as a demo project to familiarize oneself with the mentioned tools.

We suggest that you fork this repo and learn it in your own environment, if you think that this demo should be updated in some way with new information during your demo-work, please clone this repo and create a PR or simply add an issue on github [Link]. 

**Branches:**
 
 * **main**: Represents the final state of the demo, showing one possible implementation, serving as a reference solution (which of course can be improved).
 * **demo**: The branch we created for you to start in.


## Requirements
* Git installed on your machine
* A fork of this repository
* Java 17, you can for example use a JDK from [Adoptium - Temurin](https://adoptium.net/temurin/releases)
* Docker, you can install it from Microsoft Store or from [Docker's Install Page](https://docs.docker.com/engine/install/)
* RedHat developer account (more on this later)
* Hardware: 32GB RAM and at least 80GB storage available for CRC (you can do it with less resources but more on this later)


## Setup
Before diving into deploying the application and database via OpenShift, you should first learn how to deploy containers locally (e.g. via Docker). When moving to OpenShift, which basically is a commercialized Kubernetes system that depends on containers, having some understanding of containerization is beneficial. If you are already familiar with running applications in docker etc, you can skip to [Setting Up Via OpenShift](#Setting-Up-Via-OpenShift) that continues with how to deploy the app via kubernetes manifests to the OpenShift cluster.

## Setting up via Docker (Pre-OpenShift)
1.  
    The easiest way to set up a starting point, is to follow the [Quarkus docs - get started](https://quarkus.io/get-started/)

2. 
    Connecting a db to the application, for example you can run a postgresql in a container that the application connects to. In `/docker-compose.yaml` there is a service defined to use v16 of the official postgresql image. Images for other dbs can be found in the [Docker-Hub](https://hub.docker.com/). 

3. 
    Make sure you can deploy both the app and database via Docker, and create a simple POST and GET endpoint to verify that communication between the app and the db. It is very useful to add a dependency that gives you a swagger-ui for the created endpoints. There is a [Quarkus guide](https://quarkus.io/guides/openapi-swaggerui) on how to add this to the project.

4. 
    Now that the database and application can be started reliably, the next step is to introduce Flyway for database migrations. The easiest way to do this is to follow the [Flyway Guide](https://quarkus.io/guides/flyway).

## Setting Up Via OpenShift
There are two options to use openshift without committing to a enterprise plan, first you can use RedHad Developer Sandbox and its 30-day trial. But we recommend that you run OpenShift local (CRC), this comes with the a limitation of only being able to have one cluster of pods running but its enough for this demo. 

5. 
    If you have not create a RedHat developer account you should do so now [RedHat - Register](https://sso.redhat.com/auth/realms/redhat-external/login-actions/registration?execution=9ec9ca44-2f3b-4b60-ac66-299705730f29&client_id=cloud-services&tab_id=GhlDBcTQ1Ic&client_data=eyJydSI6Imh0dHBzOi8vY29uc29sZS5yZWRoYXQuY29tL29wZW5zaGlmdC9jcmVhdGUvbG9jYWwiLCJydCI6ImNvZGUiLCJybSI6ImZyYWdtZW50Iiwic3QiOiIzODdkMGYwNzgzMzM0NTQ0ODJhZDU0N2JiMjE4MTIxNiJ9).

6.
    Follow the [Install OpenShift Local Guide](https://crc.dev/docs/installing/), this will ask you to go to the [Download Page](https://console.redhat.com/openshift/create/local) where you can find the installation files along with your **pull secret** that is needed later to link your local VM to your account. 
    
    **IMPORTANT**, set your VMs resource config before you run `crc setup`, since insufficient RAM or storage can lead to unstable pod deploys. If you need to change the VMs disk size you will need to delete the current one and init a new one so its better to set a generous config from start if you have the resources, it is really easy to delete from ur system later on with `crc delete`. Run the default config command mentioned in the [Using CRC Docs](https://crc.dev/docs/using/) or the commands below for the our suggested config: 

    ```
    crc config set memory 16384
    crc config set cpus 6
    crc config set disk-size 80
    ```

7. 
    When installed you can follow the [Using CRC Docs](https://crc.dev/docs/using/) on how to use the system that manages ur local cluster. 

    When you get to starting the cluster with `crc start` you will need the pull secret from earlier (go to [Download Page](https://console.redhat.com/openshift/create/local) and retrieve it if you did not save it earlier). You will know that the cluster is running if you see an output like below:

    ```
    INFO Waiting until the user's pull secret is written to the instance disk...
    INFO Adding crc-admin and crc-developer contexts to kubeconfig...
    Started the OpenShift cluster.

    The server is accessible via web console at:
    https://console-openshift-console.apps-crc.testing

    Log in as administrator:
    Username: kubeadmin
    Password: SOME-GENERATED-PASSWORD

    Log in as user:
    Username: developer
    Password: developer

    Use the 'oc' command line interface:
    PS> & crc oc-env | Invoke-Expression
    PS> oc login -u developer https://api.crc.testing:PORT

    OR Open the web-console with:
    PS> crc console
    ```

8. 
    You first get placed in a project called default, but its recommended that you set up another namesspace with `oc new-project <name>`. Also it is good practice to remember to check what OpenShift project you are targeting before building or deploying any changes with `oc project`.

Now you should have an environment where you can deploy pods with containers for the APP and DB in your local cluster.


## Running the Project

### Development Mode(s):
DevMode, the Quarkus app runs locally on you machine and talk to the container over local host, you will have live updates for service logic and endpoint declaration when Quarkus run locally instead of in a container. 

* Build the project: `.\mvnw clean package` or `.\mvnw clean install`, if there are any tests that do not pass due to recent implementation you can run `.\mvnw clean package -DskipTests`
* Start the DB container (postgresql): `docker compose up postgres-db`
* Run the Migration container (inits tables): `docker compose run --rm flyway-migrate`
* Run the Quarkus locally in dev mode: `.\mwnw quarkus:dev`   

DevMode, run the services inside docker containers (you will have to rebuild between updates).
* Build the project: `.\mvnw clean package` or `.\mvnw clean install`
* Run the containers (DB start, DB migrate, Quarkus app): `docker compose up --build`

### OpenShift Cluster Mode:
Start your cluster and open the web-console to be able to monitor the cluster easily. The project view can be found through clicks: `Home -> Projects -> <your project name> -> Workloads`. This view shows only the pods and workloads created within your project and makes it easier to follow deployments, jobs, and rollouts.

The creators of this repository have set up the cluster using Kubernetes/OpenShift manifests. In particular a deploymentconfig which is deprecated practice but it suited the demo well in the start, this can be read about in Chapter-4.3 in the [OpenShift - Guide](https://openshift.guide/openshift-guide-screen.pdf). It is seen as best practice to use a  GitOps-based approach like **Argo CD** for continuous delivery, enabling automated and declarative updates of the application and database.



## Dev Notes
* There are some really good Quarkus guide on how to do different stuff: `https://quarkus.io/guides/`
* The default swagger ui url is: `http://localhost:8080/q/swagger-ui`
* Do you want to check that something u POST actually will persist in the DB? Then you can do the following to access the DB cli inside the container:
    ```
    PS> docker compose exec postgres-db psql -U demo -d demo
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


## Dev Note
something
