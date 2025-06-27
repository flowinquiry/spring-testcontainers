package io.flowinquiry.testcontainers.jdbc;

import io.flowinquiry.testcontainers.ContainerType;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
@Documented
@EnableJdbcContainer(rdbms = ContainerType.MYSQL)
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
