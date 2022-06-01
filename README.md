# ![RealWorld Example App](logo.png)

> ### Spring boot codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.


### [Demo](https://demo.realworld.io/)&nbsp;&nbsp;&nbsp;&nbsp;[RealWorld](https://github.com/gothinkster/realworld)


This codebase was created to demonstrate a fully fledged fullstack application built with Spring boot including CRUD operations, authentication, routing, pagination, and more.

List of available endpoints is described [here](https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints)

# How it works

General features:

- Authenticate users via JWT (login/signup pages + logout button on settings page)
- CRU* users (sign up & settings page - no deleting required)
- CRUD Articles
- CR*D Comments on articles (no updating required)
- GET and display paginated lists of articles
- Favorite articles
- Follow other users\

for more details regarding available endpoints head [here](https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints).

Application was created using:
- Java 11
- Spring Boot 2
- Spring Data
- Spring Security
- H2 database
- JUnit5
- Mockito

# Getting started
Prerequisites:
- Java 11 installed

Clone the repository and start project using 
``` ./mvnw spring-boot:run ```

## Running tests
In order to run tests use:
```./mvnw test```


Additional tests may be performed using postman collection [Postman Collection](https://github.com/gothinkster/realworld/blob/main/api/Conduit.postman_collection.json).

![tests](testresult.png)

