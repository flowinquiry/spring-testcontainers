package io.flowinquiry.testcontainers.ai;

import static io.flowinquiry.testcontainers.ContainerType.OLLAMA;

import io.flowinquiry.testcontainers.ContainerType;
import io.flowinquiry.testcontainers.SpringAwareContainerProvider;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.ollama.OllamaContainer;

/**
 * A Spring-aware container provider for Ollama AI containers.
 *
 * <p>This class manages the lifecycle of Ollama containers in a Spring environment, including
 * container creation, configuration, and integration with Spring's property system. It handles
 * pulling the specified AI model during container startup and configuring Spring AI properties to
 * connect to the containerized Ollama instance.
 *
 * <p>The provider uses the {@link EnableOllamaContainer} annotation to configure the container with
 * specific parameters such as the Docker image, version, model name, and model options (temperature
 * and top-p values).
 */
public class OllamaContainerProvider
    extends SpringAwareContainerProvider<EnableOllamaContainer, OllamaContainer> {

  private static final Logger log = LoggerFactory.getLogger(OllamaContainerProvider.class);

  /**
   * Returns the type of container managed by this provider.
   *
   * @return the OLLAMA container type
   */
  @Override
  public ContainerType getContainerType() {
    return OLLAMA;
  }

  /**
   * Creates and configures an Ollama container instance.
   *
   * @return a new OllamaContainer instance configured with the specified Docker image and version
   */
  @Override
  protected OllamaContainer createContainer() {
    return new OllamaContainer(dockerImage + ":" + version);
  }

  /**
   * Starts the Ollama container and pulls the specified AI model.
   *
   * <p>This method first calls the parent class's start method to start the container, then
   * executes the 'ollama pull' command inside the container to download the specified AI model.
   *
   * @throws RuntimeException if there is an error pulling the model
   */
  @Override
  public void start() {
    super.start();
    try {
      log.info("Starting pull model {}", enableContainerAnnotation.model());
      container.execInContainer("ollama", "pull", enableContainerAnnotation.model());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Applies Ollama-specific configuration to the Spring environment.
   *
   * <p>These properties are added to the Spring environment with high precedence to ensure they
   * override any existing configuration.
   *
   * @param environment the Spring environment to configure
   */
  @Override
  public void applyTo(ConfigurableEnvironment environment) {
    Properties props = new Properties();
    props.put("spring.ai.ollama.init.pull-model-strategy", "when_missing");
    props.put("spring.ai.ollama.chat.model", enableContainerAnnotation.model());
    props.put(
        "spring.ai.ollama.chat.options.temperature",
        enableContainerAnnotation.options().temperature());
    props.put("spring.ai.ollama.chat.options.topp", enableContainerAnnotation.options().topP());

    environment
        .getPropertySources()
        .addFirst(new PropertiesPropertySource("testcontainers", props));
  }
}
