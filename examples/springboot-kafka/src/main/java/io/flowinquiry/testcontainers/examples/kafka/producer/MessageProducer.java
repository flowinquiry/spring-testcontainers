package io.flowinquiry.testcontainers.examples.kafka.producer;

import io.flowinquiry.testcontainers.examples.kafka.config.KafkaConfig;
import io.flowinquiry.testcontainers.examples.kafka.model.Message;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

/** Service for producing messages to Kafka. */
@Service
public class MessageProducer {

  private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);

  private final KafkaTemplate<String, Message> kafkaTemplate;

  public MessageProducer(KafkaTemplate<String, Message> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Sends a message to the Kafka topic.
   *
   * @param message the message to send
   * @return a CompletableFuture that will be completed when the send operation completes
   */
  public CompletableFuture<SendResult<String, Message>> sendMessage(Message message) {
    log.info("Sending message: {}", message);
    CompletableFuture<SendResult<String, Message>> future =
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, message);

    future.whenComplete(
        (result, ex) -> {
          if (ex == null) {
            log.info("Message sent successfully: {}", message);
            log.info("Offset: {}", result.getRecordMetadata().offset());
          } else {
            log.error("Failed to send message: {}", message, ex);
          }
        });

    return future;
  }
}
