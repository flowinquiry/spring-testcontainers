package io.flowinquiry.testcontainers.ai;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Configuration options for the Ollama AI model.
 *
 * <p>This annotation is used in conjunction with {@link EnableOllamaContainer} to configure the
 * behavior of the AI model during inference.
 *
 * <p>Example usage:
 *
 * <pre>
 * &#64;EnableOllamaContainer(
 *     model = "llama2",
 *     options = &#64;OllamaOptions(
 *         temperature = "0.7",
 *         topP = "0.9"
 *     )
 * )
 * </pre>
 *
 * @see EnableOllamaContainer
 */
@Retention(RUNTIME)
public @interface OllamaOptions {

  /**
   * Controls the randomness of the model's output.
   *
   * <p>Higher values (e.g., 0.8) make the output more random and creative, while lower values
   * (e.g., 0.2) make the output more focused and deterministic.
   *
   * @return the temperature value as a string, defaults to "0.5"
   */
  String temperature() default "0.5";

  /**
   * Controls the diversity of the model's output through nucleus sampling.
   *
   * <p>The model will only consider tokens with a cumulative probability less than topP. A higher
   * value (e.g., 0.9) will include more low-probability tokens, while a lower value (e.g., 0.5)
   * will be more selective.
   *
   * @return the top-p value as a string, defaults to "0.8"
   */
  String topP() default "0.8";
}
