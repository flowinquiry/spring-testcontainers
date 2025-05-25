package io.flowinquiry.testcontainers.jdbc.mysql;

import io.flowinquiry.testcontainers.jdbc.DefaultJdbcContainerProvider;
import io.flowinquiry.testcontainers.jdbc.Rdbms;
import org.testcontainers.containers.MySQLContainer;

public class MySqlContainerProvider extends DefaultJdbcContainerProvider {

  @Override
  public Rdbms getType() {
    return Rdbms.MYSQL;
  }

  @Override
  protected MySQLContainer<?> createJdbcDatabaseContainer() {
    return new MySQLContainer<>(dockerImage + ":" + version);
  }
}
