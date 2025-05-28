package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JUnit 5 extension that manages JDBC database containers for integration testing.
 * 
 * <p>This extension automatically starts and stops database containers based on annotations
 * present on the test class. It works with meta-annotations that are themselves annotated
 * with {@link EnableJdbcContainer}, such as {@code @EnablePostgreSQL}, {@code @EnableMySQL}, etc.
 * 
 * <p>The extension handles the lifecycle of database containers:
 * <ul>
 *   <li>Before all tests: Detects database configuration from annotations and starts the appropriate container</li>
 *   <li>After all tests: Stops the container and cleans up resources</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>
 * {@code
 * @EnablePostgreSQL
 * class MyIntegrationTest {
 *     // Test methods that require a PostgreSQL database
 * }
 * }
 * </pre>
 */
public class JdbcExtension implements BeforeAllCallback, AfterAllCallback {

  private static final Logger log = LoggerFactory.getLogger(JdbcExtension.class);

  private JdbcContainerProvider provider;

  /**
   * Called before all tests in the current test class.
   * 
   * <p>This method detects the database configuration from annotations on the test class,
   * creates the appropriate container provider, and starts the database container if needed.
   * If a container for this test class is already running, it reuses the existing container.
   *
   * @param context the extension context for the test class
   */
  @Override
  public void beforeAll(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    EnableJdbcContainer config = resolveJdbcConfig(testClass);

    if (config == null) return;

    if (JdbcContainerRegistry.contains(testClass)) {
      this.provider = JdbcContainerRegistry.get(testClass);
    } else {
      this.provider = JdbcContainerProviderFactory.getProvider(config);
      log.debug("Starting JDBC container {} for test class: {}", provider, testClass.getName());
      provider.start();
      JdbcContainerRegistry.set(testClass, provider);
    }
  }

  /**
   * Called after all tests in the current test class have completed.
   * 
   * <p>This method stops the database container if it was started by this extension
   * and removes the container reference from the registry to allow proper cleanup.
   *
   * @param context the extension context for the test class
   */
  @Override
  public void afterAll(ExtensionContext context) {
    if (provider != null) {
      provider.stop();
      JdbcContainerRegistry.clear(context.getRequiredTestClass());
      log.debug(
          "Stopped JDBC container {} for test class: {}",
          provider,
          context.getRequiredTestClass().getName());
    }
  }

  private EnableJdbcContainer resolveJdbcConfig(Class<?> testClass) {
    if (testClass.isAnnotationPresent(EnableJdbcContainer.class)) {
      throw new IllegalStateException(
          """
        @EnableJdbcContainer is a meta-annotation and should not be used directly.
        Use @EnablePostgreSQL, @EnableMySQL, etc. instead.
    """);
    }

    for (Annotation annotation : testClass.getAnnotations()) {
      Annotation jdbcAnnotation =
          findNearestAnnotationWith(annotation, EnableJdbcContainer.class, new HashSet<>());
      if (jdbcAnnotation != null) {
        EnableJdbcContainer meta =
            jdbcAnnotation.annotationType().getAnnotation(EnableJdbcContainer.class);
        return buildResolvedJdbcConfig(jdbcAnnotation, meta);
      }
    }

    return null;
  }

  private Annotation findNearestAnnotationWith(
      Annotation candidate, Class<? extends Annotation> target, Set<Class<?>> visited) {
    Class<? extends Annotation> candidateType = candidate.annotationType();
    if (!visited.add(candidateType)) return null;

    // ✅ Check if the annotation itself has the target meta-annotation
    if (candidateType.isAnnotationPresent(target)) {
      return candidate;
    }

    // 🔁 Recurse through meta-annotations
    for (Annotation meta : candidateType.getAnnotations()) {
      Annotation result = findNearestAnnotationWith(meta, target, visited);
      if (result != null) {
        return result; // propagate up the nearest match
      }
    }

    return null;
  }

  /**
   * Builds a resolved JDBC container configuration from a source annotation and its meta-annotation.
   * 
   * <p>This method extracts configuration values (version and dockerImage) from the source annotation
   * and combines them with the database type (rdbms) from the meta-annotation to create a complete
   * {@link EnableJdbcContainer} configuration.
   *
   * @param sourceAnnotation the source annotation containing version and dockerImage values
   * @param meta the meta-annotation containing the database type (rdbms)
   * @return a resolved {@link EnableJdbcContainer} configuration
   * @throws IllegalStateException if reflection fails to extract values from the source annotation
   */
  private EnableJdbcContainer buildResolvedJdbcConfig(
      Annotation sourceAnnotation, EnableJdbcContainer meta) {
    try {
      Method versionMethod = sourceAnnotation.annotationType().getMethod("version");
      Method imageMethod = sourceAnnotation.annotationType().getMethod("dockerImage");

      String version = (String) versionMethod.invoke(sourceAnnotation);
      String dockerImage = (String) imageMethod.invoke(sourceAnnotation);

      return new EnableJdbcContainer() {
        @Override
        public Rdbms rdbms() {
          return meta.rdbms();
        }

        @Override
        public String version() {
          return version;
        }

        @Override
        public String dockerImage() {
          return dockerImage;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
          return EnableJdbcContainer.class;
        }
      };

    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(
          "Failed to extract JDBC container config from annotation: "
              + sourceAnnotation.annotationType().getName(),
          e);
    }
  }
}
