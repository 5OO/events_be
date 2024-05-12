# Event Registration System

## Overview

This document outlines the setup and architecture of the Event Registration System, which is designed to manage future and past events, register participants (individuals and companies), and handle event-specific information.

### Technologies Used

- **Programming Language:** Java
- **Framework:** Spring Boot
- **Database:** H2 Database
- **Frontend:** Vue.js 3 with Bootstrap for UI
- **Development Environment:** IntelliJ IDEA Ultimate
- **Version Control:** Git with repository hosted on [GitHub](https://github.com/5OO/events_be)

## Installation Guide

### Prerequisites

- Java JDK 22 
- Node.js and npm (for the frontend)
- IntelliJ IDEA Ultimate or Community Edition
- Git

### Backend Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/5OO/events_be.git
   cd events_be
   ```

2. **Open the Project:**
    - Open IntelliJ IDEA.
    - Select "Open" and navigate to the cloned directory.
    - IntelliJ should automatically recognize the project as a Spring Boot application.

3. **Database Configuration:**
    - The application uses a file-based H2 database. 
    - The database schema and initial data are automatically applied from src/main/resources/db/schema_file-db.sql and src/main/resources/db/data_file-db.sql.
    - Ensure the database file path in application.properties is correctly set for your system:

   ```bash
   spring.datasource.url=jdbc:h2:file:/<your-path>/events/src/main/resources/db/file_db;AUTO_SERVER=true
   ```

4. **Run the Application:**
    - Locate `EventsApplication.java` in the IDE and run it as a Spring Boot application.
    - The application should be accessible on `http://localhost:8080`.

### Accessing the Database Console

The H2 console is enabled and can be accessed at http://localhost:8080/h2. 
Use the JDBC URL jdbc:h2:file:/<your-path>/events/src/main/resources/db/file_db to connect to the database via the console.

## Database Configuration
### File-based H2 Database

The system utilizes a file-based H2 database which is embedded within the Spring Boot application. This setup allows for a quick start without the need for external database servers.

    JDBC URL: jdbc:h2:file:/<your-path>/events/src/main/resources/db/file_db
    Username: sa (default)
    Password: (empty by default)

The schema and initial data are automatically loaded from the following files, which should be reviewed and modified according to project requirements:

    Schema File: src/main/resources/db/schema_file-db.sql
    Data File: src/main/resources/db/data_file-db.sql

### H2 Console

The H2 console is available for database management and query execution during development:

    URL: http://localhost:8080/h2
    Ensure the JDBC URL matches the path configured in application.properties.

### Frontend Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/5OO/events_front.git
   cd events_front
   ```

2. **Install Dependencies:**
   ```bash
   npm install
   ```

3. **Run the Frontend:**
   ```bash
   npm run dev
   ```
    - The frontend should now be accessible on `http://localhost:5173`.
    - Vue DevTools: Open http://localhost:5173/__devtools__/ as a separate window

## Architecture Overview

The Event Registration System adopts a layered architecture with clear separation of concerns, facilitating maintainability and scalability.

### Key Components

- **Controller Layer:** Handles HTTP requests, orchestrating the flow of data between the frontend and the service layer.
- **Service Layer:** Contains business logic for managing events, participants, and other core functionalities. It ensures the application's business rules and validation are correctly executed.
- **Repository Layer:** Interfaces the database, handling all data persistence operations using Spring Data JPA.
- **Model Layer:** Represents the application's domain model including entities such as `Event`, `Individual`, and `Company`.
- **DTO (Data Transfer Objects):** Used to transfer data between the client and the server without exposing internal details of the database entities.
- **Validation Layer:** Ensures that incoming data adheres to defined constraints before processing.
- **Exception Handling:** Centralized exception handling mechanism to manage error scenarios gracefully.

### Dependency Management

Spring Boot's Inversion of Control (IoC) container and dependency injection features are extensively used to decouple the application components, making them easy to manage and test.

## Testing

Automated tests are written using JUnit and cover the service layer extensively to ensure that business logic is correctly implemented. Integration tests for controllers can be added to ensure the REST API behaves as expected under different scenarios.

## Database Diagram

The database schema is included in the project repository and shows how the `Events`, `Individuals`, and `Companies` tables are structured and interrelated.

![Untitled(2)](https://github.com/5OO/events_be/assets/27925052/1c45dd51-66c4-4dd9-ba03-3345e08532e8)

## Version Control

The project's progress and versioning are managed through Git, with the repository hosted on GitHub. This setup ensures that changes are tracked and documented, facilitating collaborative development and code reviews.

---

