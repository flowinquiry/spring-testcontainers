package io.flowinquiry.testcontainers.kafka;

/**
 * Annotation that enables and configures a Kafka container for integration testing. When applied to
 * a test class, this annotation triggers the creation and management of a Kafka container instance
 * that can be used during tests.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @EnableKafkaContainer
 * public class KafkaIntegrationTest {
 *     // Test methods that require Kafka
 * }
 * }</pre>
 */
public @interface EnableKafkaContainer {

  /**
   * Specifies the version of the Kafka container image to use.
   *
   * @return the Kafka version, defaults to "3.9.1"
   */
  String version() default "3.9.1";

  /**
   * Specifies the Docker image name for the Kafka container.
   *
   * @return the Docker image name, defaults to "apache/kafka"
   */
  String dockerImage() default "apache/kafka";
}
