# Customer Service

## Introduction
Customer Service is an open source platform that enable api users create update retrieve and delete customers.

## Project Support Features
* Users can retrieve all customers
* Users can retrieve a customer from a given id
* Users can update a customer
* Users can delete a customer

## Usage
Please make sure you have docker installed in your environment to run this application with given samples
* Run `mvn install` to generate jar file.
* Run `make up` to start the application.
* Connect to the API using Postman on port 8080.


## API Endpoints
| HTTP Verbs | Endpoints                        | Action                                             |
|------------|----------------------------------|----------------------------------------------------|
| POST       | /customer                        | Create customer                                    |
| GET        | /customer/:pageNumber/:pageSize  | Get all customers with given pageNumber page size  |
| GET        | /customer/:customerId            | Get customer from id                               |
| PUT        | /customer                        | Update customer                                    |
| DELETE     | /customer                        | Delete customer                                    |

![Alt text](./swagger/swagger.PNG?raw=true "API definition screenshot")


## API Usage (Swagger UI Documentation)
The API documentation can be found on Swagger UI. To view it, please visit: Swagger UI.


swagger.json: `http://127.0.0.1:8080/api-docs`

swagger-ui: `http://127.0.0.1:8080/swagger-ui.html`


## Deployment (*WIP)
The customer-service project can be deployed using terraform files under `deployment/terraform`. The work is still in progress and only contains deployment files for API service and does not contain dependent services.

## Technologies Used

### Spring-Boot
The Spring framework is dominating the java world for quite some time and it’s a very well-established framework Spring has the most significant community out of all frameworks and integrations with almost every third-party system.

Spring-Boot provides a simple way to create stand-alone, production-grade Spring-based applications. It is designed to simplify the bootstrapping and development of new Spring applications. Spring Boot also makes it easy to create production-ready applications by providing features such as embedded servers, configuration management, health checks, and security.

#### Why Spring-boot
* Documentation & Community support
* Wide range of features and plugins
* Developer experience
* Production-readiness: Provides features like health checks, metrics, and externalized configuration that make it easy to create production-ready applications.

### Spring Data JPA
Simplifying the database access layer by reducing the boilerplate code.

### Why SQL
* Data integrity. SQL databases deliver a high degree of data integrity, adhering to the principles of atomicity, consistency, isolation, and durability (ACID)
* Schema of the database is well defined

### Redis
Supporting primary database as cache layer.
Redis stores data in the server's main memory rather than on hard disks and solid-state drives. This leads to significantly faster response times when performing read and write operations.

### Test Containers
Redis stores data in the server's main memory rather than on hard disks and solid-state drives. This leads to significantly faster response times when performing read and write operations.

### OpenAPI (Swagger)
Enabling seamless API documentation for better understanding and testing.

## Credits

Customer-service is created and maintained by Anil Mercan

* I am open to suggestions, feel free to email mercanil@gmail.com

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.4/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#web)

## License
This project is available for use under the MIT License.