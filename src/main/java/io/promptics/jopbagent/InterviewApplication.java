package io.promptics.jopbagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class InterviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }
}
