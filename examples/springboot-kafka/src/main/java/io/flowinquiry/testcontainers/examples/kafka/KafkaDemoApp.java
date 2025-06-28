package io.flowinquiry.testcontainers.examples.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Spring Boot application for the Kafka demo. This application demonstrates how to use Kafka with
 * Spring Boot and Testcontainers.
 */
@SpringBootApplication
@EnableKafka
public class KafkaDemoApp {

  public static void main(String[] args) {
    SpringApplication.run(KafkaDemoApp.class, args);
  }
}
