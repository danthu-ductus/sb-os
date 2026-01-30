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
Start your cluster and open the web-console to monitor the cluster easily. An overview can be found through clicking: `Home -> Projects -> <your project name> -> Workloads`. This shows only the pods and workloads created within your project, and makes it easier to monitor everything.

The creators of this repository have set up the cluster using Kubernetes/OpenShift manifests. In particular a deploymentconfig manifest which is deprecated practice, but well suited as a start for the demo. This can be read about in Chapter-4.3 in the [OpenShift - Guide](https://openshift.guide/openshift-guide-screen.pdf). It is seen as best practice to use a GitOps-based approach like **Argo CD** for continuous delivery (CD), enabling automated and declarative updates of the application and database. However, this ahsn't been introduced by the creators of the project and will not be covered here. It might still be worth adding if you have the time for it.

* First we check that we are inside the correct OpenShift project: `oc project` -> should return "project name", else switch to it.

* Start the database pod: `oc apply -f k8s/postgres.yaml`, check in the OpenShift console if the pod has started and that you can access the terminal, you should be able to access the psql db from its cli with the credentials set in the `postgres.yaml` manifest you just applied.

* Make a configmap for the flyway migration before applying the flyway job, `oc create configmap flyway-sql --from-file=src/main/resources/db/migration`. Without this the flyway manifest won't be able to set up the tables as described in every 'Vy__xxx'.

    __NOTE__ This is a step that could be implemented in another way, maybe as a sub-job that run when applying the postgres manifest this might be a good exercise to implement if you would like to practice implementing kubernetes objects? 

* Next you apply the flyway migration through a OpenShift job, which sets up the correct database tables, `oc apply -f k8s/flyway-job.yaml`.

* Build the image for the app, from the repository, using the buildconfig yaml manifest: `oc apply -f k8s/buildconfig.yaml`. If you check the `k8s/buildconfig.yaml` you can set the repo url and the target branch that the image is built from.

* When you can see that the pod for the app have a 'Running' status in the OpenShift web-console, you should now be able to run the `oc get routes` to extract the url where you can reach your application endpoints.


## Continue Contributing to the Demo

This demo was crated created to practice using the tools to implement Java applications with Quarkus running in an OpenShift cluster. However this is still not fully realistic due to there being only a few dummy endpoints. No realistic testing pipelines and only one OpenShift cluster with two running pods. But hopefully, following these steps, you have familiarized yourself with the tools.

As stated earlier, contribute to the project if you think there are some changes that should be made. Or that something should be added to make this more realistic, or generally improve any of the steps. Maybe you think that there should be a more solid deployment pipeline running ArgoCD or maybe you'd like to practice Ghurking tests? Then please create a PR, or post an issue about it for future developments to the demo.


## References
This is a list of the docs that the creators have used during the creation of the demo:
* [Quarkus - Get Started](https://quarkus.io/get-started/)
* [Quarkus - Guide](https://quarkus.io/guides)
* [Quarkus - Qickstart Git](https://github.com/quarkusio/quarkus-quickstarts/tree/main)
* [OpenShift - Guide](https://openshift.guide)
* [Kubernetes - Docs](https://kubernetes.io/docs/home/), really nice when you get to know manifests.
* [Redhat - Containers](https://catalog.redhat.com/en/search?searchType=containers), nice to find images for rhel with different versions.

## Creators to reach out to if you have any questions
Daniel Thungren - daniel.thungren@ductus.se
Jacob Möller    - jacob.moller@ductus.se