package io.flowinquiry.testcontainers.jdbc;

import java.util.ServiceLoader;

/**
 * Factory class for creating and configuring JDBC container providers.
 *
 * <p>This factory is responsible for discovering and initializing the appropriate {@link
 * JdbcContainerProvider} implementation based on the database type specified in the {@link
 * EnableJdbcContainer} annotation.
 *
 * <p>The factory uses Java's {@link ServiceLoader} mechanism to discover available provider
 * implementations. It selects the provider that matches the requested database type, initializes it
 * with the specified Docker image and version, and prepares it for use in tests.
 *
 * <p>This class is primarily used by the {@link JdbcExtension} to create container providers for
 * test classes annotated with database-specific annotations like {@code @EnablePostgreSQL} or
 * {@code @EnableMySQL}.
 *
 * @see JdbcContainerProvider
 * @see EnableJdbcContainer
 * @see ServiceLoader
 */
public class JdbcContainerProviderFactory {

  /**
   * Creates and initializes a JDBC container provider for the specified configuration.
   *
   * <p>This method:
   *
   * <ol>
   *   <li>Discovers all available {@link JdbcContainerProvider} implementations using the {@link
   *       ServiceLoader} mechanism
   *   <li>Selects the provider that supports the database type specified in the {@link
   *       EnableJdbcContainer} annotation
   *   <li>Initializes the provider with the specified Docker image and version
   *   <li>Creates the JDBC database container
   * </ol>
   *
   * @param enableJdbcContainer the annotation containing the database configuration
   * @return a fully initialized JDBC container provider ready for use in tests
   * @throws IllegalStateException if no provider is found for the specified database type
   */
  public static JdbcContainerProvider getProvider(EnableJdbcContainer enableJdbcContainer) {
    SpringAwareJdbcContainerProvider provider =
        (SpringAwareJdbcContainerProvider)
            ServiceLoader.load(JdbcContainerProvider.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(p -> p.getType() == enableJdbcContainer.rdbms())
                .findFirst()
                .orElseThrow(
                    () ->
                        new IllegalStateException(
                            "No provider found for " + enableJdbcContainer.rdbms()));

    provider.initContainerInstance(
        enableJdbcContainer.dockerImage(), enableJdbcContainer.version());
    provider.createJdbcDatabaseContainer();
    return provider;
  }
}
