# TestContainers PostgreSQL Example

This module demonstrates how to use the TestContainers-Ext library with PostgreSQL.

## Overview

This example shows two ways to use TestContainers-Ext with PostgreSQL:

1. **Annotation-based approach**: Using the `@EnableJdbcContainer` annotation in tests
2. **Programmatic approach**: Directly creating and managing container instances

## Structure

- `src/main/java`: Contains a demo application showing programmatic usage
- `src/test/java`: Contains test examples showing annotation-based usage

## Annotation-based Usage

The annotation-based approach is the simplest way to use TestContainers-Ext. Just add the `@EnableJdbcContainer` annotation to your test class:

```java
@EnableJdbcContainer(rdbms = Rdbms.POSTGRES, version = "13.2")
public class PostgresqlExampleTest {

    @Test
    public void testWithPostgresql() {
        // The PostgreSQL container is automatically started before this test
        // and stopped after the test completes.
        
        System.out.println("PostgreSQL container is running!");
        
        // You can access connection details using:
        // - System.getProperty("db.url")
        // - System.getProperty("db.username")
        // - System.getProperty("db.password")
    }
}
```

## Programmatic Usage

For more control, you can create and manage containers programmatically:

```java
// Create a PostgreSQL container factory
PostgresSqlContainerFactory factory = new PostgresSqlContainerFactory();

// Create a PostgreSQL container with version 13.2
JdbcDatabaseContainer<?> container = factory.createContainer("13.2");

// Start the container
container.start();

// Use the container
System.out.println("JDBC URL: " + container.getJdbcUrl());
System.out.println("Username: " + container.getUsername());
System.out.println("Password: " + container.getPassword());

// Stop the container when done
container.stop();
```

## Running the Examples

### Running the Tests

```bash
./gradlew :examples:testcontainers-postgresql:test
```

### Running the Demo Application

```bash
./gradlew :examples:testcontainers-postgresql:run
```

## Requirements

- Java 8 or higher
- Docker (for running the containers)