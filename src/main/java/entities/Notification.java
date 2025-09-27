package entities;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import lombok.Data;

@Data
public class Notification {
  private String id;
  private String encodedContent;
  private String encodedMetadata;
  private String decodedContent;
  private JsonNode decodedMetadata;

  public String getDecodedContent() {
    if (decodedContent == null && encodedContent != null) {
      decodedContent = decodeBase64Gzip(encodedContent);
    }
    return decodedContent;
  }

  private String decodeBase64Gzip(String encoded) {
    try {
      byte[] decodedBytes = Base64.getDecoder().decode(encoded);
      try (GZIPInputStream gzipInputStream =
          new GZIPInputStream(new ByteArrayInputStream(decodedBytes))) {
        return new String(gzipInputStream.readAllBytes());
      }
    } catch (IOException e) {
      System.err.println("Failed to decode: " + e.getMessage());
      return encoded;
    }
  }

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

  @Override
  public String toString() {
    return String.format(
        "Notification{id='%s', issueId='%s', header='%s'}", id, getIssueId(), getHeader());
  }
}
