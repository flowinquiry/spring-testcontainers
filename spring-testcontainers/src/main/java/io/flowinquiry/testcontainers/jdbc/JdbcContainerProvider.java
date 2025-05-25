package io.flowinquiry.testcontainers.jdbc;

public interface JdbcContainerProvider {

  Rdbms getType();

  void start();

  void stop();
}
