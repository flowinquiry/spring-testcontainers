package io.flowinquiry.testcontainers.jdbc;

import java.util.ServiceLoader;

public class JdbcContainerProviderFactory {

  public static JdbcContainerProvider getProvider(EnableJdbcContainer enableJdbcContainer) {
    DefaultJdbcContainerProvider provider =
            (DefaultJdbcContainerProvider)ServiceLoader.load(JdbcContainerProvider.class).stream()
            .map(ServiceLoader.Provider::get)
            .filter(p -> p.getType() == enableJdbcContainer.rdbms())
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "No provider found for " + enableJdbcContainer.rdbms()));

    provider.initContainerInstance(enableJdbcContainer.dockerImage(), enableJdbcContainer.version());
    provider.createJdbcDatabaseContainer();
    return provider;
  }
}
