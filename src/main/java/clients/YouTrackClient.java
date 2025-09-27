package clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Activity;
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

  public int getActivityCount() {
    try {
      String url =
          baseUrl
              + "/api/activities?categories=IssueCreatedCategory,CommentsCategory,CustomFieldCategory&reverse=true&$top=10";

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .header("Authorization", "Bearer " + token)
              .GET()
              .build();

      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        return root.size();
      } else {
        return 0;
      }
    } catch (Exception e) {
      return -1;
    }
  }

  public String getActivities() {
    try {
      String url =
          baseUrl
              + "/api/activities?categories=IssueCreatedCategory,CommentsCategory,CustomFieldCategory&reverse=true&$top=10&fields=id,author(login),timestamp,target(id,idReadable)";

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
        return "Error: " + response.statusCode();
      }
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  public List<Activity> getActivityList() {
    try {
      String jsonResponse = getActivities();
      if (jsonResponse.startsWith("Error:")) {
        return new ArrayList<>();
      }

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(jsonResponse);
      List<Activity> activities = new ArrayList<>();

      for (JsonNode node : root) {
        Activity activity = new Activity();
        activity.setId(node.get("id").asText());
        activity.setActivityType(node.get("$type").asText());
        activity.setTimestamp(node.get("timestamp").asLong());

        if (node.has("author") && node.get("author").has("login")) {
          activity.setAuthorLogin(node.get("author").get("login").asText());
        }

        if (node.has("target") && !node.get("target").isNull()) {
          JsonNode target = node.get("target");

          if (target.has("id") && !target.get("id").isNull()) {
            activity.setTargetId(target.get("id").asText());
          }

          if (target.has("$type") && !target.get("$type").isNull()) {
            activity.setTargetType(target.get("$type").asText());
          }

          if (target.has("idReadable") && !target.get("idReadable").isNull()) {
            activity.setTargetReadableId(target.get("idReadable").asText());
          }
        }

        activities.add(activity);
      }

      return activities;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }
}
