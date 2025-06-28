package io.flowinquiry.testcontainers.examples.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.flowinquiry.testcontainers.examples.kafka.consumer.MessageConsumer;
import io.flowinquiry.testcontainers.examples.kafka.model.Message;
import io.flowinquiry.testcontainers.examples.kafka.producer.MessageProducer;
import io.flowinquiry.testcontainers.kafka.EnableKafkaContainer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableKafkaContainer
public class KafkaDemoAppTest {

  private static final Logger log = LoggerFactory.getLogger(KafkaDemoAppTest.class);

  @Autowired private MessageProducer producer;
  @Autowired private MessageConsumer consumer;

  @Test
  public void testKafkaMessaging() throws Exception {
    // Create a test message
    Message message = new Message("Test message content");

    // Send the message
    log.info("Sending test message");
    producer.sendMessage(message);

    // Wait for the consumer to receive the message
    log.info("Waiting for consumer to receive the message");
    boolean messageReceived = consumer.getLatch().await(10, TimeUnit.SECONDS);

    // Verify that the message was received
    assertTrue(messageReceived, "Message should be received within timeout");

    // Get the received messages
    List<Message> receivedMessages = consumer.getReceivedMessages();

    // Verify that exactly one message was received
    assertEquals(1, receivedMessages.size(), "Should receive exactly one message");

    // Verify the content of the received message
    Message receivedMessage = receivedMessages.get(0);
    assertNotNull(receivedMessage, "Received message should not be null");
    assertEquals(
        "Test message content", receivedMessage.getContent(), "Message content should match");

    log.info("Test completed successfully");
  }
}
