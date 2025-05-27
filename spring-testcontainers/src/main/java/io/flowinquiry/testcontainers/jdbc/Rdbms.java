package io.flowinquiry.testcontainers.jdbc;

/**
 * Enumeration of supported relational database management systems.
 *
 * <p>This enum defines the database types that can be used with the testcontainers extension. It is
 * used in the {@link EnableJdbcContainer} annotation to specify which database type to use for
 * testing.
 *
 * <p>Currently supported database types are:
 *
 * <ul>
 *   <li>{@link #POSTGRESQL} - PostgreSQL database
 *   <li>{@link #MYSQL} - MySQL database
 * </ul>
 *
 * @see EnableJdbcContainer
 * @see JdbcContainerProvider
 * @see JdbcExtension
 */
public enum Rdbms {
  /** PostgreSQL database. */
  POSTGRESQL,

  /** MySQL database. */
  MYSQL
}
