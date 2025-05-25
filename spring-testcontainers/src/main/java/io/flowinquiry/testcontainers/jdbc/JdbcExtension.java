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
    EnableJdbcContainer enableJdbcContainer =
        findEnableJdbcContainer(context.getRequiredTestClass());
    if (enableJdbcContainer == null) return;

    provider = JdbcContainerProviderFactory.getProvider(enableJdbcContainer);
    provider.start();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    provider.stop();
  }

  private EnableJdbcContainer findEnableJdbcContainer(Class<?> testClass) {
    // Reject direct use
    if (testClass.isAnnotationPresent(EnableJdbcContainer.class)) {
      throw new IllegalStateException(
          "@EnableJdbcContainer should not be used directly. Use @EnablePostgreSQL, @EnableMySQL, etc.");
    }

    // 2. Check meta-annotations
    for (Annotation annotation : testClass.getAnnotations()) {
      EnableJdbcContainer meta =
          annotation.annotationType().getAnnotation(EnableJdbcContainer.class);
      if (meta != null) {
        try {
          Method versionMethod = annotation.annotationType().getMethod("version");
          String version = (String) versionMethod.invoke(annotation);
          String dockerImage =
              (String) annotation.annotationType().getMethod("dockerImage").invoke(annotation);

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
        } catch (Exception e) {
          throw new RuntimeException("Failed to extract version from meta-annotation", e);
        }
      }
    }

    return null;
  }
}
