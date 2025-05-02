package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.interviewplan.TopicRepository;
import io.promptics.jobagent.interviewplan.TopicThreadRepository;
import io.promptics.jobagent.interviewplan.model.Reference;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class InterviewerTest {

    @Autowired
    Interviewer interviewer;
    @Autowired
    private TopicRepository topicRepository;
    private String careerDataId;
    private String planId;

    private ObjectMapper om = new ObjectMapper();

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private InterviewPlanner interviewPlanner;
    private CareerData careerData;
    private List<Topic> plan;
    @Autowired
    private TopicThreadRepository topicThreadRepository;

    @BeforeEach
    void beforeEach() throws IOException {
        intializeMongoDb();
    }

    @Test
    @DisplayName("start interview")
    void startInterview() {
        String output = interviewer.startInterview(careerData, plan);
        assertThat(output).isNotNull();
    }

    private void intializeMongoDb() throws IOException {
        CareerData cd = om.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        careerData = mongoTemplate.save(cd);
        this.careerDataId = careerData.getId();
        Topic topic = Topic.builder()
                .careerDataId(careerDataId)
                .type(Topic.Type.BASICS)
                .reference(Reference.builder()
                        .resumeItemId("1112223334")
                        .build())
                .build();
        plan = List.of(topic);
        topicRepository.saveAll(plan);
        List<TopicThread> threads = List.of(
                TopicThread.builder()
                        .topicId(plan.get(0).getId())
                        .type(TopicThread.Type.CORE_DETAILS)
                        .focus("Phone number contains no country code.")
                        .build()
        );
        topicThreadRepository.saveAll(threads);

//        InterviewPlan interviewPlan = om.readValue(new ClassPathResource("interview-plan.json").getFile(), InterviewPlan.class);
//        interviewPlan.setCareerDataId(cd.getId());
//        InterviewPlan plan = mongoTemplate.save(interviewPlan);
//        planId = plan.getId();
    }
}