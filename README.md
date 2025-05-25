# TestContainers-Ext

A Java library that simplifies integration testing with databases by automating TestContainers setup and lifecycle management.

## Overview

TestContainers-Ext addresses a common pain point in integration testing: the repetitive boilerplate code needed to set up, manage, and tear down test containers. Instead of manually initializing TestContainer instances in every test class and handling their lifecycle, this library provides a simple annotation-based approach that handles all the container management for you.

## Comparison: TestContainers vs TestContainers-Ext

The following table demonstrates the difference between using TestContainers directly and using TestContainers-Ext:

| Feature | Using TestContainers Directly | Using TestContainers-Ext |
|---------|------------------------------|--------------------------|
| **Setup** | Requires manual container initialization and configuration | Simple annotation-based setup |
| **Code Example** | See Example 1 below | See Example 2 below |
| **Lines of Code** | ~25 lines | ~5 lines |
| **Container Lifecycle** | Manual start/stop | Automatic management |
| **Configuration** | Manual configuration of database properties | Handled automatically |
| **Maintenance** | More code to maintain | Minimal code |
| **Focus** | Split between infrastructure and test logic | Primarily on test logic |
| **Learning Curve** | Need to understand TestContainers API details | Simple annotation-based API |

### Example 1: Using TestContainers Directly

```java
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTest {

    private static PostgreSQLContainer<?> postgres;

    @BeforeAll
    public static void startContainer() {
        postgres = new PostgreSQLContainer<>("postgres:13.2")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
        postgres.start();

        // Configure your application to use the container
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @Test
    public void testDatabaseConnection() {
        // Your test code here
    }

    @AfterAll
    public static void stopContainer() {
        if (postgres != null) {
            postgres.stop();
        }
    }
}
```

### Example 2: Using TestContainers-Ext

```java
import io.flowinquiry.testcontainers.jdbc.EnableJdbcContainer;
import io.flowinquiry.testcontainers.jdbc.Rdbms;
import org.junit.jupiter.api.Test;

@EnableJdbcContainer(rdbms = Rdbms.POSTGRES, version = "13.2")
public class PostgresTest {

    @Test
    public void testDatabaseConnection() {
        // Your test code here
        // Container is automatically started and stopped
    }
}
```

## Features

- **Simple Annotation API**: Just add `@EnableJdbcContainer` to your test class
- **Automatic Container Management**: No need to manually start/stop containers
- **JUnit 5 Integration**: Works seamlessly with JUnit 5 tests
- **Multiple Database Support**: Currently supports PostgreSQL and MySQL
- **Extensible Architecture**: Easy to add support for additional databases

## Requirements

- Java 8 or higher
- JUnit 5
- Docker (for running the containers)

## Installation

Add the following dependencies to your build file:

### Gradle (Kotlin DSL)

```kotlin
// Core library
implementation("org.hng:testcontainers-ext-core:1.0.0")

// Database-specific modules (add only what you need)
implementation("org.hng:testcontainers-ext-mysql:1.0.0")  // For MySQL support
implementation("org.hng:testcontainers-ext-postgres:1.0.0")  // For PostgreSQL support
```

### Maven

```xml
<!-- Core library -->
<dependency>
    <groupId>org.hng</groupId>
    <artifactId>testcontainers-ext-core</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>

<!-- Database-specific modules (add only what you need) -->
<dependency>
    <groupId>org.hng</groupId>
    <artifactId>testcontainers-ext-mysql</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.hng</groupId>
    <artifactId>testcontainers-ext-postgres</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

## Usage

### Basic Example

```java
import io.flowinquiry.testcontainers.jdbc.EnableJdbcContainer;
import io.flowinquiry.testcontainers.jdbc.Rdbms;
import org.junit.jupiter.api.Test;

@EnableJdbcContainer(rdbms = Rdbms.POSTGRES, version = "13.2")
public class MyDatabaseTest {

    @Test
    public void testDatabaseConnection() {
        // Your test code here
        // The database container is automatically started before the test
        // and stopped after the test
    }
}
```

### MySQL Example

```java
import io.flowinquiry.testcontainers.jdbc.EnableJdbcContainer;
import io.flowinquiry.testcontainers.jdbc.Rdbms;
import org.junit.jupiter.api.Test;

@EnableJdbcContainer(rdbms = Rdbms.MYSQL, version = "8.0")
public class MySqlTest {

    @Test
    public void testWithMySql() {
        // Your MySQL-specific test code here
    }
}
```

## Supported Databases

Currently, the following databases are supported:

- PostgreSQL
- MySQL

## How It Works

TestContainers-Ext uses JUnit 5's extension mechanism to automatically manage the lifecycle of database containers. When a test class is annotated with `@EnableJdbcContainer`, the library:

1. Identifies the requested database type and version
2. Loads the appropriate container factory using Java's Service Provider Interface (SPI)
3. Creates and starts the container before tests run
4. Makes connection details available to tests
5. Stops and removes the container after tests complete

## Contributing

Contributions are welcome! If you'd like to add support for additional databases or improve the library, please:

1. Fork the repository
2. Create a feature branch
3. Install the Git hooks to ensure code formatting (see below)
4. Add your changes
5. Submit a pull request

### Git Hooks

This project uses Git hooks to ensure code quality. We provide a pre-commit hook that automatically formats your code using the Spotless Gradle plugin before each commit.

To install the Git hooks:

```bash
# Make the installation script executable (if not already)
chmod +x git-hooks/install-hooks.sh

# Run the installation script
./git-hooks/install-hooks.sh
```

Once installed, the pre-commit hook will automatically run `./gradlew spotlessApply` before each commit, ensuring that all code is properly formatted.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
