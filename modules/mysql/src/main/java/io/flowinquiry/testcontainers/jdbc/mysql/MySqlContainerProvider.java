package io.flowinquiry.testcontainers.jdbc.mysql;

import io.flowinquiry.testcontainers.jdbc.Rdbms;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.MySQLContainer;

/**
 * MySQL-specific implementation of the {@link SpringAwareJdbcContainerProvider}.
 *
 * <p>This class provides support for MySQL database containers in the Spring TestContainers
 * framework. It creates and manages a {@link MySQLContainer} instance, which is a TestContainers
 * implementation for MySQL databases.
 *
 * <p>This provider is automatically discovered by the {@link
 * io.flowinquiry.testcontainers.jdbc.JdbcContainerProviderFactory} using Java's ServiceLoader
 * mechanism when a test class is annotated with {@code @EnableMySQL}.
 *
 * <p>The provider handles:
 *
 * <ul>
 *   <li>Creating a MySQL container with the specified Docker image and version
 *   <li>Starting and stopping the container
 *   <li>Integrating the container with Spring's environment configuration
 * </ul>
 *
 * @see SpringAwareJdbcContainerProvider
 * @see MySQLContainer
 * @see io.flowinquiry.testcontainers.jdbc.EnableMySQL
 */
public class MySqlContainerProvider extends SpringAwareJdbcContainerProvider {

  /**
   * Returns the type of database managed by this provider.
   *
   * <p>This implementation returns {@link Rdbms#MYSQL}, indicating that this provider supports
   * MySQL databases.
   *
   * @return {@link Rdbms#MYSQL} as the supported database type
   */
  @Override
  public Rdbms getType() {
    return Rdbms.MYSQL;
  }

  /**
   * Creates a new MySQL database container.
   *
   * <p>This method creates a {@link MySQLContainer} instance configured with the Docker image and
   * version specified in the {@code @EnableMySQL} annotation on the test class.
   *
   * <p>The container is created but not started. The {@link #start()} method must be called to
   * start the container before it can be used.
   *
   * @return a new {@link MySQLContainer} instance
   */
  @Override
  protected MySQLContainer<?> createJdbcDatabaseContainer() {
    return new MySQLContainer<>(dockerImage + ":" + version);
  }
}
