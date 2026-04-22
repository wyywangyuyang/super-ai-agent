# AI-Love-Agent

An intelligent pet assistant application built on Spring AI + DashScope.

## Project Overview

AI-Love-Agent is an intelligent pet assistant bot application built using the Spring AI framework and Alibaba Cloud's DashScope large language model. The application provides a simple chat interface allowing users to interact naturally with an AI pet.

## Technology Stack

- **Spring Boot** - Spring ecosystem
- **Spring AI** - AI integration framework
- **DashScope (Alibaba Cloud)** - Large language model service
- **Maven** - Project build tool

## Quick Start

### Prerequisites

- JDK 17+
- Maven 3.8+

### Configuration Steps

1. Clone the project:
```bash
git clone https://gitee.com/wyy000000000/ai-love-agent.git
cd ai-love-agent
```

2. Configure your Alibaba Cloud DashScope API Key:
Update the API key in `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    dashscope:
      api-key: your-api-key
```

3. Build and run:
```bash
./mvnw spring-boot:run
```

### API Access

- Health check: `http://localhost:8080/health`
- Application default port: 8080

## Core Features

### PetApp - Pet Chat

`PetApp` is the core chat component providing conversational interaction with the AI pet:

```java
@Autowired
private PetApp petApp;

String response = petApp.doChat("Hello", "chat-001");
```

### HealthController - Health Check

Provides a basic health check endpoint for service monitoring.

## Project Structure

```
src/
├── main/
│   ├── java/com/yy/superaiagent/
│   │   ├── SuperAiAgentApplication.java  # Spring Boot application entry
│   │   ├── app/
│   │   │   └── PetApp.java               # Pet chat component
│   │   ├── controller/
│   │   │   └── HealthController.java     # Health check controller
│   │   └── demo/invoke/
│   │       └── SpringAiAiInvoke.java     # AI invocation example
│   └── resources/
│       └── application.yml               # Application configuration
└── test/
    └── java/...                          # Test classes
```

## License

This project is intended solely for learning and communication purposes.