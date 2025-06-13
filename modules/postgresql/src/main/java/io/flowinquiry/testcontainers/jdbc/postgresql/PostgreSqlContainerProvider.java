package io.flowinquiry.testcontainers.jdbc.postgresql;

import static io.flowinquiry.testcontainers.ContainerType.POSTGRESQL;

import io.flowinquiry.testcontainers.ContainerType;
import io.flowinquiry.testcontainers.jdbc.SpringAwareJdbcContainerProvider;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public final class PostgreSqlContainerProvider extends SpringAwareJdbcContainerProvider {

  @Override
  public ContainerType getContainerType() {
    return POSTGRESQL;
  }

  @Override
  protected JdbcDatabaseContainer<?> createContainer() {
    return new PostgreSQLContainer<>(dockerImage + ":" + version);
  }
}
