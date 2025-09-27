package app;

import clients.TeamsClient;
import clients.YouTrackClient;
import config.Config;
import converters.ActivityMessageCardConverter;
import entities.Activity;
import entities.MessageCard;
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

    TeamsClient teamsClient = new TeamsClient(config.teamsWebhookUrl);

    if (!activities.isEmpty()) {
      Activity firstActivity = activities.get(0);
      MessageCard messageCard = ActivityMessageCardConverter.convert(firstActivity);
      boolean success = teamsClient.sendMessageCard(messageCard);
      System.out.println("MessageCard sent: " + success);
    }
  }
}
