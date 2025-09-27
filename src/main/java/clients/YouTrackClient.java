package clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Notification;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class YouTrackClient {
  private final String baseUrl;
  private final String token;

  public YouTrackClient(String baseUrl, String token) {
    this.baseUrl = baseUrl;
    this.token = token;
  }

  public List<Notification> getNotificationList() {
    try {
      String jsonResponse = getNotifications();
      if (jsonResponse.startsWith("Error:")) {
        return new ArrayList<>();
      }

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(jsonResponse);
      List<Notification> notifications = new ArrayList<>();

      for (JsonNode node : root) {
        Notification notification = new Notification();
        notification.setId(node.get("id").asText());

        if (node.has("content")) {
          notification.setEncodedContent(node.get("content").asText());
        }

        if (node.has("metadata")) {
          notification.setEncodedMetadata(node.get("metadata").asText());
          String decodedMetadata = decodeBase64Gzip(node.get("metadata").asText());
          if (decodedMetadata != null) {
            try {
              JsonNode metadataJson = mapper.readTree(decodedMetadata);
              notification.setDecodedMetadata(metadataJson);
            } catch (Exception e) {
              System.err.println("Failed to parse notification metadata JSON: " + e.getMessage());
            }
          }
        }

        notifications.add(notification);
      }

      return notifications;
    } catch (Exception e) {
      System.err.println("Failed to parse YouTrack notifications: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  public String getNotifications() {
    try {
      String url = baseUrl + "/api/users/notifications?fields=id,content,metadata&$top=10";

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .header("Authorization", "Bearer " + token)
              .GET()
              .build();

      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return response.body();
      } else {
        System.err.println("YouTrack Notifications API error: HTTP " + response.statusCode());
        return "Error: " + response.statusCode();
      }
    } catch (Exception e) {
      System.err.println("YouTrack notifications connection error: " + e.getMessage());
      return "Error: " + e.getMessage();
    }
  }

  public String createIssue(String projectId, String summary) {
    try {
      String url = baseUrl + "/api/issues";
      
      String jsonPayload = String.format(
        "{\"project\":{\"id\":\"%s\"},\"summary\":\"%s\"}", 
        projectId, summary.replace("\"", "\\\""));

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Authorization", "Bearer " + token)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
          .build();

      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200 || response.statusCode() == 201) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(response.body());
        return responseJson.get("id").asText();
      } else {
        System.err.println("YouTrack create issue API error: HTTP " + response.statusCode());
        System.err.println("Response: " + response.body());
        return null;
      }
    } catch (Exception e) {
      System.err.println("Failed to create YouTrack issue: " + e.getMessage());
      return null;
    }
  }

  private String decodeBase64Gzip(String encoded) {
    try {
      byte[] decodedBytes = java.util.Base64.getDecoder().decode(encoded);
      try (java.util.zip.GZIPInputStream gzipInputStream =
          new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(decodedBytes))) {
        return new String(gzipInputStream.readAllBytes());
      }
    } catch (Exception e) {
      System.err.println("Failed to decode base64+gzip: " + e.getMessage());
      return null;
    }
  }
}
