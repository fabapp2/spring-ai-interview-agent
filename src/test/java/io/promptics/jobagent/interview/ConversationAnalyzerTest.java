package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.InterviewPlan;
import io.promptics.jobagent.interviewplan.InterviewPlanService;
import io.promptics.jobagent.interviewplan.TopicAndThread;
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

import java.io.IOException;

@SpringBootTest
class ConversationAnalyzerTest {

    @Autowired
    ConversationAnalyzer analyzer;

    @Autowired
    InterviewPlanService planService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private String careerDataId;

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));


    @BeforeEach
    void beforeEach() throws IOException {
        CareerData careerData = objectMapper.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        CareerData saved = mongoTemplate.save(careerData);
        careerDataId = saved.getId();
        InterviewPlan plan = objectMapper.readValue(new ClassPathResource("interview-plan.json").getFile(), InterviewPlan.class);
        plan.setCareerDataId(careerDataId);
        mongoTemplate.save(plan);
    }

    @Test
    @DisplayName("analyze")
    void analyze() {
        TopicAndThread topicAndThread = planService.findCurrentTopicAndThread(careerDataId);
        ThreadConversation conversation = new ThreadConversation();
        conversation.getEntries().add(ConversationEntry.builder()
                        .role("assistant")
                        .timestamp("2025-02-14 22:24:01")
                        .text("What did you do after leaving TechGiant January 2024 and now?")
                .build());
        String s = analyzer.analyzeUserInput(topicAndThread, conversation, "I got certified with AWS and did a deepdive into AI and large language models.");
        System.out.println(s);
    }
    
}