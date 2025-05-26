package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JdbcExtension implements BeforeAllCallback, AfterAllCallback {

  private JdbcContainerProvider provider;

  @Override
  public void beforeAll(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    EnableJdbcContainer config = resolveJdbcConfig(testClass);

    if (config == null) return;

    this.provider = JdbcContainerProviderFactory.getProvider(config);
    provider.start();

    JdbcContainerRegistry.set(testClass, provider);
  }

  @Override
  public void afterAll(ExtensionContext context) {
    if (provider != null) {
      provider.stop();
      JdbcContainerRegistry.clear(context.getRequiredTestClass());
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
      EnableJdbcContainer metaAnnotation =
          annotation.annotationType().getAnnotation(EnableJdbcContainer.class);

      if (metaAnnotation != null) {
        return buildResolvedJdbcConfig(annotation, metaAnnotation);
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
