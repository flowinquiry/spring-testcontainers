package io.flowinquiry.testcontainers.jdbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JdbcContainerRegistry {

  private static final Map<Class<?>, JdbcContainerProvider> providers = new ConcurrentHashMap<>();

  private JdbcContainerRegistry() {}

  /** Associates a test class with its corresponding JDBC container provider. */
  public static void set(Class<?> testClass, JdbcContainerProvider provider) {
    providers.put(testClass, provider);
  }

  /** Retrieves the provider associated with the given test class. */
  public static JdbcContainerProvider get(Class<?> testClass) {
    return providers.get(testClass);
  }

  /** Checks if a provider has already been registered for the given test class. */
  public static boolean contains(Class<?> testClass) {
    return providers.containsKey(testClass);
  }

  /** Clears the provider for a given test class (optional cleanup). */
  public static void clear(Class<?> testClass) {
    providers.remove(testClass);
  }

  /** Clears all entries — useful in testing or teardown hooks. */
  public static void clearAll() {
    providers.clear();
  }
}
