package io.flowinquiry.testcontainers;

/**
 * Enumeration of supported container types in the spring-testcontainers framework.
 *
 * <p>This enum defines the different types of containers that can be managed by the framework. Each
 * container type corresponds to a specific implementation of {@link SpringAwareContainerProvider}
 * that handles the creation and configuration of that particular container type.
 *
 * <p>The container type is used to identify the appropriate provider and to configure Spring
 * environment properties specific to each container type.
 */
public enum ContainerType {
  /** PostgreSQL database. */
  POSTGRESQL,

  /** MySQL database. */
  MYSQL,

  /** Ollama Container */
  OLLAMA,

  /** Kafka Container */
  KAFKA;
}
