package io.flowinquiry.testcontainers.jdbc;

import io.flowinquiry.testcontainers.ContainerLifecycleExtension;
import io.flowinquiry.testcontainers.ContainerType;
import io.flowinquiry.testcontainers.ServiceLoaderContainerFactory;
import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.testcontainers.containers.GenericContainer;

/**
 * JUnit Jupiter extension that manages the lifecycle of JDBC database containers.
 *
 * <p>This extension is responsible for detecting and processing database-specific annotations (like
 * {@code @EnablePostgreSQL}, {@code @EnableMySQL}, etc.) that are meta-annotated with {@link
 * EnableJdbcContainer}. It handles the creation, initialization, and cleanup of database containers
 * for integration tests.
 *
 * <p>The extension works by:
 *
 * <ol>
 *   <li>Detecting database-specific annotations on test classes
 *   <li>Resolving the actual {@link EnableJdbcContainer} configuration from these annotations
 *   <li>Initializing the appropriate container provider based on the database type
 *   <li>Managing the container lifecycle through the JUnit Jupiter extension mechanism
 * </ol>
 *
 * <p>This extension prevents direct use of {@link EnableJdbcContainer} and enforces the use of
 * database-specific annotations instead.
 *
 * @see EnableJdbcContainer
 * @see ContainerLifecycleExtension
 * @see SpringAwareContainerProvider
 */
public class JdbcContainerExtension extends ContainerLifecycleExtension<EnableJdbcContainer> {

  /**
   * Resolves the {@link EnableJdbcContainer} annotation from the test class.
   *
   * <p>This method prevents direct use of {@link EnableJdbcContainer} and instead looks for
   * database-specific annotations (like {@code @EnablePostgreSQL}, {@code @EnableMySQL}, etc.) that
   * are meta-annotated with {@link EnableJdbcContainer}.
   *
   * <p>If a direct {@link EnableJdbcContainer} annotation is found, an exception is thrown.
   *
   * @param testClass the test class to examine for database-specific annotations
   * @return the resolved {@link EnableJdbcContainer} configuration, or null if no relevant
   *     annotation is found
   * @throws IllegalStateException if {@link EnableJdbcContainer} is used directly on the test class
   */
  @Override
  protected EnableJdbcContainer getResolvedAnnotation(Class<?> testClass) {
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

  /**
   * Initializes a container provider based on the configuration in the annotation.
   *
   * <p>This method uses the {@link ServiceLoaderContainerFactory} to locate and initialize the
   * appropriate container provider for the specified database type. The provider is selected based
   * on the {@link ContainerType} specified in the {@link EnableJdbcContainer} annotation.
   *
   * @param enableJdbcContainer the annotation containing the database container configuration
   * @return a configured container provider for the specified database type
   */
  @Override
  protected SpringAwareContainerProvider<EnableJdbcContainer, ? extends GenericContainer<?>>
      initProvider(EnableJdbcContainer enableJdbcContainer) {
    return ServiceLoaderContainerFactory.getProvider(
        enableJdbcContainer,
        p -> p.getContainerType() == enableJdbcContainer.rdbms(),
        (prov, ann) -> prov.initContainerInstance(ann));
  }

  /**
   * Recursively searches for an annotation that is meta-annotated with the target annotation.
   *
   * <p>This method implements a depth-first search through the annotation hierarchy to find the
   * nearest annotation that is meta-annotated with the target annotation. It uses a visited set to
   * prevent infinite recursion in case of circular annotation references.
   *
   * @param candidate the annotation to check
   * @param target the target meta-annotation to look for
   * @param visited a set of already visited annotation types to prevent infinite recursion
   * @return the found annotation that is meta-annotated with the target, or null if none is found
   */
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
        return result;
      }
    }

    return null;
  }

  /**
   * Builds a resolved {@link EnableJdbcContainer} configuration from a source annotation.
   *
   * <p>This method extracts configuration values from a database-specific annotation (like
   * {@code @EnablePostgreSQL} or {@code @EnableMySQL}) and combines them with the meta-annotation
   * information to create a complete {@link EnableJdbcContainer} configuration.
   *
   * <p>The method uses reflection to extract the 'version' and 'dockerImage' attributes from the
   * source annotation and combines them with the database type from the meta-annotation.
   *
   * @param sourceAnnotation the database-specific annotation from the test class
   * @param meta the {@link EnableJdbcContainer} meta-annotation from the database-specific
   *     annotation
   * @return a resolved {@link EnableJdbcContainer} configuration
   * @throws IllegalStateException if the required attributes cannot be extracted from the source
   *     annotation
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
        public ContainerType rdbms() {
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
