package converters;

import entities.MessageCard;
import entities.MessageCardSection;
import entities.Notification;
import java.util.Collections;
import java.util.List;

public class NotificationMessageCardConverter {

  public static MessageCard convert(Notification notification) {
    StringBuilder markdown = new StringBuilder();

    String issueId = notification.getIssueId();
    String summary = notification.getIssueSummary();
    String project = notification.getProjectName();
    String header = notification.getHeader();
    String status = notification.getIssueStatus();
    String priority = notification.getPriority();
    String assignee = notification.getAssignee();
    String timestamp = notification.getTimestamp();

    markdown
        .append("# ")
        .append(issueId != null ? issueId : "Issue")
        .append(": ")
        .append(summary != null ? summary : "Unknown")
        .append("\n\n");

    if (project != null) {
      markdown.append("**Project:** ").append(project).append("\n\n");
    }

    markdown.append("**Action:** ").append(header != null ? header : "Unknown").append("\n\n");

    if (status != null) {
      markdown.append("**Status:** ").append(status).append("\n\n");
    }

    if (priority != null) {
      markdown.append("**Priority:** ").append(priority).append("\n\n");
    }

    if (assignee != null && !"Unassigned".equals(assignee)) {
      markdown.append("**Assignee:** ").append(assignee).append("\n\n");
    }

    if (timestamp != null) {
      markdown.append("**Time:** ").append(timestamp).append("\n\n");
    }

    String issueUrl = "https://matt-papp.youtrack.cloud/issue/" + (issueId != null ? issueId : "");
    markdown.append("[View Issue](").append(issueUrl).append(")");

    MessageCardSection section =
        new MessageCardSection(markdown.toString(), "", Collections.emptyList());

    return new MessageCard(
        "MessageCard",
        "https://schema.org/extensions",
        "FF8C00",
        "YouTrack Notification",
        List.of(section));
  }
}
