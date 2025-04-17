package io.promptics.jobagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableMongoRepositories
public class InterviewApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InterviewContextHolder contextHolder;

    @Override
    public void run(ApplicationArguments args) {
        String openaiApiKey = System.getenv("SPRING_AI_OPENAI_API_KEY");
        if(openaiApiKey == null) {
            throw new IllegalStateException("No API key found. Please provide as env 'SPRING_AI_OPENAI_API_KEY'");
        }
        CareerData careerData = readCareerData();
        CareerData saved = mongoTemplate.save(careerData);
        InterviewContext context = new InterviewContext(saved.getId(), null, "session-id-123", "Max");
        contextHolder.setContext(context);
    }

    private CareerData readCareerData() {
        String filename = "career-data.json";
        return getClasspathFileContent(filename);
    }

    private CareerData getClasspathFileContent(String filename) {
        try {
            return om.readValue(new ClassPathResource(filename).getFile(), CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
