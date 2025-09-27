package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import youtrack.YouTrackClient;

public class App {
  public static void main(String[] args) {
    Config config = new Config();
    YouTrackClient client = new YouTrackClient(config.ytBaseUrl, config.ytToken);

    int count = client.getActivityCount();
    System.out.println("Found " + count + " activities");

    String activities = client.getActivities();

    try {
      ObjectMapper mapper = new ObjectMapper();
      Object json = mapper.readValue(activities, Object.class);
      System.out.println("Activities data:");
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
    } catch (Exception e) {
      System.out.println("Raw response: " + activities);
    }
  }
}
