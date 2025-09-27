package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationTrackerTest {

  @BeforeEach
  void setUp() throws IOException {
    Path lastSentFile = Paths.get(".last_sent");
    if (Files.exists(lastSentFile)) {
      Files.delete(lastSentFile);
    }
  }

  @Test
  void testFirstNotificationIsNew() {
    assertTrue(NotificationTracker.isNewNotification("516-1"));
  }

  @Test
  void testSameNotificationIsNotNew() {
    String notificationId = "516-1";
    assertTrue(NotificationTracker.isNewNotification(notificationId));
    NotificationTracker.markAsSent(notificationId);
    assertFalse(NotificationTracker.isNewNotification(notificationId));
  }

  @Test
  void testDifferentNotificationIsNew() {
    NotificationTracker.markAsSent("516-1");
    assertFalse(NotificationTracker.isNewNotification("516-1"));
    assertTrue(NotificationTracker.isNewNotification("516-2"));
  }

  @Test
  void testInMemoryCacheWorks() {
    String notificationId = "516-1";
    NotificationTracker.markAsSent(notificationId);
    assertFalse(NotificationTracker.isNewNotification(notificationId));
    assertFalse(NotificationTracker.isNewNotification(notificationId));
  }
}
