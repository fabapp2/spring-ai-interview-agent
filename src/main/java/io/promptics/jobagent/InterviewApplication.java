package io.promptics.jobagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class InterviewApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String openaiApiKey = System.getenv("SPRING_AI_OPENAI_API_KEY");
        if(openaiApiKey == null) {
            throw new IllegalStateException("No API key found. Please provide as env 'SPRING_AI_OPENAI_API_KEY'");
        }
    }
}
