package io.flowinquiry.testcontainers.examples.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/** Configuration class for Kafka. Defines the Kafka topic and other Kafka-related beans. */
@Configuration
public class KafkaConfig {

  public static final String TOPIC_NAME = "test-topic";

  /**
   * Creates a Kafka topic.
   *
   * @return the Kafka topic
   */
  @Bean
  public NewTopic testTopic() {
    return TopicBuilder.name(TOPIC_NAME).partitions(1).replicas(1).build();
  }
}
