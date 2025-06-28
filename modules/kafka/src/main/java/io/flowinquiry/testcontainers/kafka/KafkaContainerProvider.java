package io.flowinquiry.testcontainers.kafka;

import static io.flowinquiry.testcontainers.ContainerType.KAFKA;

import io.flowinquiry.testcontainers.ContainerType;
import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import java.util.Properties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.kafka.KafkaContainer;

/**
 * Provider for Kafka containers that integrates with Spring test environments. This class manages
 * the creation, configuration, and lifecycle of Kafka containers for integration testing with
 * Spring applications.
 *
 * <p>The provider is automatically discovered through Java's ServiceLoader mechanism and is used by
 * the {@link KafkaContainerExtension} when a test class is annotated with {@link
 * EnableKafkaContainer}.
 */
public class KafkaContainerProvider
    extends SpringAwareContainerProvider<EnableKafkaContainer, KafkaContainer> {

  /**
   * Creates and configures a Kafka container instance. The container is configured with the Docker
   * image and version specified in the {@link EnableKafkaContainer} annotation.
   *
   * @return a configured Kafka container instance
   */
  @Override
  protected KafkaContainer createContainer() {
    return new KafkaContainer(dockerImage + ":" + version);
  }

  /**
   * Returns the type of container managed by this provider.
   *
   * @return the KAFKA container type
   */
  @Override
  public ContainerType getContainerType() {
    return KAFKA;
  }

  /**
   * Applies Kafka-specific configuration to the Spring environment. This method sets the bootstrap
   * servers property in the Spring environment, allowing Spring Kafka clients to automatically
   * connect to the test container.
   *
   * @param environment the Spring environment to configure
   */
  @Override
  public void applyTo(ConfigurableEnvironment environment) {
    Properties props = new Properties();
    props.put("spring.kafka.bootstrap-servers", container.getBootstrapServers());

    environment
        .getPropertySources()
        .addFirst(new PropertiesPropertySource("testcontainers.kafka", props));
  }
}
