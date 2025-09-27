package converters;

import entities.MessageCard;
import entities.MessageCardSection;
import entities.Notification;
import java.util.Arrays;
import java.util.Collections;

public class NotificationMessageCardConverter {

  public static MessageCard convert(Notification notification) {
    String content = notification.getDecodedContent();
    if (content != null) {
      content = content.replaceAll("<[^>]+>", "").trim();
    } else {
      content = "YouTrack notification";
    }

    MessageCardSection section = new MessageCardSection(content, "", Collections.emptyList());

    return new MessageCard(
        "MessageCard",
        "https://schema.org/extensions",
        "FF8C00",
        "YouTrack Notification",
        Arrays.asList(section));
  }
}
