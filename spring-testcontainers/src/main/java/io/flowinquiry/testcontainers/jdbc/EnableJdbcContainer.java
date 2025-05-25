package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * INTERNAL USE ONLY. Do not use directly.
 *
 * <p>This annotation is intended to be used as a meta-annotation on database-specific annotations
 * such as {@link EnablePostgreSQL} or {@link EnableMySQL}.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ExtendWith(JdbcExtension.class)
public @interface EnableJdbcContainer {

  Rdbms rdbms();

  String dockerImage() default "";

  String version() default "latest";
}
