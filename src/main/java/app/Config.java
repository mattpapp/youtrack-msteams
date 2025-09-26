package app;

public class Config {
  public final String ytBaseUrl;
  public final String ytToken;
  public final String ytProjectId;
  public final String teamsWebhookUrl;

  public Config() {
    this.ytBaseUrl = getRequiredEnv("YT_BASE_URL");
    this.ytToken = getRequiredEnv("YT_TOKEN");
    this.ytProjectId = getRequiredEnv("YT_PROJECT_ID");
    this.teamsWebhookUrl = getRequiredEnv("TEAMS_INCOMING_WEBHOOK_URL");
  }

  private String getRequiredEnv(String name) {
    String value = System.getenv(name);
    if (value == null || value.trim().isEmpty()) {
      throw new RuntimeException("Required environment variable not set: " + name);
    }
    return value;
  }
}
