package io.flowinquiry.testcontainers.examples.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for interacting with Ollama AI chat functionality. */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

  private final ChatClient chatClient;

  @Autowired
  public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  /**
   * Endpoint to generate a chat response from Ollama.
   *
   * @param message The user's message to send to Ollama
   * @return The AI-generated response
   */
  @GetMapping
  public String chat(@RequestParam String message) {
    return chatClient.prompt().user(message).call().content();
  }

  /**
   * Simple health check endpoint.
   *
   * @return A status message
   */
  @GetMapping("/health")
  public String health() {
    return "Ollama Chat Controller is up and running!";
  }
}
