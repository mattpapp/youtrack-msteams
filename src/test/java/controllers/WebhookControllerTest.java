package controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WebhookControllerTest {

  private final WebhookController controller = new WebhookController();

  @Test
  void testEmptyCreateIssueCommand() {
    Map<String, Object> payload = Map.of("text", "/create-issue ");
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertEquals("Use /create-issue <summary> to create a YouTrack issue", result.get("text"));
  }

  @Test
  void testNonCreateIssueMessage() {
    Map<String, Object> payload = Map.of("text", "Just a regular message");
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertEquals("Use /create-issue <summary> to create a YouTrack issue", result.get("text"));
  }

  @Test
  void testMissingTextField() {
    Map<String, Object> payload = Map.of("other", "value");
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertEquals("No message content found", result.get("text"));
  }

  @Test
  void testCreateIssueCommandWithArray() {
    Map<String, Object> payload = Map.of("text", List.of("/create-issue Test issue"));
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertTrue(result.get("text").contains("created") || result.get("text").contains("Failed"));
  }

  @Test
  void testCreateIssueWithHtmlContent() {
    Map<String, Object> payload = Map.of("text", "<p>/create-issue Test with HTML</p>");
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertTrue(result.get("text").contains("created") || result.get("text").contains("Failed"));
  }

  @Test
  void testEmptyArrayHandling() {
    Map<String, Object> payload = Map.of("text", List.of());
    Map<String, String> result = controller.handleWebhook(payload);
    assertEquals("message", result.get("type"));
    assertEquals("No message content found", result.get("text"));
  }
}
