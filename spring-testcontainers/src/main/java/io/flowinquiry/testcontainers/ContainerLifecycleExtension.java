package io.flowinquiry.testcontainers;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

/**
 * Abstract base class for JUnit Jupiter extensions that manage the lifecycle of test containers.
 *
 * @param <A> the annotation type that enables and configures the container
 */
public abstract class ContainerLifecycleExtension<A extends Annotation>
    implements BeforeAllCallback, AfterAllCallback {

  private static final Logger log = LoggerFactory.getLogger(ContainerLifecycleExtension.class);

  private SpringAwareContainerProvider<? extends Annotation, ? extends GenericContainer<?>>
      provider;

  /**
   * Resolves the annotation that enables and configures the container from the test class.
   *
   * @param testClass the test class to examine for the annotation
   * @return the resolved annotation instance, or null if not present
   */
  protected abstract A getResolvedAnnotation(Class<?> testClass);

  /**
   * Initializes a container provider based on the configuration in the annotation.
   *
   * @param annotation the annotation containing container configuration
   * @return a configured container provider
   */
  protected abstract SpringAwareContainerProvider<A, ? extends GenericContainer<?>> initProvider(
      A annotation);

  /**
   * Executes before all tests in the test class.
   *
   * @param context the extension context provided by JUnit
   */
  @Override
  public void beforeAll(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    A enableContainerAnnotation = getResolvedAnnotation(testClass);

    if (enableContainerAnnotation == null) return;

    if (ContainerRegistry.contains(testClass)) {
      this.provider = ContainerRegistry.get(testClass);
    } else {
      this.provider = initProvider(enableContainerAnnotation);
      log.debug("Starting container {} for test class: {}", provider, testClass.getName());
      provider.start();
      ContainerRegistry.set(testClass, provider);
    }
  }

  /**
   * Executes after all tests in the test class have completed
   *
   * @param context the extension context provided by JUnit
   */
  @Override
  public void afterAll(ExtensionContext context) {
    if (provider != null) {
      provider.stop();
      ContainerRegistry.clear(context.getRequiredTestClass());
      log.debug(
          "Stopped container {} for test class: {}",
          provider,
          context.getRequiredTestClass().getName());
    }
  }
}
