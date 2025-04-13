# Spring AI Interview Agent

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M7-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![MongoDB](https://img.shields.io/badge/MongoDB-5.4.0-green)

A powerful AI-driven interview agent that conducts structured conversations to collect career data from users. Built with Spring AI and MongoDB for persistence.

## 🚀 Features

- **AI-Powered Interviews**: Leverages Spring AI and LLM capabilities to conduct natural conversations
- **Structured Data Collection**: Follows a predefined interview plan with topics and threads
- **Persistence**: Stores career data and interview progress in MongoDB
- **Preprocessing**: Includes natural language preprocessing to interpret user inputs
- **Observability**: Full visibility into AI model interactions for debugging and monitoring

## 🏗️ Architecture

The application follows a Spring Boot architecture with the following key components:

- **InterviewApplication**: Main entry point for the Spring Boot application
- **Interviewer**: Core component that conducts the interview using Spring AI
- **InterviewPlan**: Structure defining topics and threads for conversation flow
- **CareerData**: Domain model for storing professional information
- **MongoDB Services**: Persistence layer for storing and retrieving data

## 🛠️ Technical Stack

- **Spring Boot 3.4.4**: Application framework
- **Spring AI 1.0.0-M7**: AI capabilities integration
- **MongoDB**: Data persistence
- **JUnit 5**: Testing framework
- **Testcontainers**: Integration testing with containerized dependencies
- **Lombok**: Reduces boilerplate code

## ⚙️ Setup & Configuration

### Prerequisites

- Java 17+
- Maven
- MongoDB (or Docker for Testcontainers)

### Running the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Configuration

The application is configured via `application.yml`:

```yaml
spring:
  ai:
    chat:
      client:
        observations:
          include-input: false
  data:
    mongodb:
      host: localhost
      port: 27017
      database: baeldung
      username: admin
      password: password
```

### API Key Setup

This project requires an OpenAI API key to run. Set it as an environment variable:

```bash
export SPRING_AI_OPENAI_API_KEY=your-api-key-here
```

Without this environment variable, the application will fail to start and tests will not run properly.

## 🧪 Testing

The project includes both unit and integration tests:

```bash
# Run all tests
mvn test

# Run a specific test
mvn test -Dtest=InterviewerTest#methodName
```

## 🧠 Interview Flow

1. The system follows a structured interview plan with topics and threads
2. Each thread represents a specific area of information to collect
3. The interviewer asks one question at a time and processes the response
4. Responses are stored in MongoDB for later analysis

## 📝 License

[MIT License](LICENSE)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.