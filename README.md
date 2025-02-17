## Overview

Ecommerce in Java is a study project where I practice and learning Java and Spring Boot.

The project has like a objective simulate a ecommerce basic with the creation of its endpoints.

Its features are:
- [x] Authentication of the user for access the application
- [x] CRUD of Products 
- [x] CRUD of stock and control of the quantity of items
- [x] CRUD of Addresses (of delivery) of user
- [x] Creation of carts of purchases by the user.
- [x] Purchase process, with addition of delivery address, payment method and final payment check.
- [x] Stripe integration for payment methods.
- [x] Integration with the Gmail SMTP for sending emails.
- [x] Sending emails to the user alerting about: accounts created, access code generated and purchases made


## Getting Started

For test in project is necessary have:
- A `Stripe` account for payments setup and sandbox testing.
- A `Gmail` account for generate the access for sending emails.
- A `Database` (using in the Microsoft SQL Server project) to create and use the database in the project.
- An IDE to run Java, such as `IntelliJ`, `Eclipse`, or any other IDE you want.
- And a tool to test requests, such as `Insominia`, `Postman` or other.


## Steps for run this project

1. Clone this repository
    ```sh
       git clone https://github.com/WesleyRodrigues55/ecommerce-in-java.git
    ```

2. Go to the project folder and install the dependencies
   ```sh
      mvn clean install
   ```
   
3. Run the project with spring-boot
   ```sh
      mvn spring-boot:run
   ```
   
4. Now you can test the requests using some tool of requesting (Insomnia, Postman, etc.)

   The collection used in `Insomnia` is here [collection](https://github.com/WesleyRodrigues55/ecommerce-in-java/blob/main/collectionsInsomnia.json).
   And please don't forget to configure the file `application.properties`, with access to the database, SMTP, gmail, token and Stripe. 
   *There is a sample file in the repository.


5. And you can also run Swagger to see sample requests by going to the link below (after initializing the project).
   ```sh
      http://localhost:8080/swagger-ui/index.html
   ```