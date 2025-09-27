package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCard {
  @JsonProperty("@type")
  private String type = "MessageCard";

  @JsonProperty("@context")
  private String context = "https://schema.org/extensions";

  private String themeColor = "0078D4";
  private String summary;
  private List<MessageCardSection> sections;
}
