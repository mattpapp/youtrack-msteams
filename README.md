# YouTrack Microsoft Teams Integration

Bi-directional integration connecting YouTrack and Microsoft Teams.

## Getting Started

### Prerequisites

- Java 17+ (tested with Java 21)
- Maven 3.6+
- YouTrack instance with API access
- Microsoft Teams with webhook access
- Public endpoint for webhook (e.g., localtunnel)

### 1. Get YouTrack Token

1. Go to your YouTrack instance
2. Profile → Account Security → Permanent tokens
3. Create a permanent token
4. Copy the token for configuration

### 2. Get Teams Webhook URL

1. Go to your Teams channel
2. Click on "..." → Connectors → Incoming Webhook
3. Configure the webhook and copy the URL
4. Save the URL for configuration

### 3. Set Environment Variables

The application uses environment variables for configuration:

```bash
export YT_BASE_URL="https://your-instance.youtrack.cloud"
export YT_TOKEN="your-youtrack-permanent-token"
export YT_PROJECT_ID="0-1"  # Your project ID (found in YouTrack project settings)
export TEAMS_INCOMING_WEBHOOK_URL="https://your-teams-webhook-url"
```

### 4. Clone and Build

```bash
git clone https://github.com/mattpapp/youtrack-msteams.git
cd youtrack-msteams
mvn clean compile
```

### 5. Run the Application

```bash
mvn exec:java -Dexec.mainClass="app.App"
```

The application will:
- Monitor YouTrack notifications every 15 seconds
- Send formatted message cards to Teams for new notifications
- Check for duplicate notifications using local tracking

## Features

### Part 1: YouTrack → Teams Notifications

- Polls YouTrack notifications API every 15 seconds
- Converts notifications to Teams message cards
- Includes issue details: ID, summary, project, status, priority, assignee, timestamp
- Clickable links to view issues in YouTrack

### Part 2: Teams → YouTrack Commands (via Spring Boot Webhook)

- Spring Boot webhook endpoint at `/webhook`
- Processes `/create-issue <summary>` commands from Teams
- Creates YouTrack issues in the configured project
- Returns confirmation with issue ID and clickable link
- Handles HTML-wrapped messages from Workflows

## Setting Up Teams → YouTrack Commands

### 1. Run Webhook Server

The webhook server runs on Spring Boot. Start it separately:

```bash
mvn spring-boot:run
```

This starts a server on port 8080 with endpoint `/webhook`.

### 2. Expose Webhook Publicly

```bash
npm install -g localtunnel
lt --port 8080
# Use the provided URL for Workflows
```

### 3. Configure Workflows

1. Create new flow: "When a new channel message is added"
2. Add **Condition** action
   - Set condition to: **Message body content** → **starts with** → `/create-issue`
3. In the **If yes** branch, add **HTTP** action
   - Method: **POST**
   - URI: Your localtunnel webhook URL from previous step
   - Headers: `key: Content-Type, value: application/json`
   - Body: `{"text": [Message body content]}`

### 4. Usage in Teams

Type in the Teams channel you connected your workflows to:
```
/create-issue Fix the login bug
```

The integration will create a YouTrack issue.

## Project Structure

```
src/main/java/
├── app/
│   └── App.java                   # Main notification monitor
├── clients/
│   ├── TeamsClient.java           # Teams webhook client
│   └── YouTrackClient.java        # YouTrack API client
├── config/
│   └── Config.java                # Environment variable configuration
├── controllers/
│   └── WebhookController.java     # Spring Boot webhook handler
├── converters/
│   └── NotificationMessageCardConverter.java  # Teams message formatting
├── entities/
│   ├── MessageCard.java           # Teams message card model
│   ├── MessageCardSection.java    # Message card section
│   └── Notification.java          # YouTrack notification model
└── utils/
    └── NotificationTracker.java   # Duplicate prevention
```

## Configuration Details

The `Config.java` class reads these environment variables:

- `YT_BASE_URL`: Your YouTrack instance URL
- `YT_TOKEN`: YouTrack permanent token
- `YT_PROJECT_ID`: Project ID where issues will be created (e.g., "0-1")
- `TEAMS_INCOMING_WEBHOOK_URL`: Teams channel webhook URL

## Demo Videos

- [Part 1 Demo](demos/part1-youtrack-to-teams.mp4) - YouTrack → Teams notifications
- [Part 2 Demo](demos/part2-teams-to-youtrack.mp4) - Teams → YouTrack commands

## Testing

Run the test suite:

```bash
mvn test
```

Tests include:
- **NotificationTracker**: Duplicate detection and file persistence
- **WebhookController**: Command parsing and text extraction
- **NotificationMessageCardConverter**: Message formatting

## Troubleshooting

### No notifications in Teams
- Verify all your env variables are set correctly
- Ensure application is running and polling
- Make sure you have enabled all necessary notifications (you can find them in YouTrack's settings)

### Teams commands not working
- Verify webhook endpoint is publicly accessible
- Check Workflows flow is enabled and triggering
- Check recent runs of the workflow for any network errors / condition becoming false

### Duplicate notifications
- Check if multiple app instances are running
- Verify `.last_sent` file permissions
- Check the notification polling logs

## Architecture

### YouTrack → Teams Flow
```
YouTrack API → NotificationTracker → MessageCardConverter → TeamsClient → Teams
```

### Teams → YouTrack Flow
```
Teams → Workflows → WebhookController → YouTrackClient → YouTrack
```
