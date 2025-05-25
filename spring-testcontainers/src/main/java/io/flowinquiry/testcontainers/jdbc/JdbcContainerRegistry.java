package io.flowinquiry.testcontainers.jdbc;

import org.testcontainers.containers.JdbcDatabaseContainer;

public final class JdbcContainerRegistry {

  private static JdbcDatabaseContainer<?> container;

  private JdbcContainerRegistry() {}

  public static void setContainer(JdbcDatabaseContainer<?> c) {
    container = c;
  }

  public static JdbcDatabaseContainer<?> getContainer() {
    if (container == null) {
      throw new IllegalStateException("JDBC container has not been initialized");
    }
    return container;
  }

  public static boolean isInitialized() {
    return container != null;
  }
}
