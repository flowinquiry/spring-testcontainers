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

public class JdbcExtension implements BeforeAllCallback, AfterAllCallback {

  private static final Logger log = LoggerFactory.getLogger(JdbcExtension.class);

  private JdbcContainerProvider provider;

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
          findDirectAnnotationAnnotatedWith(
              annotation.annotationType(), EnableJdbcContainer.class, new HashSet<>());
      if (jdbcAnnotation != null) {
        EnableJdbcContainer meta =
            jdbcAnnotation.annotationType().getAnnotation(EnableJdbcContainer.class);
        return buildResolvedJdbcConfig(jdbcAnnotation, meta);
      }
    }

    return null;
  }

  private Annotation findDirectAnnotationAnnotatedWith(
      Class<? extends Annotation> annotationType,
      Class<? extends Annotation> target,
      Set<Class<?>> visited) {
    if (!visited.add(annotationType)) return null;

    for (Annotation meta : annotationType.getAnnotations()) {
      Class<? extends Annotation> metaType = meta.annotationType();

      if (metaType.equals(target)) {
        // Found an annotation directly annotated with @EnableJdbcContainer
        return annotationType.getAnnotation(metaType);
      }

      Annotation deeper = findDirectAnnotationAnnotatedWith(metaType, target, visited);
      if (deeper != null && metaType.isAnnotationPresent(target)) {
        return meta;
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
