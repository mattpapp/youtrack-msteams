package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.MessageCard;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TeamsClient {
  private final String webhookUrl;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public TeamsClient(String webhookUrl) {
    this.webhookUrl = webhookUrl;
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper();
  }

  public boolean sendMessageCard(MessageCard messageCard) {
    try {
      String json = objectMapper.writeValueAsString(messageCard);

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(webhookUrl))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(json))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      return response.statusCode() == 200;
    } catch (Exception e) {
      return false;
    }
  }
}
