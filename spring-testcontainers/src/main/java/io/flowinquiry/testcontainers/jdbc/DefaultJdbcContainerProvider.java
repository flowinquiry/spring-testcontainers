package io.flowinquiry.testcontainers.jdbc;

import org.testcontainers.containers.JdbcDatabaseContainer;

public abstract class DefaultJdbcContainerProvider implements JdbcContainerProvider {

  protected JdbcDatabaseContainer jdbcDatabaseContainer;
  protected String version;
  protected String dockerImage;

  public DefaultJdbcContainerProvider() {}

   void initContainerInstance(String dockerImage, String version) {
      this.version = version;
      this.dockerImage = dockerImage;
      jdbcDatabaseContainer = createJdbcDatabaseContainer();
   }

  protected abstract JdbcDatabaseContainer<?> createJdbcDatabaseContainer();

  @Override
  public void start() {
    jdbcDatabaseContainer.start();
  }

  @Override
  public void stop() {
    jdbcDatabaseContainer.stop();
  }
}
