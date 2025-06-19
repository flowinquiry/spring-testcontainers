package io.flowinquiry.testcontainers;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.testcontainers.containers.output.BaseConsumer;
import org.testcontainers.containers.output.OutputFrame;

/**
 * An implementation of {@link BaseConsumer} that routes container output to SLF4J logging. This
 * consumer allows for different log levels to be used for STDOUT and STDERR streams.
 *
 * <p>Usage example:
 *
 * <pre>
 * Logger logger = LoggerFactory.getLogger(MyClass.class);
 * GenericContainer container = new GenericContainer("some-image")
 *     .withLogConsumer(new Slf4jOutputConsumer(logger));
 * </pre>
 */
public class Slf4jOutputConsumer extends BaseConsumer<Slf4jOutputConsumer> {

  /** The SLF4J logger to which container output will be written. */
  private final Logger logger;

  /** The log level to use for STDOUT output from the container. */
  private final Level stdoutLogLevel;

  /** The log level to use for STDERR output from the container. */
  private final Level stderrLogLevel;

  /**
   * Creates a new Slf4jOutputConsumer with default log levels. STDOUT messages will be logged at
   * DEBUG level, and STDERR messages at ERROR level.
   *
   * @param logger the SLF4J logger to which container output will be written
   */
  public Slf4jOutputConsumer(Logger logger) {
    this(logger, Level.DEBUG, Level.ERROR);
  }

  /**
   * Creates a new Slf4jOutputConsumer with custom log levels for STDOUT and STDERR.
   *
   * @param logger the SLF4J logger to which container output will be written
   * @param stdoutLogLevel the log level to use for STDOUT output
   * @param stderrLogLevel the log level to use for STDERR output
   */
  public Slf4jOutputConsumer(Logger logger, Level stdoutLogLevel, Level stderrLogLevel) {
    this.logger = logger;
    this.stdoutLogLevel = stdoutLogLevel;
    this.stderrLogLevel = stderrLogLevel;
  }

  /**
   * Processes an output frame from a container and logs it using the configured SLF4J logger.
   *
   * <p>The method:
   *
   * <ul>
   *   <li>Skips null or empty frames
   *   <li>Determines the appropriate log level based on the frame type (STDOUT or STDERR)
   *   <li>Logs the message with the frame type as a prefix
   * </ul>
   *
   * @param outputFrame the output frame to process
   */
  @Override
  public void accept(OutputFrame outputFrame) {
    if (outputFrame == null || outputFrame.getBytes() == null) return;

    String message = outputFrame.getUtf8String().trim();
    if (message.isEmpty()) return;

    Level levelToUse =
        switch (outputFrame.getType()) {
          case STDOUT -> stdoutLogLevel;
          case STDERR -> stderrLogLevel;
          case END -> null;
        };

    if (levelToUse != null) {
      logAtLevel(levelToUse, "[{}] {}", outputFrame.getType(), message);
    }
  }

  /**
   * Logs a message at the specified SLF4J level.
   *
   * @param level the SLF4J level at which to log the message
   * @param format the message format string
   * @param args the arguments to be formatted into the message string
   */
  private void logAtLevel(Level level, String format, Object... args) {
    switch (level) {
      case TRACE -> logger.trace(format, args);
      case DEBUG -> logger.debug(format, args);
      case INFO -> logger.info(format, args);
      case WARN -> logger.warn(format, args);
      case ERROR -> logger.error(format, args);
    }
  }
}
