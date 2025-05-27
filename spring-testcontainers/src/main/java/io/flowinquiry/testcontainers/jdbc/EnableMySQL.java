package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling MySQL database containers in tests.
 *
 * <p>When a test class is annotated with {@code @EnableMySQL}, a MySQL container will be
 * automatically started before the tests run and stopped after they complete.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @SpringBootTest
 * @EnableMySQL
 * public class MyDatabaseTest {
 *     // Test methods...
 * }
 * }</pre>
 *
 * <p>You can customize the MySQL version and Docker image:
 *
 * <pre>{@code
 * @SpringBootTest
 * @EnableMySQL(version = "8.0.32", dockerImage = "mysql")
 * public class MyCustomMySQLTest {
 *     // Test methods...
 * }
 * }</pre>
 *
 * <p>When used with Spring Boot tests, the MySQL container's connection details will be
 * automatically configured in the Spring environment, making them available to your application and
 * tests.
 *
 * @see EnableJdbcContainer
 * @see JdbcExtension
 * @see Rdbms#MYSQL
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableJdbcContainer(rdbms = Rdbms.MYSQL)
public @interface EnableMySQL {
  /**
   * Specifies the version of the MySQL Docker image to use.
   *
   * <p>If not specified, defaults to "latest". It's recommended to specify a specific version for
   * reproducible tests.
   *
   * @return the MySQL Docker image version
   */
  String version() default "latest";

  /**
   * Specifies the Docker image to use for the MySQL container.
   *
   * <p>If not specified, defaults to "mysql" which is the official MySQL image. This can be changed
   * if you need to use a custom MySQL image or a different registry.
   *
   * @return the MySQL Docker image name (without version)
   */
  String dockerImage() default "mysql";
}
