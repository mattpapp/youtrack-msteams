package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
  private String id;
  private String authorLogin;
  private long timestamp;
  private String targetId;
  private String targetReadableId;
  private String targetType;
  private String activityType;

  public boolean isIssueCreated() {
    return "IssueCreatedActivityItem".equals(activityType);
  }

  public boolean isComment() {
    return "CommentActivityItem".equals(activityType);
  }

  public boolean isFieldChange() {
    return "CustomFieldActivityItem".equals(activityType);
  }

  public String getActivityDescription() {
    if (isIssueCreated()) {
      return "created issue " + (targetReadableId != null ? targetReadableId : targetId);
    } else if (isComment()) {
      if ("IssueComment".equals(targetType)) {
        return "commented on issue";
      } else {
        return "added comment";
      }
    } else if (isFieldChange()) {
      return "updated " + (targetReadableId != null ? targetReadableId : "item");
    } else {
      String target =
          targetReadableId != null ? targetReadableId : (targetId != null ? targetId : "unknown");
      return "performed "
          + (activityType != null ? activityType.replace("ActivityItem", "") : "action")
          + " on "
          + target;
    }
  }

  @Override
  public String toString() {
    return String.format("%s %s at %d", authorLogin, getActivityDescription(), timestamp);
  }
}
