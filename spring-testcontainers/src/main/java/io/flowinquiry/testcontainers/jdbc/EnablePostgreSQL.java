package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableJdbcContainer(rdbms = Rdbms.POSTGRESQL)
public @interface EnablePostgreSQL {
  String version() default "16.3";
  String dockerImage() default "postgres";
}
