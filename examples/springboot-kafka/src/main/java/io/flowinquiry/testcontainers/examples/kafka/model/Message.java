package io.flowinquiry.testcontainers.examples.kafka.model;

import java.time.LocalDateTime;

/** A simple message model class for Kafka messages. */
public class Message {

  private String content;
  private LocalDateTime timestamp;

  // Default constructor required for JSON deserialization
  public Message() {}

  public Message(String content) {
    this.content = content;
    this.timestamp = LocalDateTime.now();
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "Message{" + "content='" + content + '\'' + ", timestamp=" + timestamp + '}';
  }
}
