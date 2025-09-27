package controllers;

import clients.YouTrackClient;
import config.Config;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

  @PostMapping("/webhook")
  public Map<String, String> handleWebhook(@RequestBody Map<String, Object> payload) {
    try {
      String text = (String) payload.get("text");

      if (text != null && text.startsWith("/create-issue ")) {
        String summary = text.substring("/create-issue ".length()).trim();

        if (!summary.isEmpty()) {
          Config config = new Config();
          YouTrackClient client = new YouTrackClient(config.ytBaseUrl, config.ytToken);

          String issueId = client.createIssue("0-1", summary);

          if (issueId != null) {
            String issueUrl = config.ytBaseUrl + "/issue/" + issueId;
            return Map.of(
                "type",
                "message",
                "text",
                String.format("Issue created: [%s](%s)", issueId, issueUrl));
          } else {
            return Map.of(
                "type", "message",
                "text", "Failed to create issue");
          }
        }
      }

      return Map.of(
          "type", "message",
          "text", "Use /create-issue <summary> to create a YouTrack issue");

    } catch (Exception e) {
      return Map.of("type", "message", "text", "Error: " + e.getMessage());
    }
  }
}
