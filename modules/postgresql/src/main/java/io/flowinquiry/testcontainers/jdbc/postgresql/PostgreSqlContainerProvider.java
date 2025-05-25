package io.flowinquiry.testcontainers.jdbc.postgresql;

import io.flowinquiry.testcontainers.jdbc.DefaultJdbcContainerProvider;
import io.flowinquiry.testcontainers.jdbc.Rdbms;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlContainerProvider extends DefaultJdbcContainerProvider {

  @Override
  public Rdbms getType() {
    return Rdbms.POSTGRESQL;
  }

  @Override
  protected JdbcDatabaseContainer<?> createJdbcDatabaseContainer() {
    return new PostgreSQLContainer<>(dockerImage+":" + version);
  }
}
