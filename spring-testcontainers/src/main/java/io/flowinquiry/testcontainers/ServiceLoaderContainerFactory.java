package io.flowinquiry.testcontainers;

import java.lang.annotation.Annotation;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Factory class that uses Java's ServiceLoader mechanism to dynamically discover and initialize
 * container providers.
 *
 * <p>This class provides a centralized way to load {@link SpringAwareContainerProvider}
 * implementations that are registered via the Java ServiceLoader SPI. It allows for dynamic
 * discovery of container providers without hard-coding dependencies to specific implementations.
 *
 * <p>The factory is typically used by container extensions to find an appropriate provider based on
 * container type and annotation configuration.
 */
public class ServiceLoaderContainerFactory {

  /**
   * Discovers and initializes a container provider that matches the specified filter criteria.
   *
   * <p>This method uses Java's ServiceLoader mechanism to find all available {@link
   * SpringAwareContainerProvider} implementations, filters them according to the provided
   * predicate, and initializes the first matching provider with the given annotation.
   *
   * @param <A> the annotation type that configures the container
   * @param annotation the annotation instance containing container configuration
   * @param filter a predicate to select the appropriate provider (e.g., by container type)
   * @param initializer a function that initializes the provider with the annotation configuration
   * @return the initialized container provider
   * @throws IllegalStateException if no matching provider is found
   */
  public static <A extends Annotation> SpringAwareContainerProvider getProvider(
      A annotation,
      Predicate<SpringAwareContainerProvider> filter,
      BiConsumer<SpringAwareContainerProvider, A> initializer) {

    SpringAwareContainerProvider<?, ?> provider =
        ServiceLoader.load(SpringAwareContainerProvider.class).stream()
            .map(ServiceLoader.Provider::get)
            .filter(filter)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No matching provider found."));

    initializer.accept(provider, annotation);
    return provider;
  }
}
