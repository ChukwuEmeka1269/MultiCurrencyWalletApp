# MultiCurrencyWalletApp

A Multi-Currency Wallet Application

##### Author : Ukwuoma Emeka Paul

This is a RESTful API for a multicurrency wallet application.

### Technologies

- Java
- Maven
- Springboot
- Docker
- Postman
- Swagger
- MySQL

### Requirements

You need the following to build and run the application:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8.1](https://maven.apache.org) (Optional as code already contains maven wrapper)
- [H2](https://www.h2database.com/)
- [Docker](https://hub.docker.com/) (To start up a docker server)

## How to run

### Step 1 - clone project from github [here](https://github.com/ChukwuEmeka1269/MultiCurrencyWalletApp)

### Step 2 - CD into the project directory

```
cd multiCurrencyWalletApp/
```

### Step 3 - Build And Run The Application

Execute the following commands:

```bash
./mvnw clean install     # Build the project
docker-compose up        # Run the project using Docker
````

### Additional Information

Kindly note that the application would start on `Port 9000` so endpoints would be accessible at
`http://localhost:9000/**`. Use the appropriate HTTP methods `GET` `POST` etc. to access endpoints with you preferred
http client e.g Postman

For ease of use and to test the functionality I have made documentation of the endpoints here.

After running the Application, You can see the documentation on swagger and test the end points.

#### Full `SWAGGER` documentation can be found [here](http://localhost:8080/swagger-ui/index.html) and the `POSTMAN` documentation can be found [here](https://crimson-crescent-996633.postman.co/workspace/BackOffice-Value-Stream~9f6c99aa-539b-4ca0-ba3d-b230f590015e/collection/15908398-b60aa070-edab-404e-893f-d8758ea4fd39?action=share&creator=15908398&active-environment=15908398-634fb0ec-52d1-4e02-b5a6-abf30a8c9e90).