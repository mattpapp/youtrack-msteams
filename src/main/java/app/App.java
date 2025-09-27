package app;

import clients.YouTrackClient;
import config.Config;
import entities.Activity;
import java.util.List;

public class App {
  public static void main(String[] args) {
    Config config = new Config();
    YouTrackClient client = new YouTrackClient(config.ytBaseUrl, config.ytToken);

    int count = client.getActivityCount();
    System.out.println("Found " + count + " activities");

    List<Activity> activities = client.getActivityList();
    System.out.println("\nRecent activities:");
    for (Activity activity : activities) {
      System.out.println("- " + activity.toString());
    }
  }
}
