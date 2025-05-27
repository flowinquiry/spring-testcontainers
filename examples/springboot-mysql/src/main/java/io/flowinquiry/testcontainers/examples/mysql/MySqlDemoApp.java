package io.flowinquiry.testcontainers.examples.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Spring Boot application for the MySQL demo. */
@SpringBootApplication
@EntityScan("io.flowinquiry.testcontainers.examples.mysql.entity")
@EnableJpaRepositories("io.flowinquiry.testcontainers.examples.mysql.repository")
public class MySqlDemoApp {

  public static void main(String[] args) {
    SpringApplication.run(MySqlDemoApp.class, args);
  }
}
