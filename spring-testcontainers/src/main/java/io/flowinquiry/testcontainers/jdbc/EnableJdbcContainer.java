package io.flowinquiry.testcontainers.jdbc;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.flowinquiry.testcontainers.ContainerContextCustomizerFactory;
import io.flowinquiry.testcontainers.ContainerRegistry;
import io.flowinquiry.testcontainers.ContainerType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Meta-annotation for enabling JDBC database containers in tests.
 *
 * <p>This annotation is <strong>INTERNAL USE ONLY</strong> and should not be used directly on test
 * classes. Instead, use database-specific annotations such as {@link EnablePostgreSQL} or {@link
 * EnableMySQL} which are themselves annotated with this meta-annotation.
 *
 * <p>When a test class is annotated with a database-specific annotation (e.g.,
 * {@code @EnablePostgreSQL}), the {@link JdbcContainerExtension} will automatically:
 *
 * <ol>
 *   <li>Create a database container of the specified type
 *   <li>Start the container before all tests in the class
 *   <li>Register the container with the {@link ContainerRegistry}
 *   <li>Stop the container after all tests in the class
 * </ol>
 *
 * <p>When used in conjunction with Spring Boot tests, the container's connection details will be
 * automatically configured in the Spring environment through the {@link
 * ContainerContextCustomizerFactory}.
 *
 * <p>To create a new database-specific annotation, use this annotation as a meta-annotation:
 *
 * <pre>{@code
 * @Target(ElementType.TYPE)
 * @Retention(RetentionPolicy.RUNTIME)
 * @Documented
 * @EnableJdbcContainer(rdbms = Rdbms.POSTGRESQL)
 * public @interface EnablePostgreSQL {
 *   String version() default "16.3";
 *   String dockerImage() default "postgres";
 * }
 * }</pre>
 *
 * @see JdbcContainerExtension
 * @see ContainerContextCustomizerFactory
 * @see ContainerType
 * @see EnablePostgreSQL
 * @see EnableMySQL
 */
@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
@Documented
@ExtendWith(JdbcContainerExtension.class)
public @interface EnableJdbcContainer {

  /**
   * Specifies the type of relational database management system to use.
   *
   * <p>This attribute is required and must be one of the values defined in the {@link
   * ContainerType} enum. Currently supported values are {@link ContainerType#POSTGRESQL} and {@link
   * ContainerType#MYSQL}.
   *
   * @return the RDBMS type to use
   */
  ContainerType rdbms();

  /**
   * Specifies the Docker image to use for the database container.
   *
   * <p>If not specified or empty, a default image appropriate for the specified RDBMS will be used.
   * For example, "postgres" for PostgreSQL or "mysql" for MySQL.
   *
   * @return the Docker image name (without version)
   */
  String dockerImage() default "";

  /**
   * Specifies the version of the Docker image to use.
   *
   * <p>If not specified, "latest" will be used. It's recommended to specify a specific version for
   * reproducible tests.
   *
   * @return the Docker image version
   */
  String version() default "latest";
}
