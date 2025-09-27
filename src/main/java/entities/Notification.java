package entities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class Notification {
  private String id;
  private String encodedContent;
  private String encodedMetadata;
  private String decodedContent;
  private JsonNode decodedMetadata;

  public String getIssueId() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("id")) {
      return decodedMetadata.get("issue").get("id").asText();
    }
    return null;
  }

  public String getHeader() {
    if (decodedMetadata != null && decodedMetadata.has("header")) {
      return decodedMetadata.get("header").asText();
    }
    return null;
  }

  public String getIssueStatus() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("fields")) {
      for (JsonNode field : decodedMetadata.get("issue").get("fields")) {
        if ("State".equals(field.get("name").asText())) {
          return field.get("value").asText();
        }
      }
    }
    return null;
  }

  public String getIssueSummary() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("summary")) {
      return decodedMetadata.get("issue").get("summary").asText();
    }
    return null;
  }

  public String getProjectName() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("project")) {
      return decodedMetadata.get("issue").get("project").get("name").asText();
    }
    return null;
  }

  public String getPriority() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("fields")) {
      for (JsonNode field : decodedMetadata.get("issue").get("fields")) {
        if ("Priority".equals(field.get("name").asText())) {
          return field.get("value").asText();
        }
      }
    }
    return null;
  }

  public String getAssignee() {
    if (decodedMetadata != null
        && decodedMetadata.has("issue")
        && decodedMetadata.get("issue").has("fields")) {
      for (JsonNode field : decodedMetadata.get("issue").get("fields")) {
        if ("Assignee".equals(field.get("name").asText())) {
          return field.get("value").asText();
        }
      }
    }
    return null;
  }

  public String getTimestamp() {
    if (decodedMetadata != null
        && decodedMetadata.has("change")
        && decodedMetadata.get("change").has("humanReadableTimeStamp")) {
      return decodedMetadata.get("change").get("humanReadableTimeStamp").asText();
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format(
        "Notification{id='%s', issueId='%s', header='%s'}", id, getIssueId(), getHeader());
  }
}
