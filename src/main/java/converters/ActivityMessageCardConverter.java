package converters;

import entities.Activity;
import entities.MessageCard;
import entities.MessageCardFact;
import entities.MessageCardSection;
import java.util.Arrays;
import java.util.List;

public class ActivityMessageCardConverter {

  public static MessageCard convert(Activity activity) {
    String summary =
        String.format("%s %s", activity.getAuthorLogin(), activity.getActivityDescription());

    List<MessageCardFact> facts =
        Arrays.asList(
            new MessageCardFact(
                "Author",
                activity.getAuthorLogin() != null ? activity.getAuthorLogin() : "Unknown"),
            new MessageCardFact("Time", activity.getFormattedTimestamp()));

    if (activity.getTargetReadableId() != null) {
      facts =
          Arrays.asList(
              new MessageCardFact("Issue", activity.getTargetReadableId()),
              new MessageCardFact(
                  "Author",
                  activity.getAuthorLogin() != null ? activity.getAuthorLogin() : "Unknown"),
              new MessageCardFact("Time", activity.getFormattedTimestamp()));
    }

    MessageCardSection section =
        new MessageCardSection(activity.getActivityDescription(), "YouTrack Activity", facts);

    return new MessageCard(
        "MessageCard", "https://schema.org/extensions", "0078D4", summary, Arrays.asList(section));
  }
}
