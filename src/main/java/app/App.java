package app;

import clients.TeamsClient;
import clients.YouTrackClient;
import config.Config;
import converters.NotificationMessageCardConverter;
import entities.MessageCard;
import entities.Notification;
import java.util.List;

public class App {
  public static void main(String[] args) {
    Config config = new Config();
    YouTrackClient client = new YouTrackClient(config.ytBaseUrl, config.ytToken);

    List<Notification> notifications = client.getNotificationList();
    System.out.println("Found " + notifications.size() + " notifications");

    System.out.println("\nRecent notifications:");
    for (Notification notification : notifications) {
      System.out.println("- " + notification.toString());
    }

    TeamsClient teamsClient = new TeamsClient(config.teamsWebhookUrl);

    if (!notifications.isEmpty()) {
      Notification firstNotification = notifications.get(0);
      MessageCard messageCard = NotificationMessageCardConverter.convert(firstNotification);
      boolean success = teamsClient.sendMessageCard(messageCard);
      System.out.println("MessageCard sent: " + success);
    } else {
      System.out.println("No notifications found to send");
    }
  }
}
