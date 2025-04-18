package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Testcontainers
class InterviewPlanServiceTest {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    InterviewPlanService interviewPlanService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoTemplate template;
    private InterviewPlan saved;
    private String careerDataId = "67e98007bd5c558ba6ad93d6";

    @BeforeEach
    void beforeEach() throws IOException {
        InterviewPlan plan = loadInterviewPlan();
        plan.setCareerDataId(careerDataId);
        saved = interviewPlanService.saveInterviewPlan(plan);
    }

    @Test
    @DisplayName("store interview plan")
    void saveInterviewPlan() {
        InterviewPlan found = template.findById(saved.getId(), InterviewPlan.class);
        assertThat(found).isNotNull();
    }

    @Test
    public void verifyConnection() {
        assertThat(mongoTemplate.getDb().getName()).isEqualTo("interview");
        assertThat(mongoTemplate.collectionExists("interview_plan")).isTrue();
    }

    @Test
    @DisplayName("get currently active thread")
    void getCurrentlyActiveThread() {
        TopicAndThread currentTopicAndThread = interviewPlanService.findCurrentTopicAndThread(careerDataId);

        Thread thread = currentTopicAndThread.getThread();
        Topic topic = currentTopicAndThread.getTopic();

        assertThat(currentTopicAndThread).isNotNull();
        assertThat(thread.getIdentifier()).isEqualTo("current_status");
        assertThat(thread.getType()).isEqualTo(Thread.Type.CORE_DETAILS);
        assertThat(thread.getStatus()).isEqualTo(Thread.Status.IN_PROGRESS);
        assertThat(thread.getFocus()).isEqualTo("Determine current employment status and activities since December 2024");

        assertThat(topic.getType()).isEqualTo(Topic.Type.GAP);
        assertThat(topic.getReference().getSection()).isEqualTo(Reference.Section.WORK);
        assertThat(topic.getReference().getIdentifier().getName()).isEqualTo("TechGiant");
        assertThat(topic.getReference().getIdentifier().getStartDate()).isEqualTo("2024-01");
    }

    @Test
    @DisplayName("add to conversation")
    void addToConversation() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("complete thread and get next")
    void completeThreadAndGetNext() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("complete thread")
    void completeThread() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("add follow-up thread")
    void addFollowUpThread() {
        fail("Not implemented");
    }

    private static InterviewPlan loadInterviewPlan() throws IOException {
        ClassPathResource resource = new ClassPathResource("interview-plan.json");
        InterviewPlan interviewPlan = new ObjectMapper().readValue(resource.getFile(), InterviewPlan.class);
        return interviewPlan;
    }
}