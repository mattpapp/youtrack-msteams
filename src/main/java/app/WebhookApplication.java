package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"app", "controllers"})
public class WebhookApplication {
  public static void main(String[] args) {
    SpringApplication.run(WebhookApplication.class, args);
  }
}
