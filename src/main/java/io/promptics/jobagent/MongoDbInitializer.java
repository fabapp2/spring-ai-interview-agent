package io.promptics.jobagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import io.promptics.jobagent.careerdata.model.CareerData;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@Slf4j
@Configuration
@Profile("mongo-in-docker")
public class MongoDbInitializer implements ApplicationRunner {
    private static final int MAX_RETRIES = 12;
    private static final int RETRY_DELAY_SEC = 10;

    private final MongoTemplate mongoTemplate;

    private final MongoClient mongoClient;

    private final ObjectMapper om;

    private final InterviewContextHolder contextHolder;

    public MongoDbInitializer(MongoTemplate mongoTemplate, MongoClient mongoClient, ObjectMapper om, InterviewContextHolder contextHolder) {
        this.mongoTemplate = mongoTemplate;
        this.mongoClient = mongoClient;
        this.om = om;
        this.contextHolder = contextHolder;
    }

    private void initDatabase() {
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        waitForMongoDb();
        initDatabase();
    }
    private void waitForMongoDb() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                mongoClient.getDatabase(mongoTemplate.getDb().getName());
                mongoTemplate.getDb().runCommand(Document.parse("{ connectionStatus: 1 }"));
                return;
            } catch (Exception e) {
                retries++;
                log.warn("Waiting for MongoDB to become available...");
                try {
                    Thread.sleep(RETRY_DELAY_SEC * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IllegalStateException("Unable to connect to MongoDB after " + MAX_RETRIES + " retries.");
    }
}