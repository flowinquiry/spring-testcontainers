package io.flowinquiry.testcontainers;

import java.util.List;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * A factory for creating context customizers that integrate TestContainers with Spring test
 * contexts.
 *
 * <p>This class implements Spring's {@link ContextCustomizerFactory} interface to provide
 * integration between TestContainers and Spring's test framework. It creates customizers that
 * retrieve container providers from the {@link ContainerRegistry} and apply their configuration to
 * the Spring test context environment.
 *
 * <p>The factory works in conjunction with {@link ContainerLifecycleExtension} which manages the
 * container lifecycle and registers container providers in the {@link ContainerRegistry}.
 */
public class ContainerContextCustomizerFactory implements ContextCustomizerFactory {

  /**
   * Creates a context customizer for the specified test class.
   *
   * <p>The created customizer retrieves the {@link SpringAwareContainerProvider} associated with
   * the test class from the {@link ContainerRegistry} and applies its configuration to the Spring
   * environment. This allows test classes to access container-specific properties (like connection
   * URLs, ports, etc.) through Spring's environment.
   *
   * @param testClass the test class for which to create a context customizer
   * @param configAttributes the context configuration attributes
   * @return a context customizer that applies container configuration to the Spring environment
   */
  @Override
  public ContextCustomizer createContextCustomizer(
      Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
    return (context, mergedConfig) -> {
      SpringAwareContainerProvider<?, ?> provider = ContainerRegistry.get(testClass);
      provider.applyTo(context.getEnvironment());
    };
  }
}
