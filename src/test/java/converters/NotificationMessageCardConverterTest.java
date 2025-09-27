package converters;

import static org.junit.jupiter.api.Assertions.*;

import entities.MessageCard;
import entities.Notification;
import org.junit.jupiter.api.Test;

class NotificationMessageCardConverterTest {

  @Test
  void testBasicNotificationConversion() {
    Notification notification = new Notification();
    notification.setId("516-1");

    MessageCard card = NotificationMessageCardConverter.convert(notification);

    assertEquals("MessageCard", card.getType());
    assertEquals("YouTrack Notification", card.getSummary());
    assertEquals("FF8C00", card.getThemeColor());
    assertFalse(card.getSections().isEmpty());
  }

  @Test
  void testNotificationWithNullFields() {
    Notification notification = new Notification();
    notification.setId("516-3");

    MessageCard card = NotificationMessageCardConverter.convert(notification);

    String content = card.getSections().get(0).getActivityTitle();
    assertTrue(content.contains("Issue"));
    assertTrue(content.contains("Unknown"));
    assertTrue(content.contains("View Issue"));
  }

  @Test
  void testMessageCardStructure() {
    Notification notification = new Notification();
    notification.setId("516-4");

    MessageCard card = NotificationMessageCardConverter.convert(notification);

    assertEquals("https://schema.org/extensions", card.getContext());
    assertEquals(1, card.getSections().size());
    assertNotNull(card.getSections().get(0).getActivityTitle());
    assertEquals("", card.getSections().get(0).getActivitySubtitle());
    assertTrue(card.getSections().get(0).getFacts().isEmpty());
  }

  @Test
  void testIssueUrlGeneration() {
    Notification notification = new Notification();
    notification.setId("516-5");

    MessageCard card = NotificationMessageCardConverter.convert(notification);

    String content = card.getSections().get(0).getActivityTitle();
    assertTrue(content.contains("https://matt-papp.youtrack.cloud/issue/"));
    assertTrue(content.contains("[View Issue]"));
  }
}
