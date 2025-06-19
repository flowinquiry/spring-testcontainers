# Spring-TestContainers

[![Build Status](https://github.com/flowinquiry/spring-testcontainers/actions/workflows/gradle.yml/badge.svg)](https://github.com/flowinquiry/spring-testcontainers/actions/workflows/gradle.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.flowinquiry.testcontainers/spring-testcontainers?label=Maven%20Central)](https://search.maven.org/artifact/io.flowinquiry.testcontainers/spring-testcontainers)

Spring-TestContainers is a Java library that makes it easier to write integration tests with Testcontainers, especially when you're using Spring or Spring Boot. It handles the setup and lifecycle of containers for you, so you can focus on testing—not boilerplate.

We originally built this for FlowInquiry to make our own testing smoother. It worked so well, we decided to share it as a standalone library so other teams can take advantage of it too.

## Why Spring-TestContainers?

Setting up Testcontainers in Spring-based projects often involves boilerplate code and manual configuration. Spring-TestContainers eliminates that overhead with a clean, annotation-driven approach that:

* Reduces setup to a single annotation (@EnablePostgreSQL, @EnableMySQL, etc.)

* Automatically manages container lifecycle

* Auto-configures Spring environment with database connection details

### Supported Containers

Spring-TestContainers provides out-of-the-box support for the following containers. You can enable each one via a dedicated annotation in your test classes:

Spring-TestContainers provides out-of-the-box support for the following containers. You can enable each one via a dedicated annotation in your test classes:

| Container       | Annotation                        | Example Usage                                        | Notes                          |
|----------------|-----------------------------------|-----------------------------------------------------|--------------------------------|
| **PostgreSQL** | `@EnablePostgresContainer`        | `@EnablePostgresContainer(version = "15")`          | Uses `PostgreSQLContainer`     |
| **MySQL**      | `@EnableMySQLContainer`           | `@EnableMySQLContainer(version = "8")`              | Uses `MySQLContainer`          |
| **Ollama (AI)**| `@EnableOllamaContainer`          | `@EnableOllamaContainer(model = "llama2")`          | Starts Ollama with auto-pull   |


## Comparison: TestContainers with Spring vs Spring-TestContainers

The following table demonstrates the difference between using TestContainers with Spring directly and using Spring-TestContainers:

| Feature | Plain TestContainers with Spring                                  | Spring-TestContainers |
|---------|-------------------------------------------------------------------|------------------------|
| **Setup** | Manual container and Spring config	 | Single annotation |
| **Boilerplate** | ~50 lines                                                         | ~5 lines |
| **Container Lifecycle** | Manual or via JUnit extension                       | Automatic |
| **Spring Environment** | Manually wired                              | Auto-configured |
| **Maintenance** | More code to maintain                                             | Minimal code |
| **Focus** | 	Infrastructure + test logic                       | Pure test logic |
| **Learning Curve** | High           | Low |

### Example 1: Traditional Testcontainers Setup

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostgresSpringTest.TestConfig.class)
@Testcontainers
class PostgresSpringTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() {
        // Your test code here using the dataSource
        // The container is automatically started by the @Testcontainers annotation
        // and the dataSource is configured to connect to it
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(postgres.getJdbcUrl());
            dataSource.setUsername(postgres.getUsername());
            dataSource.setPassword(postgres.getPassword());
            return dataSource;
        }
    }
}
```

### Example 2: Using Spring-TestContainers

```java
import io.flowinquiry.testcontainers.jdbc.EnablePostgreSQL;
import org.junit.jupiter.api.Test;

@ExtendWith(SpringExtension.class)
@EnablePostgreSQL
class PostgresTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() {
        // Your test code here
        // Container is automatically started and stopped
    }
}
```

## Features

* 🧩 Simple annotation API: @EnablePostgreSQL, @EnableMySQL, @EnableOllmaContainer

* 🔄 Automatic container lifecycle management

* 🧪 JUnit 5 integration

* 🌱 Full Spring and Spring Boot support

* 🧰 Extensible architecture for other databases

* 🐘 Out-of-the-box support for PostgreSQL and MySQL

## Requirements

- Java 17 or higher
- JUnit 5
- Docker (for running the containers)
- Spring Framework 6.x (for Spring integration)
- Spring Boot 3.x (for Spring Boot integration)

## Installation

Add the core library along with the database module(s) you plan to use. Each database has its own module, which includes everything needed to support that specific container and configuration.

### Gradle (Kotlin DSL)

```kotlin
// Add one or more of the following database modules
testImplementation("io.flowinquiry.testcontainers:postgresql:<!-- Replace with the latest version -->") // PostgreSQL support
testImplementation("io.flowinquiry.testcontainers:mysql:<!-- Replace with the latest version -->")      // MySQL support
testImplementation("io.flowinquiry.testcontainers:ollama:<!-- Replace with the latest version -->")     // Ollama support 
```

### Maven

```xml

<!-- Add one or more of the following database modules -->

<!-- Add this dependency to test Postgres database -->
<dependency>
    <groupId>io.flowinquiry.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version><!-- Replace with the latest version --></version>
    <scope>test</scope>
</dependency>
<dependency>

<!-- Add this dependency to test MySQL database -->
<dependency>
    <groupId>io.flowinquiry.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <version><!-- Replace with the latest version --></version>
    <scope>test</scope>
</dependency>

<!-- Add this dependency to test Ollama container -->
<dependency>
    <groupId>io.flowinquiry.testcontainers</groupId>
    <artifactId>ollama</artifactId>
    <version><!-- Replace with the latest version --></version>
    <scope>test</scope>
</dependency>
```

> 📝 As more databases are supported, simply add the corresponding module and TestContainers dependency.

## Usage

### Spring Framework Integration

```java
// Spring Framework (without Spring Boot)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = YourSpringConfig.class)
@EnablePostgreSQL
class SpringPostgresTest {
    @Autowired
    private YourRepository repository;

    @Test
    void testWithSpring() {
        // Spring environment is auto-configured with container details
    }
}

// Spring Boot
@SpringBootTest
@EnablePostgreSQL
class SpringBootPostgresTest {
    @Autowired
    private YourRepository repository;

    @Test
    void testWithSpringBoot() {
        // Spring Boot is auto-configured with container details
    }
}
```

## Supported Test Containers

Currently, the following containers are supported:

- PostgreSQL
- MySQL
- Ollama

## Examples

The project includes several example modules demonstrating how to use Spring-TestContainers:

### [springboot-postgresql](examples/springboot-postgresql) / [springboot-mysql](examples/springboot-mysql)

* Spring Boot applications using JPA with PostgreSQL and MySQL

* Show how to integrate containerized databases with minimal configuration


### [spring-postgresql](examples/spring-postgresql)

* Spring Framework (no Boot) setup with JPA and PostgreSQL

* Manual configuration for container-based testing

These examples provide a good starting point for integrating Spring-TestContainers into your own projects.

### [springboot-ollama](examples/springboot-ollama)

* Spring Boot applications using Spring AI and Ollama

* Show how to test AI prompts with Ollama container

## Contributing

Contributions are welcome! If you'd like to add support for additional databases or improve the library, please:

1. Fork the repository
2. Create a feature branch
3. Add your changes
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
