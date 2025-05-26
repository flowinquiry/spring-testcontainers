package io.flowinquiry.testcontainers.jdbc;

import java.util.List;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

public class JdbcContainerContextCustomizerFactory implements ContextCustomizerFactory {

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
