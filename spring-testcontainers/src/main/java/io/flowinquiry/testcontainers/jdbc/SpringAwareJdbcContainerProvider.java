package io.flowinquiry.testcontainers.jdbc;

import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * Abstract base class for JDBC database container providers that integrate with Spring.
 *
 * <p>This class extends {@link SpringAwareContainerProvider} specifically for JDBC database
 * containers, providing automatic configuration of Spring datasource properties based on the
 * container's connection details.
 *
 * <p>When a JDBC container is started, this provider automatically configures the Spring
 * environment with the appropriate datasource URL, username, and password properties from the
 * container.
 *
 * <p>Concrete implementations of this class should provide specific container creation logic for
 * different database types (e.g., PostgreSQL, MySQL).
 *
 * @param <T> The specific type of JdbcDatabaseContainer being managed
 * @see JdbcDatabaseContainer
 * @see EnableJdbcContainer
 */
public abstract class SpringAwareJdbcContainerProvider<T extends JdbcDatabaseContainer<T>>
    extends SpringAwareContainerProvider<EnableJdbcContainer, T> {

  private static final Logger log = LoggerFactory.getLogger(SpringAwareJdbcContainerProvider.class);

  /** Default constructor. */
  public SpringAwareJdbcContainerProvider() {}

  /**
   * Applies JDBC container configuration to the Spring environment.
   *
   * @param environment the Spring environment to configure with datasource properties
   */
  @Override
  public final void applyTo(ConfigurableEnvironment environment) {
    Properties props = new Properties();
    props.put("spring.datasource.url", container.getJdbcUrl());
    props.put("spring.datasource.username", container.getUsername());
    props.put("spring.datasource.password", container.getPassword());

    log.debug("Database container url: {}", container.getJdbcUrl());

    environment
        .getPropertySources()
        .addFirst(new PropertiesPropertySource("testcontainers", props));
  }
}
