package io.flowinquiry.testcontainers.jdbc.mysql;

import io.flowinquiry.testcontainers.jdbc.Rdbms;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.MySQLContainer;

public class MySqlContainerProvider extends SpringAwareJdbcContainerProvider {

  @Override
  public Rdbms getType() {
    return Rdbms.MYSQL;
  }

  @Override
  protected MySQLContainer<?> createJdbcDatabaseContainer() {
    return new MySQLContainer<>(dockerImage + ":" + version);
  }
}
