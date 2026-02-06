# Spring AI Interview Agent

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M8-blue)
![Java](https://img.shields.io/badge/Java-17-orange)

## Intent

This project is a playground, learning, and showcase project with the intent to learn while building something useful for myself.
One day, I realized that my CV was not very up to date and I had to update it. 
I experimented with n8n to build an interview loop that would update my CV with additional information.
I found it to be a bit too complex and not very flexible and started experimenting in this repo.
Now (a few month later), that I am writing this I'd do a few things differently, and I might even do so in the future. 
But I also learned a lot and I want to share some of my learnings with you.

## Learnings
- Start with a larger model, make things work, tune for smaller model
- Saving costs through context caching in system message can be effective
- Split logic whenever possible. Allows for easier testing, failure isolation, parallelization and smaller models

## Improvements
- Use graph RAG to steer the interview
- Add STT/TTS voice control


## 🚀 Features

- **AI-Powered Interviews**: Uses Spring AI to run an interview loop via an LLM
- **Structured Data Collection**: Follows a predefined interview plan with topics and threads
- **Persistence**: Stores career data and interview progress in MongoDB
- **Preprocessing**: Includes natural language preprocessing to interpret user inputs
- **Observability**: Spring AI client observations can be enabled/adjusted via config

## 🏗️ Architecture

The application follows a Spring Boot architecture with the following key components:

- **InterviewApplication**: Main entry point for the Spring Boot application
- **InterviewEngine**: Orchestrates message handling, preprocessing, planning and persistence updates
- **Interviewer**: Uses Spring AI `ChatClient` to ask/continue questions
- **InterviewPlanner**: Manages topics/threads for the interview flow
- **CareerData**: Domain model stored in MongoDB

## 🛠️ Technical Stack

- **Spring Boot 3.4.4**: Application framework
- **Spring AI 1.0.0-M8**: AI capabilities integration
- **MongoDB**: Data persistence
- **JUnit 5**: Testing framework
- **Testcontainers**: Integration testing with containerized dependencies
- **Lombok**: Reduces boilerplate code

## ⚙️ Setup & Configuration

### Prerequisites

- Java 17+
- Maven
- MongoDB (or Docker, if you only want to run tests with Testcontainers)

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
      port: 27018
      database: interview
      username: mongouser
      password: mongopw
```

### API Key Setup

To run LLM-backed parts (OpenAI), set the API key as an environment variable:

```bash
export SPRING_AI_OPENAI_API_KEY=your-api-key-here
```

Without this environment variable, calls to the OpenAI model will fail.

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