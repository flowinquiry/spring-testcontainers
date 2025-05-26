package io.flowinquiry.testcontainers.examples.postgresql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring Boot application for the PostgreSQL demo.
 */
@SpringBootApplication
@EntityScan("io.flowinquiry.testcontainers.examples.postgresql.entity")
@EnableJpaRepositories("io.flowinquiry.testcontainers.examples.postgresql.repository")
public class PostgresqlDemoApp {

    public static void main(String[] args) {
        SpringApplication.run(PostgresqlDemoApp.class, args);
    }
}