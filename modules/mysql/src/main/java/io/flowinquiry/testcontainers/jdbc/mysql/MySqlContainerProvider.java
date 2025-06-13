package io.flowinquiry.testcontainers.jdbc.mysql;

import static io.flowinquiry.testcontainers.ContainerType.MYSQL;

import io.flowinquiry.testcontainers.ContainerType;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

public class MySqlContainerProvider extends SpringAwareJdbcContainerProvider {

  @Override
  public ContainerType getContainerType() {
    return MYSQL;
  }

  @Override
  protected JdbcDatabaseContainer<?> createContainer() {
    return new MySQLContainer<>(dockerImage + ":" + version);
  }
}
