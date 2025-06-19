package io.flowinquiry.testcontainers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

/**
 * Abstract base class for Spring-aware container providers that manage TestContainers instances.
 * This class provides common functionality for initializing, starting, and stopping containers, as
 * well as applying container configuration to Spring environment.
 *
 * <p>Implementations of this class should provide specific container creation logic and environment
 * configuration for different container types (e.g., PostgreSQL, MySQL, Ollama, etc).
 *
 * @param <SELF> The specific type of GenericContainer being managed
 */
public abstract class SpringAwareContainerProvider<
        A extends Annotation, SELF extends GenericContainer<SELF>>
    implements LifecycleAware {

  private static final Logger log = LoggerFactory.getLogger(SpringAwareContainerProvider.class);

  private static boolean reuseContainerSupport =
      TestcontainersConfiguration.getInstance().environmentSupportsReuse();

  /** The version of the container image to use. */
  protected String version;

  /** The Docker image name for the container. */
  protected String dockerImage;

  protected A enableContainerAnnotation;

  /** The actual container instance being managed. */
  protected SELF container;

  public final void initContainerInstance(A enableContainerAnnotation) {
    try {
      this.enableContainerAnnotation = enableContainerAnnotation;
      Method dockerImageMethod =
          enableContainerAnnotation.annotationType().getMethod("dockerImage");
      Method versionMethod = enableContainerAnnotation.annotationType().getMethod("version");

      log.info("Initializing the container with image {}:{}", dockerImage, version);
      this.version = (String) versionMethod.invoke(enableContainerAnnotation);
      this.dockerImage = (String) dockerImageMethod.invoke(enableContainerAnnotation);

      container = createContainer();
      container.withReuse(reuseContainerSupport);
      log.info(
          "Created the container with image {}:{} with reuse {}",
          dockerImage,
          version,
          reuseContainerSupport);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException(
          "Annotation "
              + enableContainerAnnotation.annotationType().getName()
              + " must have attributes dockerImage and version");
    }
  }

  /**
   * Creates and configures a container instance. This method should be implemented by subclasses to
   * create a specific type of container with appropriate configuration.
   *
   * @return a configured container instance
   */
  protected abstract SELF createContainer();

  /** Starts the container. This method is called when the Spring context is initialized. */
  @Override
  public void start() {
    container.start();
  }

  /** Stops the container. This method is called when the Spring context is closed. */
  @Override
  public void stop() {
    if (!reuseContainerSupport) {
      container.stop();
    }
  }

  /**
   * Returns the type of container managed by this provider.
   *
   * @return the container type (e.g., POSTGRESQL, MYSQL, OLLAMA)
   */
  public abstract ContainerType getContainerType();

  /**
   * Applies container-specific configuration to the Spring environment. This method should be
   * implemented by subclasses to set appropriate properties in the Spring environment based on the
   * container configuration.
   *
   * @param environment the Spring environment to configure
   */
  public abstract void applyTo(ConfigurableEnvironment environment);
}
