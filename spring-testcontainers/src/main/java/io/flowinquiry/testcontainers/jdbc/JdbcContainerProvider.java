package io.flowinquiry.testcontainers.jdbc;

/**
 * Interface for providers that manage JDBC database containers for testing.
 *
 * <p>This interface defines the contract for classes that create and manage database containers
 * using Testcontainers. Implementations of this interface are responsible for:
 *
 * <ol>
 *   <li>Creating a specific type of database container (PostgreSQL, MySQL, etc.)
 *   <li>Starting the container when tests begin
 *   <li>Stopping the container when tests complete
 * </ol>
 *
 * <p>This interface serves as a wrapper around TestContainers' database containers ({@link
 * org.testcontainers.containers.JdbcDatabaseContainer}), providing a standardized way to create,
 * configure, and manage these containers within the testing framework. Concrete implementations
 * like {@link SpringAwareJdbcContainerProvider} add additional functionality such as Spring
 * environment integration.
 *
 * <p>Implementations of this interface are discovered using Java's {@link java.util.ServiceLoader}
 * mechanism and are selected based on the database type specified in the {@link
 * EnableJdbcContainer} annotation.
 *
 * <p>The lifecycle of a JdbcContainerProvider is typically managed by the {@link JdbcExtension}
 * JUnit extension, which creates the provider, starts the container before tests run, and stops it
 * after tests complete.
 *
 * @see EnableJdbcContainer
 * @see JdbcExtension
 * @see JdbcContainerProviderFactory
 * @see SpringAwareJdbcContainerProvider
 * @see Rdbms
 * @see org.testcontainers.containers.JdbcDatabaseContainer
 */
public interface JdbcContainerProvider {

  /**
   * Returns the type of relational database management system this provider supports.
   *
   * <p>This method is used by the {@link JdbcContainerProviderFactory} to select the appropriate
   * provider based on the database type specified in the {@link EnableJdbcContainer} annotation.
   *
   * @return the RDBMS type supported by this provider
   */
  Rdbms getType();

  /**
   * Starts the database container.
   *
   * <p>This method is called by the {@link JdbcExtension} before tests run. Implementations should
   * create and start the container, making it ready for use by tests.
   */
  void start();

  /**
   * Stops the database container.
   *
   * <p>This method is called by the {@link JdbcExtension} after tests complete. Implementations
   * should stop and clean up the container to free resources.
   */
  void stop();
}
