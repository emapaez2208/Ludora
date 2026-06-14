# LUDORA API
Ludora is a backend platform tailored for a video game marketplace. It enables developers to publish their titles for sale, while customers can purchase games and contribute by writing reviews. The platform includes a loyalty system where users earn points with each purchase, which can later be redeemed for discounts.
This project was developed as a final assignment for the "Programación III" and "Metodología de Sistemas I" courses at the Universidad Tecnológica Nacional (UTN).

## 🚀 Features
* User Authentication & Authorization: Secure registration and login system.
* Game Management: Developers can publish, update, and manage their game titles. 
* Marketplace Functionality: Clients can browse, purchase, and own video games. 
* Mercado Pago Integration: Secure payment processing for all transactions. 
* Review System: Clients can write and manage reviews for purchased games. 
* Loyalty & Rewards Program: Automated point-earning system for every purchase. 
* Discount Redemptions: Clients can redeem accumulated points for discounts on future purchases. 
* Role-Based Access Control (RBAC): Distinct permissions for Developers, Clients, and Administrators. 
* RESTful Architecture: Fully documented API using OpenAPI 3.0 (via OpenAPI-GUI).

## 🛠 Tech Stack

### Core
* Language: Java 
* Database: MySQL

### Spring Framework
* Spring Boot 
* Spring Security 
* Spring Data JPA 
* Spring Web 
* Spring Validation

### Tools & Libraries
* ORM: Hibernate 
* Authentication: JWT (JSON Web Tokens)
* Mapping: MapStruct 
* Documentation: OpenAPI (via OpenAPI-GUI)
* Productivity: Lombok 
* Database connectivity: JDBC

### Integrations
* Payments: Mercado Pago API

## 🏗 Architecture

### Three-Tier Architecture
* Controller Layer: Manages incoming HTTP requests, performs input validation, and returns structured API responses. 
* Service Layer: Encapsulates core business logic, transaction management, and service coordination. 
* Repository Layer: Handles data persistence and complex queries using Spring Data JPA.

### Data Flow & Processing
* Entity-DTO Pattern: Decouples the internal database schema from the external API data structure. 
* MapStruct: Efficient, compile-time object mapping between entities and DTOs. 
* Global Exception Handler: Centralized error management using Spring’s @ControllerAdvice and @ExceptionHandler. 
* Data Validation: Strict data integrity enforcement using Spring Validation constraints.

### Database Access
* Spring Data JPA: Data access abstraction using standard repository methods. 
* JPA Specifications: Implementation of dynamic and flexible queries, allowing for complex filtering criteria at runtime. 
* Hibernate: Serves as the ORM provider to automatically manage object-relational mapping and synchronization with MySQL.

## 📖 API Documentation
* Endpoint Descriptions: Detailed information for every available API route and operation.
* Request/Response Examples: Concrete JSON samples for all endpoints to facilitate integration. 
* Authentication Requirements: Clear documentation on protected routes and JWT-based security requirements. 
* Schema Definitions: Detailed data structures for all models used in the API.

## 🛡 Security

### Authentication
* Token-Based System: Secure authentication using JWT (JSON Web Tokens) for stateless session management. 
* Configurable Expiration: Implementation of secure token life cycles to balance security and user experience. 
* Password Security: Robust credential protection using BCrypt hashing for sensitive data storage.

### Authorization
* Role-Based Access Control: Granular permission management across different user profiles (Clients, Developers, Administrators). 
* Role-Specific Privileges: Clear separation of concerns, ensuring admin-level features are restricted to authorized personnel. 
* Protected Endpoints: Strict server-side validation, ensuring resources are accessible only to users with the required authorization level.

## 💳 Payment Integration
* Mercado Pago API: Seamless integration with Mercado Pago for secure, real-time payment processing. 
* Transaction Lifecycle: Automated management of payment states (Pending, Approved, Rejected) ensuring data consistency. 
* Webhook Handling: Secure reception of asynchronous notifications from Mercado Pago to update order status automatically. 
* Checkout Experience: Implementation of a standardized, secure flow to facilitate user transactions.

## ⚙️ Getting Started

### Prerequisites
* Java Development Kit (JDK)
* Maven
* MySQL

### Usage
1. Clone the repository:

    git clone https://github.com/emapaez2208/Ludora.git


2. Navigate to the project folder:
    
    cd Ludora


3. Configure environment variables:
* DB_URL, DB_USER, DB_PASSWORD
* JWT_SECRET, JWT_EXPIRATION
* MERCADOPAGO_ACCESS, APP_BASE_URL

4. Run the application:

    mvn spring-boot:run

## 🤝 Contributions
This project is currently under active development. While we are not accepting external contributions at this time.

## ⚖️ License
This project is not currently released under a public license. Its use is strictly limited to educational or private purposes.

