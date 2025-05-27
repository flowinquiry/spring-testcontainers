package io.flowinquiry.testcontainers.jdbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing JDBC container providers associated with test classes.
 *
 * <p>This class serves as a central registry that maintains a mapping between test classes and
 * their corresponding {@link JdbcContainerProvider} instances. It ensures that each test class has
 * access to its own database container provider, and prevents duplicate container creation when a
 * test class is used in multiple contexts.
 *
 * <p>The registry is primarily used by the {@link JdbcExtension} to:
 *
 * <ol>
 *   <li>Check if a provider already exists for a test class before creating a new one
 *   <li>Store newly created providers for future reference
 *   <li>Retrieve providers when needed for container management
 *   <li>Clean up providers after tests complete
 * </ol>
 *
 * <p>This registry is thread-safe and uses a {@link ConcurrentHashMap} to store the mappings,
 * allowing it to be safely used in concurrent test execution environments.
 *
 * <p>Example usage within the framework:
 *
 * <pre>{@code
 * // In JdbcExtension.beforeAll:
 * if (JdbcContainerRegistry.contains(testClass)) {
 *     // Reuse existing provider
 *     provider = JdbcContainerRegistry.get(testClass);
 * } else {
 *     // Create new provider
 *     provider = JdbcContainerProviderFactory.getProvider(config);
 *     provider.start();
 *     JdbcContainerRegistry.set(testClass, provider);
 * }
 *
 * // In JdbcExtension.afterAll:
 * provider.stop();
 * JdbcContainerRegistry.clear(testClass);
 * }</pre>
 *
 * @see JdbcContainerProvider
 * @see JdbcExtension
 * @see EnableJdbcContainer
 */
public final class JdbcContainerRegistry {

  private static final Map<Class<?>, JdbcContainerProvider> providers = new ConcurrentHashMap<>();

  // Private constructor to prevent instantiation
  private JdbcContainerRegistry() {}

  /**
   * Associates a test class with its corresponding JDBC container provider.
   *
   * <p>This method registers a {@link JdbcContainerProvider} for a specific test class, making it
   * available for retrieval later. If a provider is already registered for the given test class, it
   * will be replaced with the new provider.
   *
   * <p>This method is typically called by the {@link JdbcExtension} when it creates a new provider
   * for a test class that doesn't already have one.
   *
   * @param testClass the test class to associate with the provider
   * @param provider the JDBC container provider to register
   */
  public static void set(Class<?> testClass, JdbcContainerProvider provider) {
    providers.put(testClass, provider);
  }

  /**
   * Retrieves the provider associated with the given test class.
   *
   * <p>This method returns the {@link JdbcContainerProvider} that was previously registered for the
   * specified test class using the {@link #set(Class, JdbcContainerProvider)} method. If no
   * provider has been registered for the test class, this method returns {@code null}.
   *
   * <p>This method is typically called by the {@link JdbcExtension} to check if a provider already
   * exists for a test class before creating a new one.
   *
   * @param testClass the test class whose provider to retrieve
   * @return the JDBC container provider associated with the test class, or {@code null} if none
   *     exists
   */
  public static JdbcContainerProvider get(Class<?> testClass) {
    return providers.get(testClass);
  }

  /**
   * Checks if a provider has already been registered for the given test class.
   *
   * <p>This method returns {@code true} if a {@link JdbcContainerProvider} has been registered for
   * the specified test class using the {@link #set(Class, JdbcContainerProvider)} method, and
   * {@code false} otherwise.
   *
   * <p>This method is typically called by the {@link JdbcExtension} to determine whether to create
   * a new provider or reuse an existing one.
   *
   * @param testClass the test class to check
   * @return {@code true} if a provider exists for the test class, {@code false} otherwise
   */
  public static boolean contains(Class<?> testClass) {
    return providers.containsKey(testClass);
  }

  /**
   * Clears the provider for a given test class.
   *
   * <p>This method removes the {@link JdbcContainerProvider} that was previously registered for the
   * specified test class. If no provider has been registered for the test class, this method has no
   * effect.
   *
   * <p>This method is typically called by the {@link JdbcExtension} after tests complete to clean
   * up resources and prevent memory leaks.
   *
   * @param testClass the test class whose provider to clear
   */
  public static void clear(Class<?> testClass) {
    providers.remove(testClass);
  }

  /**
   * Clears all registered providers.
   *
   * <p>This method removes all {@link JdbcContainerProvider} instances that have been registered
   * with this registry. It is useful for cleaning up all resources at once, such as during testing
   * or when the application is shutting down.
   *
   * <p>This method should be used with caution, as it affects all registered providers, potentially
   * impacting other tests that may be running concurrently.
   */
  public static void clearAll() {
    providers.clear();
  }
}
