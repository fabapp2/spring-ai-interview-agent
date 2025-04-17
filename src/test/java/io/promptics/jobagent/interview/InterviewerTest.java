package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.InterviewPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

@SpringBootTest
@Testcontainers
class InterviewerTest {

    @Autowired
    Interviewer interviewer;
    private String careerDataId;
    private String planId;

    private ObjectMapper om = new ObjectMapper();

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void beforeEach() throws IOException {
        intializeMongoDb();
    }

    @Test
    @DisplayName("start interview")
    void startInterview() {
        InterviewContext context = new InterviewContext(careerDataId, planId, "userid", "sessionid", "Max");
        String output = interviewer.execute(context, "Start interview");
        System.out.println(output);
    }

    private void assistant(String message) {
        System.out.println("Assistant:\n  %s\n".formatted(message));
    }

    private void user(String message) {
        System.out.println("User:\n  %s\n".formatted(message));
    }

    private void intializeMongoDb() throws IOException {
        CareerData cd = om.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        CareerData careerData = mongoTemplate.save(cd);
        this.careerDataId = careerData.getId();
        InterviewPlan interviewPlan = om.readValue(new ClassPathResource("interview-plan.json").getFile(), InterviewPlan.class);
        interviewPlan.setCareerDataId(cd.getId());
        InterviewPlan plan = mongoTemplate.save(interviewPlan);
        planId = plan.getId();
    }
}