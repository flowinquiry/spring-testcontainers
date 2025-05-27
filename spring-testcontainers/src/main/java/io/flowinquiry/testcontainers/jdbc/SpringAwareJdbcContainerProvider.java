package io.flowinquiry.testcontainers.jdbc;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * Abstract base class for JDBC container providers that integrate with Spring.
 *
 * <p>This class extends the basic {@link JdbcContainerProvider} interface with Spring-specific
 * functionality, allowing database containers to be seamlessly integrated with Spring's environment
 * and configuration system. It manages a {@link JdbcDatabaseContainer} instance and provides
 * methods to:
 *
 * <ol>
 *   <li>Initialize the container with a specific Docker image and version
 *   <li>Start and stop the container (implementing the {@link JdbcContainerProvider} interface)
 *   <li>Apply the container's connection details to a Spring environment
 * </ol>
 *
 * <p>This class is designed to be extended by concrete database-specific implementations such as
 * PostgreSQL or MySQL providers. These implementations must override the {@link
 * #createJdbcDatabaseContainer()} method to create the appropriate container type.
 *
 * <p>The Spring integration is handled through the {@link #applyTo(ConfigurableEnvironment)}
 * method, which adds the container's connection details (URL, username, password) to the Spring
 * environment as properties. This allows Spring Boot applications to automatically connect to the
 * database container without any additional configuration.
 *
 * <p>This class is used by the {@link JdbcContainerContextCustomizerFactory} to configure the
 * Spring environment for test classes annotated with database-specific annotations like {@link
 * EnablePostgreSQL} or {@link EnableMySQL}.
 *
 * @see JdbcContainerProvider
 * @see JdbcDatabaseContainer
 * @see JdbcContainerContextCustomizerFactory
 * @see EnableJdbcContainer
 * @see EnablePostgreSQL
 * @see EnableMySQL
 */
public abstract class SpringAwareJdbcContainerProvider implements JdbcContainerProvider {

  private static final Logger log = LoggerFactory.getLogger(SpringAwareJdbcContainerProvider.class);

  /**
   * The TestContainers JDBC database container managed by this provider. This container is created
   * by the {@link #createJdbcDatabaseContainer()} method and is started and stopped by the {@link
   * #start()} and {@link #stop()} methods.
   */
  protected JdbcDatabaseContainer jdbcDatabaseContainer;

  /**
   * The version of the Docker image to use for the database container. This is set by the {@link
   * #initContainerInstance(String, String)} method based on the value from the database-specific
   * annotation.
   */
  protected String version;

  /**
   * The Docker image name to use for the database container. This is set by the {@link
   * #initContainerInstance(String, String)} method based on the value from the database-specific
   * annotation.
   */
  protected String dockerImage;

  /**
   * Default constructor. Concrete subclasses must provide their own implementation of {@link
   * #createJdbcDatabaseContainer()} to create the appropriate container type.
   */
  public SpringAwareJdbcContainerProvider() {}

  /**
   * Initializes the container instance with the specified Docker image and version.
   *
   * <p>This method is called by the {@link JdbcContainerProviderFactory} when creating a provider
   * for a test class. It sets the Docker image and version to use for the container, and then
   * creates the container by calling {@link #createJdbcDatabaseContainer()}.
   *
   * @param dockerImage the Docker image name to use for the container
   * @param version the version of the Docker image to use
   */
  void initContainerInstance(String dockerImage, String version) {
    this.version = version;
    this.dockerImage = dockerImage;
    log.info("Initializing JDBC container with image {}:{}", dockerImage, version);
    jdbcDatabaseContainer = createJdbcDatabaseContainer();
  }

  /**
   * Creates a new JDBC database container of the appropriate type.
   *
   * <p>This method must be implemented by concrete subclasses to create the specific type of
   * database container they support (e.g., PostgreSQL, MySQL). The implementation should use the
   * {@link #dockerImage} and {@link #version} fields to configure the container.
   *
   * @return a new JDBC database container of the appropriate type
   */
  protected abstract JdbcDatabaseContainer<?> createJdbcDatabaseContainer();

  /**
   * Starts the database container.
   *
   * <p>This method is called by the {@link JdbcExtension} before tests run. It delegates to the
   * {@link JdbcDatabaseContainer#start()} method to start the container.
   */
  @Override
  public void start() {
    jdbcDatabaseContainer.start();
  }

  /**
   * Stops the database container.
   *
   * <p>This method is called by the {@link JdbcExtension} after tests complete. It delegates to the
   * {@link JdbcDatabaseContainer#stop()} method to stop the container and free resources.
   */
  @Override
  public void stop() {
    jdbcDatabaseContainer.stop();
  }

  /**
   * Applies the container's connection details to the Spring environment.
   *
   * <p>This method is called by the {@link JdbcContainerContextCustomizerFactory} when configuring
   * the Spring environment for a test class. It adds the container's JDBC URL, username, and
   * password as properties in the Spring environment, allowing Spring Boot applications to
   * automatically connect to the database container.
   *
   * @param environment the Spring environment to configure
   */
  void applyTo(ConfigurableEnvironment environment) {
    Properties props = new Properties();
    props.put("spring.datasource.url", jdbcDatabaseContainer.getJdbcUrl());
    props.put("spring.datasource.username", jdbcDatabaseContainer.getUsername());
    props.put("spring.datasource.password", jdbcDatabaseContainer.getPassword());

    log.debug("Database container url: {}", jdbcDatabaseContainer.getJdbcUrl());

    environment
        .getPropertySources()
        .addFirst(new PropertiesPropertySource("testcontainers", props));
  }
}
