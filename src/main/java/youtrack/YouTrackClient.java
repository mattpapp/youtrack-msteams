package youtrack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}
