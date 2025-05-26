package io.flowinquiry.testcontainers.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableJdbcContainer(rdbms = Rdbms.MYSQL)
public @interface EnableMySQL {
  String version() default "8.0.32";

  String dockerImage() default "mysql";
}
