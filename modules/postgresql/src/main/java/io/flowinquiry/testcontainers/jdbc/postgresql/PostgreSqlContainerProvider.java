package io.flowinquiry.testcontainers.jdbc.postgresql;

import io.flowinquiry.testcontainers.jdbc.Rdbms;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlContainerProvider extends SpringAwareJdbcContainerProvider {

  @Override
  public Rdbms getType() {
    return Rdbms.POSTGRESQL;
  }

  @Override
  protected JdbcDatabaseContainer<?> createJdbcDatabaseContainer() {
    return new PostgreSQLContainer<>(dockerImage + ":" + version);
  }
}
