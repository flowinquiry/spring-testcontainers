package io.flowinquiry.testcontainers;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A registry for managing container providers associated with test classes.
 *
 * <p>This utility class maintains a thread-safe mapping between test classes and their
 * corresponding container providers. It allows for container reuse across test executions and
 * ensures proper container lifecycle management.
 *
 * <p>The registry is used by {@link ContainerLifecycleExtension} to track active containers and
 * prevent duplicate container creation for the same test class.
 */
public final class ContainerRegistry {

  /** Thread-safe map storing the association between test classes and their container providers. */
  private static final Map<Class<?>, SpringAwareContainerProvider<? extends Annotation, ?>>
      providers = new ConcurrentHashMap<>();

  /** Private constructor to prevent instantiation of this utility class. */
  private ContainerRegistry() {}

  /**
   * Associates a container provider with a test class in the registry.
   *
   * @param testClass the test class to associate with the provider
   * @param provider the container provider to register
   */
  public static void set(Class<?> testClass, SpringAwareContainerProvider<?, ?> provider) {
    providers.put(testClass, provider);
  }

  /**
   * Retrieves the container provider associated with a test class.
   *
   * @param testClass the test class whose provider should be retrieved
   * @return the container provider associated with the test class, or null if none exists
   */
  public static SpringAwareContainerProvider<? extends Annotation, ?> get(Class<?> testClass) {
    return providers.get(testClass);
  }

  /**
   * Checks if a container provider is registered for a test class.
   *
   * @param testClass the test class to check
   * @return true if a container provider is registered for the test class, false otherwise
   */
  public static boolean contains(Class<?> testClass) {
    return providers.containsKey(testClass);
  }

  /**
   * Removes the container provider associated with a test class from the registry.
   *
   * @param testClass the test class whose provider should be removed
   */
  public static void clear(Class<?> testClass) {
    providers.remove(testClass);
  }

  /**
   * Removes all container providers from the registry. This method is typically used during cleanup
   * to ensure no containers remain registered.
   */
  public static void clearAll() {
    providers.clear();
  }
}
