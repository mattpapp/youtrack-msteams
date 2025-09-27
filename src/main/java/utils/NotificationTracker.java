package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NotificationTracker {
  private static final String LAST_SENT_FILE = ".last_sent";
  private static String cachedLastSent = null;

  public static boolean isNewNotification(String notificationId) {
    if (cachedLastSent == null) {
      cachedLastSent = getLastSent();
    }
    return !notificationId.equals(cachedLastSent);
  }

  public static void markAsSent(String notificationId) {
    cachedLastSent = notificationId;
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
