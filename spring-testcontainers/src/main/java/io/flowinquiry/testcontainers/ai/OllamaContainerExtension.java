package io.flowinquiry.testcontainers.ai;

import static io.flowinquiry.testcontainers.ContainerType.OLLAMA;
import static io.flowinquiry.testcontainers.ServiceLoaderContainerFactory.getProvider;

import io.flowinquiry.testcontainers.ContainerLifecycleExtension;
import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import org.testcontainers.containers.GenericContainer;

/**
 * JUnit Jupiter extension that manages the lifecycle of Ollama containers for AI testing.
 *
 * <p>This extension works with the {@link EnableOllamaContainer} annotation to automatically start
 * and stop Ollama containers for tests. It handles container initialization based on the
 * configuration provided in the annotation.
 *
 * <p>Usage example:
 *
 * <pre>
 * &#64;EnableOllamaContainer(dockerImage = "ollama/ollama", version = "latest")
 * public class OllamaTest {
 *     // Test methods
 * }
 * </pre>
 */
public class OllamaContainerExtension extends ContainerLifecycleExtension<EnableOllamaContainer> {
  /**
   * Resolves the {@link EnableOllamaContainer} annotation from the test class.
   *
   * @param testClass the test class to examine for the annotation
   * @return the resolved annotation instance
   */
  @Override
  protected EnableOllamaContainer getResolvedAnnotation(Class<?> testClass) {
    return testClass.getAnnotation(EnableOllamaContainer.class);
  }

  /**
   * Initializes an Ollama container provider based on the configuration in the annotation.
   *
   * @param annotation the {@link EnableOllamaContainer} annotation containing container
   *     configuration
   * @return a configured Ollama container provider
   */
  @Override
  protected SpringAwareContainerProvider<EnableOllamaContainer, ? extends GenericContainer<?>>
      initProvider(EnableOllamaContainer annotation) {
    return getProvider(
        annotation,
        p -> p.getContainerType() == OLLAMA,
        (prov, ann) -> prov.initContainerInstance(ann));
  }
}
