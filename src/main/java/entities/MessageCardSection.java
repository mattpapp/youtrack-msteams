package entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCardSection {
  private String activityTitle;
  private String activitySubtitle;
  private List<MessageCardFact> facts;
}
