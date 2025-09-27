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
    TeamsClient teamsClient = new TeamsClient(config.teamsWebhookUrl);

    System.out.println("Starting notification monitor...");
    System.out.println("Checking for new ones every 15 seconds");

    while (true) {
      try {
        List<Notification> notifications = client.getNotificationList();
        System.out.println(
            "[" + java.time.LocalTime.now() + "] Found " + notifications.size() + " notifications");

        if (!notifications.isEmpty()) {
          Notification latestNotification = notifications.get(notifications.size() - 1);
          String latestId = latestNotification.getId();

          if (NotificationTracker.isNewNotification(latestId)) {
            System.out.println("New notification detected: " + latestNotification);

            MessageCard messageCard = NotificationMessageCardConverter.convert(latestNotification);
            boolean success = teamsClient.sendMessageCard(messageCard);

            if (success) {
              NotificationTracker.markAsSent(latestId);
              System.out.println("Notification sent to Teams successfully");
            } else {
              System.out.println("Failed to send notification to Teams");
            }
          } else {
            System.out.println("No new notifications");
          }
        } else {
          System.out.println("No notifications found");
        }

        Thread.sleep(15000);
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        try {
          Thread.sleep(15000);
        } catch (InterruptedException ie) {
          break;
        }
      }
    }
  }
}
