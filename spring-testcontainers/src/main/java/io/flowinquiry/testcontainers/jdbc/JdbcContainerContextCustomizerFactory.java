package io.flowinquiry.testcontainers.jdbc;

import java.util.List;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * Spring {@link ContextCustomizerFactory} that integrates TestContainers JDBC containers with
 * Spring's test context.
 *
 * <p>This factory is automatically discovered by Spring's SPI mechanism through the {@code
 * META-INF/spring.factories} file. When a Spring test context is being prepared, Spring will call
 * this factory to create a {@link ContextCustomizer} for each test class.
 *
 * <p>The customizer created by this factory:
 *
 * <ol>
 *   <li>Retrieves the {@link JdbcContainerProvider} associated with the test class from the {@link
 *       JdbcContainerRegistry}
 *   <li>If the provider is a {@link SpringAwareJdbcContainerProvider}, applies the container's
 *       connection details to the Spring environment
 * </ol>
 *
 * <p>This enables seamless integration between TestContainers database containers and Spring Boot
 * tests. When a test class is annotated with both {@code @SpringBootTest} and a database-specific
 * annotation like {@link EnablePostgreSQL} or {@link EnableMySQL}, the database connection details
 * from the TestContainers container will be automatically configured in the Spring environment,
 * making them available to your application and tests.
 *
 * <p>Example of how this works in a typical test:
 *
 * <pre>{@code
 * @SpringBootTest
 * @EnablePostgreSQL
 * public class MyDatabaseTest {
 *     // The JdbcContainerContextCustomizerFactory will automatically configure
 *     // spring.datasource.url, spring.datasource.username, and spring.datasource.password
 *     // in the Spring environment, based on the PostgreSQL container's connection details.
 *
 *     @Autowired
 *     private DataSource dataSource; // This will be connected to the PostgreSQL container
 *
 *     // Test methods...
 * }
 * }</pre>
 *
 * @see JdbcContainerProvider
 * @see SpringAwareJdbcContainerProvider
 * @see JdbcContainerRegistry
 * @see EnableJdbcContainer
 * @see EnablePostgreSQL
 * @see EnableMySQL
 */
public class JdbcContainerContextCustomizerFactory implements ContextCustomizerFactory {

  /**
   * Creates a {@link ContextCustomizer} for the specified test class.
   *
   * <p>This method is called by Spring's test context framework for each test class that uses
   * Spring's testing support. The customizer it returns is responsible for applying the database
   * container's connection details to the Spring environment.
   *
   * <p>The customizer:
   *
   * <ol>
   *   <li>Retrieves the {@link JdbcContainerProvider} associated with the test class from the
   *       {@link JdbcContainerRegistry}
   *   <li>If the provider is a {@link SpringAwareJdbcContainerProvider}, calls its {@code
   *       applyTo()} method to configure the Spring environment with the container's connection
   *       details
   * </ol>
   *
   * <p>This method is automatically called by Spring's test context framework and should not be
   * called directly.
   *
   * @param testClass the test class for which to create a context customizer
   * @param configAttributes the context configuration attributes for the test class
   * @return a {@link ContextCustomizer} that configures the Spring environment with the database
   *     container's connection details, or a no-op customizer if no container is associated with
   *     the test class
   */
  @Override
  public ContextCustomizer createContextCustomizer(
      Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
    return (context, mergedConfig) -> {
      JdbcContainerProvider provider = JdbcContainerRegistry.get(testClass);
      if (provider instanceof SpringAwareJdbcContainerProvider springAware) {
        springAware.applyTo(context.getEnvironment());
      }
    };
  }
}
