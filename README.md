## Introduction

BookWeb is an online book trading web application that allows users to browse inventory, add books to cart, place an order, and make a purchase.  
BookWeb is suitable for individuals or businesses that want to create an online book store that allows for efficient order and inventory management.

## Technologies Used

- **Java 11**: The core programming language.
- **Spring Boot**: For building and deploying the web application.
- **Spring Data JPA**: To simplify database interactions.
- **Spring Security**: For securing the application and managing user authentication.
- **Swagger**: For API documentation and testing.
- **Docker Compose**: For running the application in Docker.
- **TestContainers**: Container with database used for testing.
- **JUnit**: For writing unit tests.
- **MockMvc**: For testing controllers in Spring.
- **Maven**: For dependency management and project build.

## Functionality

The application consists of several controllers that handle:

- User authentication and registration.
- CRUD operations for books and other models.
- Search functionality for finding books by title, author, or ISBN.
- Shopping Cart management: adding, updating, or removing items in the cart.
- Order processing: creating and managing orders.

## Database Schema

<p align="center">
  <img src="/DataBase_Schema.png" alt="database_schema"/>
</p>


## Setup Instructions

1. Clone the repository:
    
    ```bash
    git clone https://github.com/shumYevh/BookWeb.git
    ```

2. Launch the app on your local device or via Docker:
   - Change the `.env` file to your parameters (e.g., database settings, ports).
   - Run the application with the command:
     ```bash
     docker-compose up
     ```

3. Open Swagger UI using your browser to check functionality:
   - Access it at `http://localhost:8081/swagger-ui/`.

<p align="center">
  <img src="/Swagger.png" alt="swagger_png"/>
</p>


## Postman Setup

[![Run in Postman](https://run.pstmn.io/button.svg)](https://elements.getpostman.com/redirect?entityId=33969486-26d9ac4d-b809-4df0-a906-8737c653db0e&entityType=collection)

## Contributing

If you would like to contribute to this project:
1. Fork the repository.
2. Create a new branch for your feature or fix.
3. Make your changes and commit them.
4. Submit a pull request.

## Contact

For questions or suggestions

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?logo=linkedin&logoColor=white&style=flat-square)](https://www.linkedin.com/in/yevhen-shumeiko-5a153b26a/)

