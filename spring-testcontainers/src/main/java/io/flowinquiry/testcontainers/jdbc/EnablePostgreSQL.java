package io.flowinquiry.testcontainers.jdbc;

import static io.flowinquiry.testcontainers.ContainerType.POSTGRESQL;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.flowinquiry.testcontainers.ContainerType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for enabling PostgreSQL database containers in tests.
 *
 * <p>This annotation pre-configures {@link EnableJdbcContainer} for PostgreSQL databases. When a
 * test class is annotated with {@code @EnablePostgreSQL}, a PostgreSQL container will be
 * automatically started before the tests run and stopped after they complete.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @SpringBootTest
 * @EnablePostgreSQL
 * public class MyDatabaseTest {
 *     // Test methods...
 * }
 * }</pre>
 *
 * <p>You can customize the PostgreSQL version and Docker image:
 *
 * <pre>{@code
 * @SpringBootTest
 * @EnablePostgreSQL(version = "16.3", dockerImage = "postgres")
 * public class MyCustomPostgreSQLTest {
 *     // Test methods...
 * }
 * }</pre>
 *
 * <p>When used with Spring Boot tests, the PostgreSQL container's connection details will be
 * automatically configured in the Spring environment, making them available to your application and
 * tests.
 *
 * @see EnableJdbcContainer
 * @see JdbcContainerExtension
 * @see ContainerType#POSTGRESQL
 */
@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
@Documented
@EnableJdbcContainer(rdbms = POSTGRESQL)
public @interface EnablePostgreSQL {
  /**
   * Specifies the version of the PostgreSQL Docker image to use.
   *
   * <p>If not specified, defaults to "latest". It's recommended to specify a specific version for
   * reproducible tests.
   *
   * @return the PostgreSQL Docker image version
   */
  String version() default "latest";

  /**
   * Specifies the Docker image to use for the PostgreSQL container.
   *
   * <p>If not specified, defaults to "postgres" which is the official PostgreSQL image. This can be
   * changed if you need to use a custom PostgreSQL image or a different registry.
   *
   * @return the PostgreSQL Docker image name (without version)
   */
  String dockerImage() default "postgres";
}
