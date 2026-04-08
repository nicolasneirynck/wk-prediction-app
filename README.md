# WK Prognose

WK Prognose is a Spring Boot web application for managing World Cup predictions. Users can view matches, enter score predictions, create teams, and join prediction groups.

## Tech Stack

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL
- Maven

## Features

- View match information
- Add and edit matches
- Enter score predictions
- Create teams
- Join existing teams
- View team details and members

## Project Structure

```text
src/main/java      Application logic, controllers, services, models, repositories
src/main/resources Templates, static assets, configuration, seed data
src/test/java      Tests
```

## Getting Started

### Prerequisites

- Java 21
- Maven or the included Maven Wrapper
- MySQL

### Run the Application

1. Configure your database connection with environment variables:

```bash
export DB_URL="jdbc:mysql://localhost:3306/wkprognose?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="your-password"
```

2. Start the application:

```bash
./mvnw spring-boot:run
```

3. Open your browser at `http://localhost:8080`.

## Database

The application is configured for MySQL and includes SQL seed data through `src/main/resources/data.sql`.
Database credentials are read from `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` so sensitive values do not need to be committed.

## Notes

- `target/` and IDE files are already ignored through `.gitignore`.
- `src/main/resources/application.properties` is set up to use environment variables instead of hardcoded secrets.

## License

This project is currently unlicensed.
