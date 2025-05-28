package io.flowinquiry.testcontainers.jdbc.postgresql;

import io.flowinquiry.testcontainers.jdbc.Rdbms;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgreSQL-specific implementation of the {@link SpringAwareJdbcContainerProvider}.
 * 
 * <p>This class provides support for PostgreSQL database containers in the Spring TestContainers
 * framework. It creates and manages a {@link PostgreSQLContainer} instance, which is a TestContainers
 * implementation for PostgreSQL databases.
 * 
 * <p>This provider is automatically discovered by the {@link io.flowinquiry.testcontainers.jdbc.JdbcContainerProviderFactory}
 * using Java's ServiceLoader mechanism when a test class is annotated with
 * {@code @EnablePostgreSQL}.
 * 
 * <p>The provider handles:
 * <ul>
 *   <li>Creating a PostgreSQL container with the specified Docker image and version</li>
 *   <li>Starting and stopping the container</li>
 *   <li>Integrating the container with Spring's environment configuration</li>
 * </ul>
 * 
 * @see SpringAwareJdbcContainerProvider
 * @see PostgreSQLContainer
 * @see io.flowinquiry.testcontainers.jdbc.EnablePostgreSQL
 */
public class PostgreSqlContainerProvider extends SpringAwareJdbcContainerProvider {

  /**
   * Returns the type of database managed by this provider.
   * 
   * <p>This implementation returns {@link Rdbms#POSTGRESQL}, indicating that this provider
   * supports PostgreSQL databases.
   *
   * @return {@link Rdbms#POSTGRESQL} as the supported database type
   */
  @Override
  public Rdbms getType() {
    return Rdbms.POSTGRESQL;
  }

  /**
   * Creates a new PostgreSQL database container.
   * 
   * <p>This method creates a {@link PostgreSQLContainer} instance configured with the Docker image
   * and version specified in the {@code @EnablePostgreSQL} annotation on the test class.
   * 
   * <p>The container is created but not started. The {@link #start()} method must be called
   * to start the container before it can be used.
   *
   * @return a new {@link PostgreSQLContainer} instance
   */
  @Override
  protected JdbcDatabaseContainer<?> createJdbcDatabaseContainer() {
    return new PostgreSQLContainer<>(dockerImage + ":" + version);
  }
}
