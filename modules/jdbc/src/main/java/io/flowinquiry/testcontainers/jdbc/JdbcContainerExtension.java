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

public class JdbcContainerExtension extends ContainerLifecycleExtension<EnableJdbcContainer> {

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

  @Override
  protected SpringAwareContainerProvider<EnableJdbcContainer, ? extends GenericContainer<?>>
      initProvider(EnableJdbcContainer enableJdbcContainer) {
    return ServiceLoaderContainerFactory.getProvider(
        enableJdbcContainer,
        p -> p.getContainerType() == enableJdbcContainer.rdbms(),
        (prov, ann) -> prov.initContainerInstance(ann));
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
        return result;
      }
    }

    return null;
  }

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
