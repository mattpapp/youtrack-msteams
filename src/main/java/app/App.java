package app;

import youtrack.YouTrackClient;

public class App {
  public static void main(String[] args) {
    Config config = new Config();
    YouTrackClient client = new YouTrackClient(config.ytBaseUrl, config.ytToken);

    int count = client.getNotificationCount();
    System.out.println("Found " + count + " notifications");
  }
}
