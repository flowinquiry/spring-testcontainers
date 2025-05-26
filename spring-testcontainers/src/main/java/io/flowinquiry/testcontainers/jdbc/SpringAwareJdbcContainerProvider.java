package io.flowinquiry.testcontainers.jdbc;

import java.util.Properties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;

public abstract class SpringAwareJdbcContainerProvider implements JdbcContainerProvider {

  protected JdbcDatabaseContainer jdbcDatabaseContainer;
  protected String version;
  protected String dockerImage;

  public SpringAwareJdbcContainerProvider() {}

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

  void applyTo(ConfigurableEnvironment environment) {
    Properties props = new Properties();
    props.put("spring.datasource.url", jdbcDatabaseContainer.getJdbcUrl());
    props.put("spring.datasource.username", jdbcDatabaseContainer.getUsername());
    props.put("spring.datasource.password", jdbcDatabaseContainer.getPassword());

    environment
        .getPropertySources()
        .addFirst(new PropertiesPropertySource("testcontainers", props));
  }
}
