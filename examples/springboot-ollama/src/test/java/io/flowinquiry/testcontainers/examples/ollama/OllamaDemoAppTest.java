package io.flowinquiry.testcontainers.examples.ollama;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import io.flowinquiry.testcontainers.ai.EnableOllamaContainer;
import io.flowinquiry.testcontainers.ai.OllamaOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = OllamaDemoApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableOllamaContainer(
    dockerImage = "ollama/ollama",
    version = "0.9.0",
    model = "smollm2:135m",
    options = @OllamaOptions(temperature = "0.7", topP = "0.5"))
@ActiveProfiles("test")
public class OllamaDemoAppTest {

  private static final Logger log = getLogger(OllamaDemoAppTest.class);

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ChatClient.Builder chatClientBuilder;

  private ChatClient chatClient;

  @BeforeEach
  public void init() {
    this.chatClient = this.chatClientBuilder.build();
  }

  @Test
  public void testHealthEndpoint() {
    log.info("Testing health endpoint");
    String url = "http://localhost:" + port + "/api/chat/health";
    String response = restTemplate.getForObject(url, String.class);
    log.info("Health endpoint response: {}", response);
    assertNotNull(response);
    assertTrue(response.contains("Ollama Chat Controller is up and running"));
  }

  @Test
  public void testChatClient() {
    log.info("Testing chat client directly");
    String prompt = "What is Spring AI?";
    log.info("Sending prompt: {}", prompt);

    String content = chatClient.prompt().user(prompt).call().content();

    log.info("Received response: {}", content);
    assertNotNull(content);
    assertFalse(content.isEmpty());
  }
}
