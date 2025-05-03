package io.promptics.jobagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.model.Reference;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterviewEngineTest {

    private ObjectMapper om = new ObjectMapper();

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    InterviewEngine engine;

    @Autowired
    InterviewContextHolder contextHolder;

    private String careerDataId;

    @Test
    @DisplayName("start interview")
    void startInterview() throws IOException {
        intializeMongoDb();
        InterviewContext context = new InterviewContext(careerDataId, null, "session-id", "username");
        contextHolder.setContext(context);
        String response = engine.start();
    }


    private void intializeMongoDb() throws IOException {
        CareerData cd = om.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        CareerData careerData = mongoTemplate.save(cd);
        this.careerDataId = careerData.getId();
    }

}