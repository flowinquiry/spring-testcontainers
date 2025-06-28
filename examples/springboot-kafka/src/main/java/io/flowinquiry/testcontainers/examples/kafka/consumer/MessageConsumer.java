package io.flowinquiry.testcontainers.examples.kafka.consumer;

import io.flowinquiry.testcontainers.examples.kafka.config.KafkaConfig;
import io.flowinquiry.testcontainers.examples.kafka.model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/** Service for consuming messages from Kafka. */
@Service
public class MessageConsumer {

  private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

  private final List<Message> receivedMessages = new ArrayList<>();
  private CountDownLatch latch = new CountDownLatch(1);

  /**
   * Receives messages from the Kafka topic.
   *
   * @param message the received message
   */
  @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "test-group")
  public void receive(Message message) {
    log.info("Received message: {}", message);
    receivedMessages.add(message);
    latch.countDown();
  }

  /**
   * Gets the list of received messages.
   *
   * @return the list of received messages
   */
  public List<Message> getReceivedMessages() {
    return new ArrayList<>(receivedMessages);
  }

  /**
   * Gets the latch that counts down when a message is received.
   *
   * @return the latch
   */
  public CountDownLatch getLatch() {
    return latch;
  }

  /**
   * Resets the latch to the specified count.
   *
   * @param count the count to reset the latch to
   */
  public void resetLatch(int count) {
    latch = new CountDownLatch(count);
  }
}
