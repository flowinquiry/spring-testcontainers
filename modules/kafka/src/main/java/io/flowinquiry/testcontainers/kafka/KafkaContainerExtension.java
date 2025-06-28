package io.flowinquiry.testcontainers.kafka;

import static io.flowinquiry.testcontainers.ContainerType.KAFKA;
import static io.flowinquiry.testcontainers.ServiceLoaderContainerFactory.getProvider;

import io.flowinquiry.testcontainers.ContainerLifecycleExtension;
import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import org.testcontainers.containers.GenericContainer;

/**
 * JUnit Jupiter extension that manages the lifecycle of Kafka containers for integration tests.
 * This extension works in conjunction with the {@link EnableKafkaContainer} annotation to
 * automatically start and stop Kafka containers before and after test execution.
 *
 * <p>The extension is automatically registered when a test class is annotated with {@link
 * EnableKafkaContainer}. It handles container initialization, startup, and shutdown, making the
 * Kafka instance available during test execution.
 */
public class KafkaContainerExtension extends ContainerLifecycleExtension<EnableKafkaContainer> {

  /**
   * Resolves the {@link EnableKafkaContainer} annotation from the test class.
   *
   * @param testClass the test class to examine for the annotation
   * @return the resolved annotation instance, or null if not present
   */
  @Override
  protected EnableKafkaContainer getResolvedAnnotation(Class<?> testClass) {
    return testClass.getAnnotation(EnableKafkaContainer.class);
  }

  /**
   * Initializes a Kafka container provider based on the configuration in the annotation. This
   * method locates the appropriate provider for Kafka containers and initializes it with the
   * settings from the annotation.
   *
   * @param annotation the annotation containing Kafka container configuration
   * @return a configured Kafka container provider
   */
  @Override
  protected SpringAwareContainerProvider<EnableKafkaContainer, ? extends GenericContainer<?>>
      initProvider(EnableKafkaContainer annotation) {
    return getProvider(
        annotation,
        p -> p.getContainerType() == KAFKA,
        (prov, ann) -> prov.initContainerInstance(ann));
  }
}
