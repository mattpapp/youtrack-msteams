package app;

import clients.TeamsClient;
import clients.YouTrackClient;
import config.Config;
import converters.NotificationMessageCardConverter;
import entities.MessageCard;
import entities.Notification;
import java.util.List;
import utils.NotificationTracker;

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
      Notification latestNotification = notifications.get(notifications.size() - 1);
      String latestId = latestNotification.getId();

      if (NotificationTracker.isNewNotification(latestId)) {
        MessageCard messageCard = NotificationMessageCardConverter.convert(latestNotification);
        boolean success = teamsClient.sendMessageCard(messageCard);

        if (success) {
          NotificationTracker.markAsSent(latestId);
          System.out.println("New notification sent to Teams");
        } else {
          System.out.println("Failed to send notification to Teams");
        }
      } else {
        System.out.println("No new notifications, skipping...");
      }
    } else {
      System.out.println("No notifications found");
    }
  }
}
