package youtrack;

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

  public int getNotificationCount() {
    try {
      String url = baseUrl + "/api/users/notifications";

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .header("Authorization", "Bearer " + token)
              .GET()
              .build();

      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return 1;
      } else {
        return 0;
      }
    } catch (Exception e) {
      return -1;
    }
  }
}
