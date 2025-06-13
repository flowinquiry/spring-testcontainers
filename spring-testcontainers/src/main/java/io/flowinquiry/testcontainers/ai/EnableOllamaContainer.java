package io.flowinquiry.testcontainers.ai;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Annotation that enables and configures an Ollama container for AI testing.
 *
 * <p>This annotation is used to automatically start and stop an Ollama container during test
 * execution. It leverages the TestContainers library to manage the container lifecycle and is
 * integrated with Spring's test context framework.
 *
 * <p>When applied to a test class, this annotation will:
 *
 * <ul>
 *   <li>Start an Ollama container before tests execution
 *   <li>Configure the container with the specified model and options
 *   <li>Make the container available to the test context
 *   <li>Automatically stop and clean up the container after tests
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>
 * &#64;SpringBootTest
 * &#64;EnableOllamaContainer(
 *     dockerImage = "ollama/ollama",
 *     version = "0.1.26",
 *     model = "hf.co/microsoft/Phi-3-mini-4k-instruct-gguf"
 * )
 * public class MyAiTest {
 *     // Test methods
 * }
 * </pre>
 *
 * @see OllamaContainerExtension
 * @see OllamaOptions
 */
@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
@Documented
@ExtendWith(OllamaContainerExtension.class)
public @interface EnableOllamaContainer {

  /**
   * Specifies the version of the Ollama container to use.
   *
   * @return the container version, defaults to "latest"
   */
  String version() default "latest";

  /**
   * Specifies the Docker image to use for the Ollama container.
   *
   * @return the Docker image name, defaults to "ollama"
   */
  String dockerImage() default "ollama";

  /**
   * Specifies the AI model to load in the Ollama container.
   *
   * <p>This is a required parameter that determines which AI model will be used for inference in
   * the tests.
   *
   * @return the model identifier/name
   */
  String model();

  /**
   * Configures additional options for the Ollama AI model.
   *
   * <p>These options control the behavior of the AI model during inference, such as temperature and
   * top-p sampling parameters.
   *
   * @return the model options configuration
   * @see OllamaOptions
   */
  OllamaOptions options() default @OllamaOptions;
}
