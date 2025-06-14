package io.flowinquiry.testcontainers;

/**
 * Interface for components that have a lifecycle with explicit start and stop phases.
 *
 * <p>This interface is implemented by classes that need to perform initialization when starting and
 * cleanup when stopping. It is particularly useful for managing resources that need proper setup
 * and teardown, such as containers.
 */
public interface LifecycleAware {

  /**
   * Starts the component.
   *
   * <p>This method should initialize any resources needed by the component and prepare it for use.
   * It is typically called during system initialization.
   *
   * @throws Exception if an error occurs during startup
   */
  void start() throws Exception;

  /**
   * Stops the component.
   *
   * <p>This method should release any resources held by the component and perform necessary
   * cleanup. It is typically called during system shutdown.
   *
   * @throws Exception if an error occurs during shutdown
   */
  void stop() throws Exception;
}
