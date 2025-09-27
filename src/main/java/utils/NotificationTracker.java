package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NotificationTracker {
  private static final String LAST_SENT_FILE = ".last_sent";

  public static boolean isNewNotification(String notificationId) {
    String lastSent = getLastSent();
    return !notificationId.equals(lastSent);
  }

  public static void markAsSent(String notificationId) {
    try {
      Files.writeString(Paths.get(LAST_SENT_FILE), notificationId);
    } catch (IOException ignored) {
    }
  }

  private static String getLastSent() {
    try {
      Path path = Paths.get(LAST_SENT_FILE);
      return Files.exists(path) ? Files.readString(path).trim() : "";
    } catch (IOException e) {
      return "";
    }
  }
}
